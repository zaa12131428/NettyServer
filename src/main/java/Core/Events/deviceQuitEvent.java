package Core.Events;

import Core.Device.Device;

public class deviceQuitEvent {
    private Device device;

    public deviceQuitEvent(Device device){
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }
}
