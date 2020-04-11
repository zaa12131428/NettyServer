package Core.NetWork;

import Core.Device.Device;
import Core.Device.DeviceAPI;
import Core.Events.deviceJoinEvent;
import Core.Events.deviceQuitEvent;
import Core.NettyServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


public class IdleStateTrigger extends ChannelInboundHandlerAdapter {
    String realMsg = "";

    @Override
    public void channelRead(ChannelHandlerContext ctx , Object msg){
        new Thread(()->{
            if (realMsg.contains("HeatBeat")){
                realMsg = "";
                ctx.channel().flush();
                return;
            }

            if(!realMsg.contains("*")) {
                realMsg += msg;
            }

            String clientMsg = realMsg.replace('*','\n');
            realMsg = "";
            JsonObject clientMessage = null;
            try{
                clientMessage = JsonParser.parseString(clientMsg).getAsJsonObject();
            }catch (Exception e){
                ctx.channel().flush();
            }
            if (clientMessage !=null && clientMessage.getAsJsonObject("Register") != null) {
                JsonObject registerInfo = clientMessage.getAsJsonObject("Register");
                String deviceCode = registerInfo.getAsJsonPrimitive("code").getAsString();
                Device device = DeviceAPI.findTargetDevice(deviceCode);
                if(device != null){
                    device.close();
                    NettyServer.eventBus.deviceJoin(new deviceJoinEvent(registerInfo, ctx.channel()));
                }else {
                    NettyServer.eventBus.deviceJoin(new deviceJoinEvent(registerInfo, ctx.channel()));
                }
                ctx.channel().flush();
            }else{
                ctx.fireChannelRead(clientMsg);
                ctx.channel().flush();
            }
        }).start();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {

            new Thread(()->{
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                Device device = DeviceAPI.findTargetDevice(ctx.channel());
                if(device != null) {
                    if(device.getChannel() != null){
                        NettyServer.eventBus.deviceQuit(new deviceQuitEvent(device));
                    }
                }
            }
        }).start();
        } else {
            super.userEventTriggered(ctx, evt);
        }
        ctx.channel().flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }
}
