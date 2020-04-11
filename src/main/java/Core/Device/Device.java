package Core.Device;


import io.netty.channel.Channel;

public interface Device {
    String getToken();
    String getId();
    String getCode();

    void close();
    Channel getChannel();
    void sendMessage(String msg);
}
