package CARL.Components;

import AuxiliaryPackage.AuxiliaryPackageEnumerations.WATTAGE_EVENTS_APPLIANCE;

import static AuxiliaryPackage.AuxiliaryPackageConstants.*;

public class FibaroEventComponent {

    private int    uniqueId    = INITIAL_UNKNOWN_VALUE;
    private String startTimestamp   = BLANK_STRING;
    private String endTimestamp   = BLANK_STRING;
    private int    roomId      = INITIAL_UNKNOWN_VALUE;
    private String roomName    = BLANK_STRING;
    private int    sectionId   = INITIAL_UNKNOWN_VALUE;
    private String sectionName = BLANK_STRING;
    private String deviceName  = BLANK_STRING;
    private int    deviceId    = INITIAL_UNKNOWN_VALUE;
    private float  wattageSum  = INITIAL_UNKNOWN_VALUE;
    private int    userId      = INITIAL_UNKNOWN_VALUE;
    private String eventName   = BLANK_STRING;
    private int    duration    = INITIAL_UNKNOWN_VALUE;

    public FibaroEventComponent() {
    }

    // TODO Should provided to the constructor all the other class variables as well.
    public FibaroEventComponent(WATTAGE_EVENTS_APPLIANCE appliance, int duration, float wattageSum, String startTimestamp, String endTimestamp, int userId, int deviceId, String roomName, int roomId) {
        setUniqueId(IPOSTIRIZW_FIBARO_EVENT_COMPONENT_COUNTER++);
        setDeviceName(appliance.toString());
        setDuration(duration);
        setWattageSum(wattageSum);
        setStartTimestamp(startTimestamp);
        setEndTimestamp(endTimestamp);
        setUserId(userId);
        setDeviceId(deviceId);
        setRoomName(roomName);
        setRoomId(roomId);
    }


    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public float getWattageSum() {
        return wattageSum;
    }

    public void setWattageSum(float wattageSum) {
        this.wattageSum = wattageSum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void status() {

        System.out.println("===============================\n");

        System.out.println("Room Id      : " + roomId);
        System.out.println("Room Name    : " + roomName);
//        System.out.println("Section Id   : " + sectionId);
//        System.out.println("Section Name : " + sectionName);
        System.out.println("Device Id    : " + deviceId);
        System.out.println("Device Name  : " + deviceName);
        System.out.println("Start Time    : " + startTimestamp);
        System.out.println("End Time    : " + endTimestamp);
        System.out.println("duration in secs    : " + duration);
        System.out.println("Wattage Sum : " + wattageSum);
        System.out.println("User         : " + userId);
        System.out.println("\n===============================\n");

    }
}
