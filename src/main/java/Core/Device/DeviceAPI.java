package Core.Device;

import Core.NettyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceAPI {

    public static int getOnlineAndroidDeviceNum(){
        return NettyServer.onlineAndroidDevices.size();
    }

    public static int getOnlineIotDeviceNum(){
        return NettyServer.onlineIotDevices.size();
    }

    public static int getOnlineDeviceNum(){
        return NettyServer.onlineAndroidDevices.size()+NettyServer.onlineIotDevices.size();
    }

    public static Device findTargetDevice(Channel ctx){
        if(NettyServer.onlineAndroidDevices.containsKey(ctx)){
            return NettyServer.onlineAndroidDevices.get(ctx);
        }
        if(NettyServer.onlineIotDevices.containsKey(ctx)){
            return NettyServer.onlineIotDevices.get(ctx);
        }
        return null;
    }

    public static Device findTargetDevice(String code) {
        for (Map.Entry<Channel, androidDevice> entry : NettyServer.onlineAndroidDevices.entrySet()) {
            if (entry.getValue().getCode().equalsIgnoreCase(code)) {
                return entry.getValue();
            }
        }

        for (Map.Entry<Channel, iotDevice> entry : NettyServer.onlineIotDevices.entrySet()) {
            if (entry.getValue().getCode().equalsIgnoreCase(code)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static List<Device> findTargetVersionAndroid(String version){
        List<Device> deviceList = new ArrayList<>();
        for (Map.Entry<Channel, androidDevice> entry : NettyServer.onlineAndroidDevices.entrySet()) {
            if(entry.getValue().getClientV().equalsIgnoreCase(version)){
                deviceList.add(entry.getValue());
            }
        }
        return deviceList;
    }

    public static List<Device> findTargetModeIot(String mode){
        List<Device> deviceList = new ArrayList<>();
        for (Map.Entry<Channel, iotDevice> entry : NettyServer.onlineIotDevices.entrySet()) {
            if(entry.getValue().getMode().equalsIgnoreCase(mode)){
                deviceList.add(entry.getValue());
            }
        }
        return deviceList;
    }
}
