package ua.in.smartjava.upnp;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.domain.logrecord.LogService;
import ua.in.smartjava.snakeyaml.UPnPConfig;

/**
 * UPnP discovery service
 */
public class DiscoveryService {

    private static final ScheduledExecutorService discoverableServicesExecutor = Executors.newScheduledThreadPool(2);
    private final UPnPDiscoverable uPnPDiscoverable;
    private final LogService logService;

    public DiscoveryService(String ip, UPnPConfig uPnPConfig, LogService logService) {
        this.logService = logService;
        uPnPDiscoverable = new UPnPDiscoverable(ip, uPnPConfig);
    }

    /**
     * @param discoveryTime in seconds service will wait for search requests
     */
    public void startDiscoveryService(int discoveryTime, List<Device> devices) {
        logService.logAction(MessageFormat.format("Started discovery of {0} devices for {1} seconds.", devices.size()
                , discoveryTime));
        uPnPDiscoverable.setDevices(devices);
        uPnPDiscoverable.setStatus(true);
        discoverableServicesExecutor.submit(uPnPDiscoverable);
        discoverableServicesExecutor.schedule(() -> {
                    uPnPDiscoverable.setStatus(false);
                    logService.logAction("Discovery Stopped.");
                }, discoveryTime, TimeUnit.SECONDS);
    }

    public void stopDiscoveryService() {
        logService.logAction("Discovery Stopped.");
        uPnPDiscoverable.setStatus(false);
    }
}
