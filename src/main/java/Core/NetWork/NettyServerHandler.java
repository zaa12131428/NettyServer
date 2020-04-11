package Core.NetWork;

import Core.Events.*;
import Core.NettyServer;
import Core.Device.DeviceAPI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(((String)msg).contains("HeatBeat")) {
            ctx.channel().flush();
            return;
        }
        String clientMsg = ((String)msg).replace('\n',' ').replace('\r',' ').replace('\0',' ');
        new Thread(() -> {
            try {
                JsonObject clientMessage = null;
                try {
                    System.out.println((clientMsg));
                    clientMessage = JsonParser.parseString(clientMsg).getAsJsonObject();
                }catch (Exception e){
                    ctx.channel().flush();
                    e.printStackTrace();
                }

                if(clientMessage == null){
                    ctx.channel().flush();
                    return;
                }

                if (clientMessage.getAsJsonObject("Command") != null) {
                    JsonObject command = clientMessage.getAsJsonObject("Command");
                    NettyServer.eventBus.deviceCommand(new deviceCommandEvent(DeviceAPI.findTargetDevice(ctx.channel()), command));
                }

                if (clientMessage.getAsJsonObject("Info") != null) {
                    JsonObject Info = clientMessage.getAsJsonObject("Info");
                    NettyServer.eventBus.deviceInfo(new deviceInfoEvent(DeviceAPI.findTargetDevice(ctx.channel()), Info));
                }

                if(clientMessage.getAsJsonObject("getDeviceList") != null) {
                    JsonObject deviceList = clientMessage.getAsJsonObject("getDeviceList");
                    NettyServer.eventBus.deviceList(new deviceListEvent(DeviceAPI.findTargetDevice(ctx.channel()),deviceList));

                }
                ctx.channel().flush();
            } catch (Exception e) {
                ctx.channel().flush();
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().flush();
        ctx.close();
    }
}
