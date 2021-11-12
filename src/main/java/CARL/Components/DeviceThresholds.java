package CARL.Components;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class DeviceThresholds {

    private int deviceId;
    private String deviceName;
    private int powerThreshold;
    private int userId;

    public DeviceThresholds(Object object) throws IOException, ParseException {
        parseRetrievedJsonObject(object);
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    void parseRetrievedJsonObject(Object object) throws IOException, ParseException {

        JSONObject jsonObject = (JSONObject) object;

        if (jsonObject.containsKey("id")) {
            setDeviceId(Integer.valueOf(jsonObject.get("id").toString()));
        }
        if (jsonObject.containsKey("name")) {
            setDeviceName(String.valueOf(jsonObject.get("name").toString()));
        }
        if (jsonObject.containsKey("value")) {
            setPowerThreshold(Integer.valueOf(jsonObject.get("value").toString()));
        }
        if (jsonObject.containsKey("user")) {
            setUserId(Integer.valueOf(jsonObject.get("user").toString()));
        }

    }

    public void status() {

        System.out.println("===============================\n");

        System.out.println("Device Name     : " + deviceName);
        System.out.println("Device Id    : " + deviceId);
        System.out.println("Power Threshold   : " + powerThreshold);
        System.out.println("User         : " + userId);
        System.out.println("\n===============================\n");

    }
}
