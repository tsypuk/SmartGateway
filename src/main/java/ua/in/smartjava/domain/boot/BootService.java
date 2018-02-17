package ua.in.smartjava.domain.boot;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import lombok.extern.slf4j.Slf4j;
import spark.Service;
import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.domain.device.DeviceRepository;
import ua.in.smartjava.domain.logrecord.LogService;
import ua.in.smartjava.generated.SetBinaryState;

import static java.text.MessageFormat.format;
import static ua.in.smartjava.utils.ResourceUtils.loadDataFromFile;

@Slf4j
public class BootService {

    private static final String setupResponse = loadDataFromFile("setup.xml", "\r\n");
    private static final String controlBasicEventResponse = loadDataFromFile("controlBasicEvent.xml", "\r\n");
    private static final String eventServiceResponse = loadDataFromFile("eventService.xml", "\r\n");

    private final List<Service> bootServers = new ArrayList<>();
    private final DeviceRepository deviceRepository;
    private final int SEVER_MAX_THREADS;

    private final LogService logService;

    public BootService(DeviceRepository deviceRepository, int sever_max_threads, LogService logService) {
        this.deviceRepository = deviceRepository;
        SEVER_MAX_THREADS = sever_max_threads;
        this.logService = logService;
    }

    public void bootDeviceServers() {
        stopDeviceServers();
        //Registering Jetty servers for each running device
        Predicate<Device> notNullPredicate = device -> device.getIp() != null && device.getPort() != null &&
                device.getName() != null;
//TODO read only unique ip/port (do not read if was the same) OR create validation of device list with error message
        deviceRepository.findAll().stream()
                .filter(notNullPredicate)
                .forEach(device -> {
                    int port = Integer.parseInt(device.getPort());
                    Service service = Service.ignite().port(port).threadPool(SEVER_MAX_THREADS);
                    log.info("Booting service on port: {}", port);
                    buildEmulatedDevice(device).accept(service);
                    bootServers.add(service);
                });
        logService.logAction(format("Starting emulated devices {0}", bootServers.size()));
    }

    public void stopDeviceServers() {
        bootServers.stream()
                .forEach(Service::stop);
        logService.logAction(format("Stopped {0} device emulated services.", bootServers.size()));
        bootServers.clear();
    }

    private Consumer<Service> buildEmulatedDevice(Device device) {
        return service -> {
            service.post("/upnp/event/basicevent1", (request, response) -> {
                log.info("Device {} call to /upnp/event/basicevent1", device.getName());
                log.info(request.body());
                response.type("text/xml");
//                TODO here return always 1. Check if alexa or google home work with event
                return buildResponse("1");
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
                SetBinaryState sensorState = null;
                try {
                    SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null,
                            new ByteArrayInputStream(request.bodyAsBytes()));

//                    TODO Ios/Android app is doing additional GetBinaryState calls to this endpoint
//                    TODO use flow statement to verify it too
                    JAXBContext jaxbContext = JAXBContext.newInstance(SetBinaryState.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    sensorState = (SetBinaryState) jaxbUnmarshaller.unmarshal(soapMessage.getSOAPBody()
                            .extractContentAsDocument());

                    log.info(sensorState.getBinaryState());
                    if (sensorState.getBinaryState().equalsIgnoreCase("1")) {
                        deviceRepository.turnOn(device.getId());
                    } else {
                        deviceRepository.turnOff(device.getId());
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }

                response.type("text/xml");
                return buildResponse(sensorState.getBinaryState());
            });
        };
    }

    private String buildResponse(String state) {
        return MessageFormat.format(controlBasicEventResponse, state);
    }

    private static String buildSetup(Device device) {
        return MessageFormat.format(setupResponse, device.getName(), device.getId());
    }

    private static String buildEventService() {
        return eventServiceResponse;
    }
}
