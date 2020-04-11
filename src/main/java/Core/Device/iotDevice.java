package Core.Device;

import Core.NettyServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class iotDevice implements Device{
    private String id;
    private String Mode;
    private String code;
    private String token;
    private int msgCount;
    private Device device;
    private String clientV;

    private Channel channel;

    public iotDevice(
            String token,
            String id,
            String code,
            String clientV,
            String Mode,
            Channel ctx
    ){

        this.token = token;
        this.id = id;
        this.code = code;
        this.device = this;
        this.clientV = clientV;
        this.Mode = Mode;
        this.channel = ctx;
    }

    public String getMode() {
        return Mode;
    }

    public String getClientV() {
        return clientV;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String toString(){
        return "id:"+this.id+"code:"+this.code+"clientV:"+clientV;
    }

    @Override
    public synchronized void sendMessage(String msg) {
            msgCount++;
            String readMsg = msg + "\r\n";
            ByteBuf keepAlive_ByteBuf = Unpooled.buffer(readMsg.length());
            keepAlive_ByteBuf.writeBytes(readMsg.getBytes());
            channel.write(keepAlive_ByteBuf);
            channel.flush();

    }

    @Override
    public void close(){
        if(NettyServer.onlineIotDevices.containsKey(channel)){
            NettyServer.onlineIotDevices.remove(channel);
        }

        if(NettyServer.onlineAndroidDevices.containsKey(channel)){
            NettyServer.onlineAndroidDevices.remove(channel);
        }
        channel.close();
    }
}
