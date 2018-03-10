package ua.in.smartjava.upnp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.domain.logrecord.LogService;
import ua.in.smartjava.snakeyaml.UPnP;

import static java.text.MessageFormat.format;
import static ua.in.smartjava.utils.ResourceUtils.loadDataFromFile;

@Slf4j
public class UPnPDiscoverable implements Runnable {

    private final String broadcastIp;
    private final String gatewayIp;
    private final int upnpPort;
    private final int ttl;
    private final int socketTimeout;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss");

    volatile private boolean inService;
    private final String deviceIp;
    private List<Device> devices;
    private final LogService logService;

    public UPnPDiscoverable(String deviceIp, UPnP uPnPConfig, LogService logService) {
        this.deviceIp = deviceIp;
        this.ttl = Integer.parseInt(uPnPConfig.getTtl());
        this.socketTimeout = Integer.parseInt(uPnPConfig.getTimeout());
        this.upnpPort = Integer.parseInt(uPnPConfig.getPort());
        this.gatewayIp = uPnPConfig.getGateway();
        this.broadcastIp = uPnPConfig.getBroadcastIp();
        this.inService = true;
        this.logService = logService;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void setStatus(boolean status) {
        this.inService = status;
        log.info("UPnP Discovery change state to: {}", status);
    }

    @Override
    public void run() {
        try {
            log.info("UPnP Discovery started...");
            MulticastSocket recSocket = new MulticastSocket(null);
            recSocket.bind(new InetSocketAddress(InetAddress.getByName(gatewayIp), upnpPort));
            recSocket.setTimeToLive(ttl);
            recSocket.setSoTimeout(socketTimeout);
            recSocket.joinGroup(InetAddress.getByName(broadcastIp));

            while (inService) {
                byte[] buf = new byte[2048];
                DatagramPacket inputPacket = new DatagramPacket(buf, buf.length);
                try {
                    recSocket.receive(inputPacket);
                    String requestData = new String(inputPacket.getData());

                    log.info(requestData);
                    log.info(inputPacket.getAddress().toString());
                    //TODO add filter check for Alexa IP address - isolate other search requests

                    int port = inputPacket.getPort();
                    if (requestData.contains("M-SEARCH * HTTP/1.1")) {
                        log.info("Received search request.");
                        logService.logAction(format("Received M-SEARCH * HTTP/1.1 from {0}",
                                inputPacket.getAddress().toString()));

                        for (Device device : devices) {
                            String response = buildResponse(device);
                            log.info(response);
                            byte[] bytes = response.getBytes();
                            DatagramPacket responsePacket = new DatagramPacket(bytes, bytes.length,
                                    InetAddress.getByName(deviceIp), port);
                            recSocket.send(responsePacket);
                        }
                        log.info("Response is sent with data about {} devices", devices.size());
                    }
                } catch (Exception e) {
                }
            }
            recSocket.disconnect();
            recSocket.close();
        } catch (Exception ex) {
        }
        log.info("UPnP Discovery stopped...");
    }

    private String buildResponse(Device device) {
        String responsePattern = loadDataFromFile("response.data", "\r\n");
        return format(responsePattern, device.getAddress(), device.getId(), getDateTime());
    }

    private String getDateTime() {
        String formatDateTime = LocalDateTime.now().format(formatter);
        return format("{0} GMT",formatDateTime);
    }
}
