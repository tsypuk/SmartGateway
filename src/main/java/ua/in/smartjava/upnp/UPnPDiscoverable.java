package ua.in.smartjava.upnp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UPnPDiscoverable implements Runnable {

    private static final int TTL = 10;
    private static final int SOCKET_TIMEOUT = 1000;

    volatile private boolean inService;
    private final String deviceIp;
    private List<String> deviceAddresses;

    public UPnPDiscoverable(String deviceIp) {
        this.deviceIp = deviceIp;
        this.inService = true;
    }

    public void setDeviceAddresses(List<String> deviceAddresses) {
        this.deviceAddresses = deviceAddresses;
    }

    public void stopDiscovery() {
        this.inService = false;
    }

    @Override
    public void run() {
        try {
            log.info("Starting UPnP.");
            MulticastSocket recSocket = new MulticastSocket(null);
            recSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 1900));
            recSocket.setTimeToLive(TTL);
            recSocket.setSoTimeout(SOCKET_TIMEOUT);
            recSocket.joinGroup(InetAddress.getByName("239.255.255.250"));

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
                        for (int i = 0; i < deviceAddresses.size(); i++) {
                            byte[] bytes = buildResponse(deviceAddresses.get(i), i).getBytes();
                            DatagramPacket responsePacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(deviceIp), port);
                            recSocket.send(responsePacket);
                        }
                        log.info("Response is sent to {} devices", deviceAddresses.size());
                    }
                } catch (Exception e) {
                }
            }
            recSocket.disconnect();
            recSocket.close();
        } catch (Exception ex) {
        }
        log.info("Exit from UPnP Discovery.");
    }

// TODO add message formater
// TODO externalize into resource file
    private String buildResponse(String deviceAddress, int id) {
        return
                "HTTP/1.1 200 OK\r\n" +
                        "CACHE-CONTROL: max-age=86400\r\n" +
                        "DATE: Fr, 26 Jan 2018 21:56:29 GMT\r\n" +
                        "EXT:\r\n" +
                        "LOCATION: http://" + deviceAddress + "/setup.xml\r\n" +
                        "OPT: \"http://schemas.upnp.org/upnp/1/0/\"; ns=01\r\n" +
                        "01-NLS: 7af96e40-1aa2-22c1-bcfc-eac9223a69cc\r\n" +
                        "SERVER: Unspecified, UPnP/1.0, Unspecified\r\n" +
                        "ST: urn:Belkin:device:**\r\n" +
                        "USN: uuid:Socket-1_0-38323636-4558-4dda-9188-cda0e66dfe6a-810" + id +
                        "::urn:Belkin:device:**\r\n" +
                        "X-User-Agent: redsonic\r\n\r\n";
    }
}
