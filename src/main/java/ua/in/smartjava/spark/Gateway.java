package ua.in.smartjava.spark;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import ua.in.smartjava.domain.boot.BootController;
import ua.in.smartjava.domain.boot.BootService;
import ua.in.smartjava.domain.device.DeviceController;
import ua.in.smartjava.domain.discovery.DiscoveryController;
import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.domain.logrecord.LogRecord;
import ua.in.smartjava.domain.logrecord.LogService;
import ua.in.smartjava.mongo.MongoData;
import ua.in.smartjava.snakeyaml.ConfigService;
import ua.in.smartjava.upnp.DiscoveryService;

import static spark.Spark.*;

@Slf4j
public class Gateway {
    public static void main(String[] args) throws IOException {
        final ConfigService configurationService = new ConfigService();

        final String alexaIp = configurationService.getAlexaIp();
        final int severMaxThreads = Integer.parseInt(configurationService.getServerConfig().getMaxThreads());
        final int serverPort = Integer.parseInt(configurationService.getServerConfig().getPort());

        //Services
        final MongoData mongoData = new MongoData(configurationService.getMongoConfig());
        final LogService logService = new LogService(mongoData.getRepository(LogRecord.class));
        final DiscoveryService discoveryService = new DiscoveryService(alexaIp, configurationService.getUpNPConfig(), logService);
        final BootService bootService = new BootService(mongoData.getRepository(Device.class), severMaxThreads);

        //React webapp
        port(serverPort);
        staticFileLocation("/webapp");

        //CORS
        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                    }
                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                    }
                    return "OK";
                });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        bootService.bootDeviceServers();

        //Controllers
        new DeviceController(mongoData.getRepository(Device.class));
        new DiscoveryController(discoveryService, mongoData.getRepository(Device.class));
        new BootController(bootService);
    }
}
