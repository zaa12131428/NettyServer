package Core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimeZone;

import Core.Device.androidDevice;
import Core.Device.iotDevice;
import Core.Events.Event;
import Core.Events.EventHandlers;
import Core.NetWork.BroadCastAddress;
import Core.NetWork.NettyServerStart;
import io.netty.channel.Channel;

public class NettyServer {

    public static File logFile;
    public static Event eventBus;
    public static HashMap<Channel, iotDevice> onlineIotDevices = new HashMap<>();
    public static HashMap< Channel , androidDevice> onlineAndroidDevices = new HashMap<>();


    public static void main(String[] args) {
        eventBus = new EventHandlers();
        new BroadCastAddress();
        TimeZone time = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(time);
        logFile = new File("./1.log");

        if(!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int port = 8088;

        try {
            new NettyServerStart(port).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
