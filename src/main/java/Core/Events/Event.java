package Core.Events;

public interface Event {
    void deviceList(deviceListEvent event);
    void deviceJoin(deviceJoinEvent event);
    void deviceQuit(deviceQuitEvent event);
    void deviceInfo(deviceInfoEvent event);
    void deviceCommand(deviceCommandEvent event);
}
