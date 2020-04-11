package Core.Events;

import Core.Device.Device;
import Core.Device.DeviceAPI;
import Core.Device.androidDevice;
import Core.Device.iotDevice;
import Core.NettyServer;
import Core.Untils.DeviceType;
import Core.Untils.Tools;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.channel.Channel;

import java.util.Map;

public class EventHandlers implements Event {

    @Override
    public void deviceJoin(deviceJoinEvent event) {
        Channel ctx = event.getChannel();
        if (!NettyServer.onlineIotDevices.containsKey(ctx) || !NettyServer.onlineAndroidDevices.containsKey(ctx)) {
            JsonObject packet   = event.getPacket();
            DeviceType type = packet.getAsJsonPrimitive("id").getAsInt() == 10 ? DeviceType.ANDROID : DeviceType.IOTDEVICE;
            switch (type) {
                case ANDROID:
                    String android_token = Tools.calcToken(ctx);
                    String android_id = packet.getAsJsonPrimitive("id").getAsString();
                    String android_code = packet.getAsJsonPrimitive("code").getAsString();
                    String android_clientV = packet.getAsJsonPrimitive("clientV").getAsString();
                    androidDevice android_Device = new androidDevice(android_token,android_id, android_code, android_clientV, ctx);
                    android_Device.sendMessage("{\"token\":\""+android_token+"\"}\r\n");
                    NettyServer.onlineAndroidDevices.put(ctx, android_Device);
                    Tools.messageFomat("一台安卓设备接入了系统!");
                    Tools.messageFomat("设备代码: "+android_code);
                    Tools.messageFomat("客户端版本: "+android_clientV);
                    Tools.messageFomat("IP地址: "+ctx.remoteAddress());
                    break;
                case IOTDEVICE:
                    String iot_token = Tools.calcToken(ctx);
                    String iot_id = packet.getAsJsonPrimitive("id").getAsString();
                    String iot_code = packet.getAsJsonPrimitive("code").getAsString();
                    String iot_clientV = packet.getAsJsonPrimitive("clientV").getAsString();
                    String iot_deviceMode = packet.getAsJsonPrimitive("mode").getAsString();
                    iotDevice iot_Device = new iotDevice(iot_token,iot_id, iot_code, iot_clientV, iot_deviceMode, ctx);
                    iot_Device.sendMessage("{\"token\":\""+iot_token+"\"}\r\n");
                    NettyServer.onlineIotDevices.put(ctx, iot_Device);
                    Tools.messageFomat("一台物联网设备接入了系统!");
                    Tools.messageFomat("设备代码: "+iot_code);
                    Tools.messageFomat("芯片型号: "+iot_deviceMode);
                    Tools.messageFomat("IP地址: "+ctx.remoteAddress());
                    break;
            }
        }
    }

    @Override
    public void deviceQuit(deviceQuitEvent event){
        Device device = event.getDevice();
        Tools.messageFomat("设备编码: "+device.getCode()+" 断开了连接!");
        device.close();
    }

    @Override
    public void deviceInfo(deviceInfoEvent event) {
        Device device = event.getDevice();
        JsonObject packet = event.getPacket();
        Channel ctx = device.getChannel();
        if(packet.getAsJsonPrimitive("token") != null){
            if(Tools.verifyToken(packet.getAsJsonPrimitive("token").getAsString(),ctx)){
                if(packet.getAsJsonPrimitive("targetCode") != null){

                    String targetCode = packet.getAsJsonPrimitive("targetCode").getAsString();
                    JsonObject msg = packet.getAsJsonObject("Msg");
                    Device targetDevice = DeviceAPI.findTargetDevice(targetCode);
                    if(targetDevice != null) {
                        targetDevice.sendMessage(msg.toString() + "\r\n");
                        Tools.messageFomat("设备编码: " + device.getCode() + " [Info] " + msg.toString());
                    }else{
                        device.sendMessage("{\"State\": \"ERROR_DEVICE_NOT_ONLIE\"}\r\n");
                    }

                }
            }else{
                Tools.messageFomat("设备编码: " +device.getCode() +"Token 异常!");
                ctx.close();
            }
        }
    }

    @Override
    public void deviceCommand(deviceCommandEvent event) {

    }

    @Override
    public void deviceList(deviceListEvent event){
        Device device = event.getDevice();
        JsonObject packet = event.getPacket();
        Channel ctx = device.getChannel();
        if(packet.getAsJsonPrimitive("token") != null) {
            if(Tools.verifyToken(packet.getAsJsonPrimitive("token").getAsString(),ctx)) {
                JsonArray array = new JsonArray();
                for (Map.Entry<Channel, iotDevice> entry : NettyServer.onlineIotDevices.entrySet()) {
                    array.add(entry.getValue().getCode());
                }
                Gson gson = new Gson();
                String json = gson.toJson(array);
                String result = "{\"State:\":\"SUCCESS\",\"DeviceList\":"+json+"}";
                device.sendMessage(result);
                System.out.println(result);
            }
        }
    }
}
