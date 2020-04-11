package Core.Events;

import Core.Device.Device;
import com.google.gson.JsonObject;

public class deviceInfoEvent {
    private Device device;
    private JsonObject packet;

    public deviceInfoEvent(Device device, JsonObject packet){
        this.device = device;
        this.packet = packet;
    }

    public Device getDevice() {
        return device;
    }

    public JsonObject getPacket() {
        return packet;
    }

}
