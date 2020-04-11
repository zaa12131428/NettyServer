package Core.Untils;

public enum  DeviceType {
    ANDROID("安卓设备",10)
    ,IOTDEVICE("物联网设备",0);

    private String name = "";
    private int value = 0;


    private DeviceType(String name , int value) {
        this.value = value;
    }

    public String getName(int i){
        for(DeviceType type : DeviceType.values()){
            if(i == 10){
                return ANDROID.name;
            }
            if(i == 0){
                return IOTDEVICE.name;
            }
        }
        return null;
    }

}
