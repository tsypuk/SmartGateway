package ua.in.smartjava.upnp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.snakeyaml.UPnPConfig;

@Slf4j
public class UPnPDiscoverable implements Runnable {

    private final String broadcastIp;
    private final String gatewayIp;
    private final int upnpPort;
    private final int ttl;
    private final int socketTimeout;

    volatile private boolean inService;
    private final String deviceIp;
    private List<Device> devices;

    public UPnPDiscoverable(String deviceIp, UPnPConfig uPnPConfig) {
        this.deviceIp = deviceIp;
        this.ttl = Integer.parseInt(uPnPConfig.getTtl());
        this.socketTimeout = Integer.parseInt(uPnPConfig.getTimeout());
        this.upnpPort = Integer.parseInt(uPnPConfig.getPort());
        this.gatewayIp = uPnPConfig.getGateway();
        this.broadcastIp = uPnPConfig.getBroadcastIp();
        this.inService = true;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void setStatus(boolean status) {
        this.inService = status;
        log.info("UPnP Discovery stopping...");
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
                    System.out.println(inputPacket.getAddress().toString());
                    //TODO add filter check for Alexa IP address - isolate other search requests

                    int port = inputPacket.getPort();
                    if (requestData.contains("M-SEARCH * HTTP/1.1")) {
                        for (Device device : devices){
                            byte[] bytes = buildResponse(device).getBytes();
                            DatagramPacket responsePacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(deviceIp), port);
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

// TODO add message formater
// TODO externalize into resource file
    private String buildResponse(Device device) {
        return
                "HTTP/1.1 200 OK\r\n" +
                        "CACHE-CONTROL: max-age=86400\r\n" +
                        "DATE: Fr, 26 Jan 2018 21:56:29 GMT\r\n" +
                        "EXT:\r\n" +
                        "LOCATION: http://" + device.getAddress() + "/setup.xml\r\n" +
                        "OPT: \"http://schemas.upnp.org/upnp/1/0/\"; ns=01\r\n" +
                        "01-NLS: 7af96e40-1aa2-22c1-bcfc-eac9223a69cc\r\n" +
                        "SERVER: Unspecified, UPnP/1.0, Unspecified\r\n" +
                        "ST: urn:Belkin:device:**\r\n" +
                        "USN: uuid:Socket-1_0-" + device.getId() +
                        "::urn:Belkin:device:**\r\n" +
                        "X-User-Agent: redsonic\r\n\r\n";
    }
}
