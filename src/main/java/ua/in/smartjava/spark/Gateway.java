package ua.in.smartjava.spark;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import lombok.extern.slf4j.Slf4j;
import spark.Service;
import ua.in.smartjava.upnp.DiscoveryService;
import ua.in.smartjava.generated.SetBinaryState;

import static spark.Spark.*;

@Slf4j
public class Gateway {
    public static final int START_PORT = 5321;
    public static final String ALEXA_IP = "192.168.1.10";
    public static final String SERVER_IP = "192.168.1.6";
    //TODO extract sensor to every switch
    private static AtomicInteger sensor = new AtomicInteger();

    //Services
    private static DiscoveryService discoveryService;

    private static Consumer<Service> buildEmulatedDevices(String uuid, String serial, String deviceName) {
        return service -> {
            post("/upnp/event/basicevent1", (request, response) -> {
                log.info("/upnp/event/basicevent1" + request.userAgent());
                log.info(deviceName + request.body());
                response.type("text/xml");
                return buildResponse();
            });

            service.get("/setup.xml", (request, response) -> {
                log.info(deviceName + ": call to /setup.xml");
                response.type("text/xml");
                return buildSetup(uuid, serial, deviceName);
            });

            service.get("/eventservice.xml", (request, response) -> {
                log.info(deviceName + ": call to /eventservice.xml");
                response.type("text/xml");
                return buildEventService();
            });

            service.post("/upnp/control/basicevent1", (request, response) -> {
                log.info(deviceName + ": call to /upnp/control/basicevent1" + request.userAgent());

                try {
                    SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,
                            new ByteArrayInputStream(request.bodyAsBytes()));

                    JAXBContext jaxbContext = JAXBContext.newInstance(SetBinaryState.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    SetBinaryState sensorState = (SetBinaryState) jaxbUnmarshaller.unmarshal(soapMessage.getSOAPBody()
                            .extractContentAsDocument());

                    log.info(sensorState.getBinaryState());
                    sensor.set(Integer.parseInt(sensorState.getBinaryState()));
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }

                response.type("text/xml");
                return buildResponse();
            });
        };
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> uuids = new ArrayList<>();
        String[] deviceNames = {"transmission", "room", "garage", "projector", "kitchen"};
        List<Service> services = new ArrayList<>();
        List<String> addresses = new ArrayList<>();
            for (int i = 0; i < deviceNames.length; i++) {
                Service service = Service.ignite().port(START_PORT + i).threadPool(10);
                log.info("Booting service on port: {}", START_PORT + i);
                buildEmulatedDevices("Socket-1_0-38323636-4558-4dda-9188-cda0e66dfe6a-81" + i, "221517K0101769" + i,
                        deviceNames[i]).accept(service);
                uuids.add("Socket-1_0-38323636-4558-4dda-9188-cda0e66dfe6a-81" + i);
                services.add(service);
                addresses.add(SERVER_IP + ":" + (START_PORT + i));
            }
        discoveryService = new DiscoveryService(ALEXA_IP);
        discoveryService.startDiscoveryService(20, addresses);
    }

    //TODO Create 2 static resources for 1 and 2. Probably with soap marshaler.
    private static String buildResponse() {
        return
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas" +
                        ".xmlsoap.org/soap/encoding/\"><s:Body>\r\n" +
                        "<u:GetBinaryStateResponse xmlns:u=\"urn:Belkin:service:basicevent:1\">\r\n" +
                        "<BinaryState>" + sensor.get() + "</BinaryState>\r\n" +
                        "</u:GetBinaryStateResponse>\r\n" +
                        "</s:Body> </s:Envelope>\r\n";
    }

    private static String buildSetup(String uuid, String serial, String deviceName) {
        return
                "<root>\n" +
                        "<device>\n" +
                        "<deviceType>urn:Belkin:device:controllee:1</deviceType>\n" +
                        "<friendlyName>" + deviceName + "</friendlyName>\n" +
                        "<manufacturer>Belkin International Inc.</manufacturer>\n" +
                        "<modelName>Socket</modelName>\n" +
                        "<modelNumber>3.1415</modelNumber>\n" +
                        "<modelDescription>Belkin Plugin Socket 1.0</modelDescription>\n" +
                        "<UDN>uuid:" + uuid + "</UDN>\n" +
                        "<serialNumber>" + serial + "</serialNumber>\n" +
                        "<binaryState>0</binaryState>\n" +
                        "<serviceList>\n" +
                        "<service>\n" +
                        "<serviceType>urn:Belkin:service:basicevent:1</serviceType>\n" +
                        "<serviceId>urn:Belkin:serviceId:basicevent1</serviceId>\n" +
                        "<controlURL>/upnp/control/basicevent1</controlURL>\n" +
                        "<eventSubURL>/upnp/event/basicevent1</eventSubURL>\n" +
                        "<SCPDURL>/eventservice.xml</SCPDURL>\n" +
                        "</service>\n" +
                        "</serviceList>\n" +
                        "</device>\n" +
                        "</root>";
    }

    private static String buildEventService() {
        return
                "<scpd xmlns=\"urn:Belkin:service-1-0\">" +
                        "<actionList>" +
                        "<action>" +
                        "<name>SetBinaryState</name>" +
                        "<argumentList>" +
                        "<argument>" +
                        "<retval/>" +
                        "<name>BinaryState</name>" +
                        "<relatedStateVariable>BinaryState</relatedStateVariable>" +
                        "<direction>in</direction>" +
                        "</argument>" +
                        "</argumentList>" +
                        "</action>" +
                        "<action>" +
                        "<name>GetBinaryState</name>" +
                        "<argumentList>" +
                        "<argument>" +
                        "<retval/>" +
                        "<name>BinaryState</name>" +
                        "<relatedStateVariable>BinaryState</relatedStateVariable>" +
                        "<direction>out</direction>" +
                        "</argument>" +
                        "</argumentList>" +
                        "</action>" +
                        "</actionList>" +
                        "<serviceStateTable>" +
                        "<stateVariable sendEvents=\"yes\">" +
                        "<name>BinaryState</name>" +
                        "<dataType>Boolean</dataType>" +
                        "<defaultValue>0</defaultValue>" +
                        "</stateVariable>" +
                        "<stateVariable sendEvents=\"yes\">" +
                        "<name>level</name>" +
                        "<dataType>string</dataType>" +
                        "<defaultValue>0</defaultValue>" +
                        "</stateVariable>" +
                        "</serviceStateTable>" +
                        "</scpd>";
    }
}
