package Core.Events;


import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class deviceJoinEvent {
    private JsonObject packet;
    private Channel channel;

    public deviceJoinEvent(JsonObject packet, Channel channel){
        this.packet = packet;
        this.channel = channel;
    }

    public JsonObject getPacket() {
        return packet;
    }

    public Channel getChannel() {
        return channel;
    }
}
