package ua.in.smartjava.upnp;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * UPnP discovery service
 */
public class DiscoveryService {

    private final ScheduledExecutorService discoverableServicesExecutor = Executors.newScheduledThreadPool(2);
    private final UPnPDiscoverable uPnPDiscoverable;

    public DiscoveryService(String ip) {
        uPnPDiscoverable = new UPnPDiscoverable(ip);
    }

    /**
     *
     * @param discoveryTime in seconds service will wait for search requests
     */
    public void startDiscoveryService(int discoveryTime, List<String> addresses) {
        uPnPDiscoverable.setDeviceAddresses(addresses);
        discoverableServicesExecutor.submit(uPnPDiscoverable);
        discoverableServicesExecutor.schedule(() -> uPnPDiscoverable.stopDiscovery(), discoveryTime, TimeUnit.SECONDS);
    }
}
