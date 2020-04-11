package Core.Events;

import Core.Device.Device;
import Core.Untils.DeviceType;
import com.google.gson.JsonObject;

public class deviceBroadCastEvent {
    private Device device;
    private JsonObject packet;
    private DeviceType broadCastRegion;

    public deviceBroadCastEvent(Device device, JsonObject packet, DeviceType broadCastRegion){
        this.device = device;
        this.packet = packet;
        this.broadCastRegion = broadCastRegion;
    }

    public Device getDevice() { return device; }
    public JsonObject getPacket() { return packet; }
    public DeviceType getBroadCastRegion() { return broadCastRegion; }
}
