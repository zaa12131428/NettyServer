package Core.NetWork;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class BroadCastAddress {
    private String serverAddress = "";
    private DatagramSocket socket;
    public BroadCastAddress(){
        try {
            socket = new DatagramSocket(6666);
        }catch (IOException e){
            e.printStackTrace();
        }
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (serverAddress.isEmpty()) {
                        Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
                        NetworkInterface networkInterface;
                        InetAddress ipAddress;
                        while (networkInterfaceEnumeration.hasMoreElements()) {
                            networkInterface = networkInterfaceEnumeration.nextElement();
                            if (networkInterface.getDisplayName().contains("wlan0")) {
                                Enumeration address = networkInterface.getInetAddresses();
                                while (address.hasMoreElements()) {
                                    ipAddress = (InetAddress) address.nextElement();
                                    if (ipAddress instanceof Inet4Address) {
                                        serverAddress = ipAddress.getHostAddress();
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    String msg = "NETTYSERVERADDRESS@" + serverAddress;
                    byte b[] = msg.getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(b,b.length);
                    socket.connect(InetAddress.getByName("255.255.255.255"), 6666);
                    socket.setBroadcast(true);

                    socket.send(datagramPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
