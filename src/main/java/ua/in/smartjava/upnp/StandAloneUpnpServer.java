package ua.in.smartjava.upnp;

import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import static ua.in.smartjava.utils.ResourceUtils.loadDataFromFile;

public class StandAloneUpnpServer {

    static String response = buildResponse();

    public static void main(String[] args) throws IOException {
        MulticastSocket recSocket = new MulticastSocket(null);
        recSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 1900));
        recSocket.setTimeToLive(10);
        recSocket.setSoTimeout(1000);
        recSocket.joinGroup(InetAddress.getByName("239.255.255.250"));
        boolean inService = true;
        while (inService) {
            byte[] buf = new byte[2048];
            DatagramPacket input = new DatagramPacket(buf, buf.length);
            try {
                recSocket.receive(input);
                String originaldata = new String(input.getData());

                System.out.println(originaldata);
                System.out.println(input.getAddress().toString());
                int port = input.getPort();
                if (originaldata.contains("M-SEARCH * HTTP/1.1")) {
                    System.out.println(response);
                    byte[] bytes = response.getBytes();
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
                            InetAddress.getByName("192.168.1.10"), port);
                    recSocket.send(packet);

                    System.out.println("send the response");
                }

            } catch (SocketTimeoutException e) {
            }
        }
        recSocket.disconnect();
        recSocket.close();
    }

    private static String buildResponse() {
        String responsePattern = loadDataFromFile("/response.data", "\r\n");
        String response = MessageFormatter.format(responsePattern, "192.169.1.6:1600", "someserial").getMessage();
        return response;
    }
}