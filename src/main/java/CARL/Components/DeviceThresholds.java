package CARL.Components;

public class DeviceThresholds {

    private int deviceId;
    private String deviceName;
    private int powerThreshold;

    public DeviceThresholds() {

    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getPowerThreshold() {
        return powerThreshold;
    }

    public void setPowerThreshold(int powerThreshold) {
        this.powerThreshold = powerThreshold;
    }
}
