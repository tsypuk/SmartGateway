package ua.in.smartjava.spark;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import lombok.extern.slf4j.Slf4j;
import spark.Service;
import ua.in.smartjava.domain.device.DeviceController;
import ua.in.smartjava.domain.discovery.DiscoveryController;
import ua.in.smartjava.mongo.CrudRepository;
import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.mongo.MongoData;
import ua.in.smartjava.snakeyaml.ConfigService;
import ua.in.smartjava.upnp.DiscoveryService;
import ua.in.smartjava.generated.SetBinaryState;

import static spark.Spark.*;
import static ua.in.smartjava.utils.ResourceUtils.loadDataFromFile;

@Slf4j
public class Gateway {
    //TODO extract sensor state for every switch. This is emulation POC.

    //TODO add devices reload feature - trigger stream -> service.stop(); run discovery again;
    private static AtomicInteger sensor = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        final ConfigService configurationService = new ConfigService();

        final String ALEXA_IP = configurationService.getAlexaIp();
        final int SEVER_MAX_THREADS = Integer.parseInt(configurationService.getServerConfig().getMaxThreads());
        final int SERVER_PORT = Integer.parseInt(configurationService.getServerConfig().getPort());

        //Services
        MongoData mongoData = new MongoData(configurationService.getMongoConfig());
        final CrudRepository<Device> deviceRepository = mongoData.getRepository(Device.class);
        final DiscoveryService discoveryService = new DiscoveryService(ALEXA_IP, configurationService.getUpNPConfig());

        //Registering controllers
        port(SERVER_PORT);
        new DeviceController(deviceRepository);
        new DiscoveryController(discoveryService, deviceRepository);

        bootServers(deviceRepository, SEVER_MAX_THREADS);
    }

    private static void bootServers(final CrudRepository<Device> deviceRepository, int SEVER_MAX_THREADS) {
        //Registering Jetty servers for each running device
        Predicate<Device> notNullPredicate = device -> device.getIp() != null && device.getPort() != null &&
                device.getName() != null;

        deviceRepository.findAll().stream()
                .filter(notNullPredicate)
                .forEach(device -> {
                    int port = Integer.parseInt(device.getPort());
                    Service service = Service.ignite().port(port).threadPool(SEVER_MAX_THREADS);
                    log.info("Booting service on port: {}", port);
                    buildEmulatedDevice(device).accept(service);
                });
    }

    private static Consumer<Service> buildEmulatedDevice(Device device) {
        return service -> {
            post("/upnp/event/basicevent1", (request, response) -> {
                log.info("Device {} call to /upnp/event/basicevent1",device.getName());
                log.info(request.body());
                response.type("text/xml");
                return buildResponse();
            });

            service.get("/setup.xml", (request, response) -> {
                log.info(device.getName() + ": call to /setup.xml");
                response.type("text/xml");
                return buildSetup(device);
            });

            service.get("/eventservice.xml", (request, response) -> {
                log.info("Device {} call to /eventservice.xml", device.getName());
                response.type("text/xml");
                return buildEventService();
            });

            service.post("/upnp/control/basicevent1", (request, response) -> {
                log.info("Device {} call to /upnp/control/basicevent1", device.getName());
                log.info(request.body());
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

    //externalize into resource file
    private static String buildSetup(Device device) {
        return
                "<root>\n" +
                        "<device>\n" +
                        "<deviceType>urn:Belkin:device:controllee:1</deviceType>\n" +
                        "<friendlyName>" + device.getName() + "</friendlyName>\n" +
                        "<manufacturer>Belkin International Inc.</manufacturer>\n" +
                        "<modelName>Socket</modelName>\n" +
                        "<modelNumber>3.1415</modelNumber>\n" +
                        "<modelDescription>Belkin Plugin Socket 1.0</modelDescription>\n" +
                        "<UDN>uuid:Socket-1_0-" + device.getId() + "</UDN>\n" +
                        "<serialNumber>" + device.getId() + "</serialNumber>\n" +
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
        String responsePattern = loadDataFromFile("eventService.xml", "\r\n");
        return responsePattern;
    }
}
