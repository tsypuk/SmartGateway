package ua.in.smartjava.spark;

import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import ua.in.smartjava.generated.SetBinaryState;

import static spark.Spark.*;

public class SimpleExample {
    private static AtomicInteger sensor = new AtomicInteger();

    public static void main(String[] args) {

        port(1900);

        get("/setup.xml", (request, response) -> {
            System.out.println("Call to /setup.xml");
            return buildSetup();
        });

        get("/eventservice.xml", (request, response) -> {
            System.out.println("Call to /eventservice.xml");
            response.type("text/xml");
            response.body(buildEventService());
            return response;

        });

        post("/upnp/control/basicevent1", (request, response) -> {
            System.out.println("Call to /upnp/control/basicevent1" + request.userAgent());
            System.out.println(request.host());
            System.out.println(request.port());
            System.out.println(request.body());

            try {
                SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,
                        new ByteArrayInputStream(request.bodyAsBytes()));

                JAXBContext jaxbContext = JAXBContext.newInstance(SetBinaryState.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                SetBinaryState sensorState = (SetBinaryState) jaxbUnmarshaller.unmarshal(soapMessage.getSOAPBody().extractContentAsDocument());


                System.out.println(sensorState.getBinaryState());
                sensor.set(Integer.parseInt(sensorState.getBinaryState()));
            } catch (Exception ex) {
                System.out.println("Was exception: " +ex);
            }

            response.type("text/xml");
            response.body(buildResponse());
            System.out.println("RESPONSE: " + buildResponse());
            return response;
        });

        post("/upnp/event/basicevent1", (request, response) -> {
            System.out.println("/upnp/event/basicevent1" + request.userAgent());
            System.out.println(request.body());
            response.type("text/xml");
            response.body(buildResponse());
            return response;
        });

    }

    private static String buildResponse() {
        return
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>\r\n" +
                "<u:GetBinaryStateResponse xmlns:u=\"urn:Belkin:service:basicevent:1\">\r\n" +
                "<BinaryState>" + sensor.get() +"</BinaryState>\r\n" +
                "</u:GetBinaryStateResponse>\r\n" +
                "</s:Body> </s:Envelope>\r\n";
    }

    private static String buildSetup() {
        return
                "<root>\n" +
                        "<device>\n" +
                        "<deviceType>urn:Belkin:device:controllee:1</deviceType>\n" +
                        "<friendlyName>robot</friendlyName>\n" +
                        "<manufacturer>Belkin International Inc.</manufacturer>\n" +
                        "<modelName>Socket</modelName>\n" +
                        "<modelNumber>3.1415</modelNumber>\n" +
                        "<modelDescription>Belkin Plugin Socket 1.0</modelDescription>\n" +
                        "<UDN>uuid:Socket-1_0-38323636-4558-4dda-9188-cda0e66dfe6a-81</UDN>\n" +
                        "<serialNumber>221517K0101769</serialNumber>\n" +
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