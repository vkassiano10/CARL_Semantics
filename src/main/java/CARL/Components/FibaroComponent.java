package CARL.Components;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.IOException;

import static AuxiliaryPackage.AuxiliaryPackageConstants.*;

public class FibaroComponent {

    private int    uniqueId    = 0;
    private String timestamp   = BLANK_STRING;
    private int    id          = INITIAL_UNKNOWN_VALUE;
    private int    roomId      = INITIAL_UNKNOWN_VALUE;
    private String roomName    = BLANK_STRING;
    private int    sectionId   = INITIAL_UNKNOWN_VALUE;
    private String sectionName = BLANK_STRING;
    private String deviceName  = BLANK_STRING;
    private int    deviceId    = INITIAL_UNKNOWN_VALUE;
    private int    user        = INITIAL_UNKNOWN_VALUE;

    public FibaroComponent(Object object) throws IOException, ParseException {

        setUniqueId(IPOSTIRIZW_FIBARO_COMPONENT_COUNTER++);

        parseRetrievedJsonObject(object);
    }

    public void setUniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    public int getUniqueId() { return uniqueId; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getRoomId() { return roomId; }

    public void setRoomId(Integer roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }

    public void setRoomName(String roomName) { this.roomName = roomName; }

    public Integer getSectionId() { return sectionId; }

    public void setSectionId(Integer sectionId) { this.sectionId = sectionId; }

    public String getSectionName() { return sectionName; }

    public void setSectionName(String sectionName) { this.sectionName = sectionName;}

    public String getDeviceName() { return deviceName; }

    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public Integer getDeviceId() { return deviceId; }

    public void setDeviceId(Integer deviceId) { this.deviceId = deviceId; }

    public Integer getUser() { return user; }

    public void setUser(Integer user) { this.user = user; }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    void parseRetrievedJsonObject(Object object) throws IOException, ParseException {

        JSONObject jsonObject = (JSONObject) object;

        if (jsonObject.containsKey("id")) {
            setId(Integer.valueOf(jsonObject.get("id").toString()));
        }
        if (jsonObject.containsKey("room_id")) {
            setRoomId(Integer.valueOf(jsonObject.get("room_id").toString()));
        }
        if (jsonObject.containsKey("room_name")) {
            setRoomName(String.valueOf(jsonObject.get("room_name").toString()));
        }
        if (jsonObject.containsKey("section_id")) {
            setSectionId(Integer.valueOf(jsonObject.get("section_id").toString()));
        }
        if (jsonObject.containsKey("section_name")) {
            setSectionName(String.valueOf(jsonObject.get("section_name").toString()));
        }
        if (jsonObject.containsKey("device_name")) {
            setDeviceName(String.valueOf(jsonObject.get("device_name").toString()));
        }
        if (jsonObject.containsKey("device_id")) {
            setDeviceId(Integer.valueOf(jsonObject.get("device_id").toString()));
        }
        if (jsonObject.containsKey("timestamp")) {
            setTimestamp(String.valueOf(jsonObject.get("timestamp").toString()));
        }
        if (jsonObject.containsKey("user")) {
            setUser(Integer.valueOf(jsonObject.get("user").toString()));
        }

    }

    public void status() {

        System.out.println("===============================\n");

        System.out.println("Id           : " + id);
        System.out.println("Room Id      : " + roomId);
        System.out.println("Room Name    : " + roomName);
        System.out.println("Section Id   : " + sectionId);
        System.out.println("Section Name : " + sectionName);
        System.out.println("Device Id    : " + deviceId);
        System.out.println("Device Name  : " + deviceName);
        System.out.println("TimeStamp    : " + timestamp);
        System.out.println("User         : " + user);
        System.out.println("\n===============================\n");

    }
}


