package ua.in.smartjava.upnp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class StandAloneUpnpServer {
    private static final String response =
            "HTTP/1.1 200 OK\r\n" +
                    "CACHE-CONTROL: max-age=86400\r\n" +
                    "DATE: Fr, 26 Jan 2018 21:56:29 GMT\r\n" +
                    "EXT:\r\n" +
                    "LOCATION: http://192.168.1.6:1900/setup.xml\r\n" +
                    "OPT: \"http://schemas.upnp.org/upnp/1/0/\"; ns=01\r\n" +
                    "01-NLS: 7af96e40-1aa2-22c1-bcfc-eac9223a69cc\r\n" +
                    "SERVER: Unspecified, UPnP/1.0, Unspecified\r\n" +
                    "ST: urn:Belkin:device:**\r\n" +
                    "USN: uuid:Socket-1_0-38323636-4558-4dda-9188-cda0e66dfe6a-81::urn:Belkin:device:**\r\n" +
                    "X-User-Agent: redsonic\r\n\r\n";

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
                    byte[] bytes = response.getBytes();
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
                            InetAddress.getByName("192.168.1.10"), port);
                    recSocket.send(packet);

                    System.out.println("send the response");
                }

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
        }
        recSocket.disconnect();
        recSocket.close();
    }
}