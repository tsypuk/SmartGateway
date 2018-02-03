package ua.in.smartjava.upnp;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.snakeyaml.UPnPConfig;

/**
 * UPnP discovery service
 */
public class DiscoveryService {

    private static final ScheduledExecutorService discoverableServicesExecutor = Executors.newScheduledThreadPool(2);
    private final UPnPDiscoverable uPnPDiscoverable;

    public DiscoveryService(String ip, UPnPConfig uPnPConfig) {
        uPnPDiscoverable = new UPnPDiscoverable(ip, uPnPConfig);
    }

    /**
     *
     * @param discoveryTime in seconds service will wait for search requests
     */
    public void startDiscoveryService(int discoveryTime, List<Device> devices) {
        uPnPDiscoverable.setDevices(devices);
        uPnPDiscoverable.setStatus(true);
        discoverableServicesExecutor.submit(uPnPDiscoverable);
        discoverableServicesExecutor.schedule(() -> uPnPDiscoverable.setStatus(false), discoveryTime, TimeUnit.SECONDS);
    }

    public void stopDiscoveryService() {
        uPnPDiscoverable.setStatus(false);
    }
}
