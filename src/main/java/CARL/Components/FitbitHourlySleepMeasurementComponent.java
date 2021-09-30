package CARL.Components;

import CARL.entities.SleepSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static AuxiliaryPackage.AuxiliaryPackageConstants.*;
import static java.util.Objects.isNull;

public class FitbitHourlySleepMeasurementComponent {
    private int id = INITIAL_UNKNOWN_VALUE;
    private int user_id = INITIAL_UNKNOWN_VALUE;
    private String datetime = BLANK_STRING;
    private int duration = INITIAL_UNKNOWN_VALUE;
    private boolean ismainsleep = false;
    private String type = BLANK_STRING;
    private int efficiency = INITIAL_UNKNOWN_VALUE;
    private String startTime = BLANK_STRING;
    private String endTime = BLANK_STRING;
    private int minAfterWakeup = INITIAL_UNKNOWN_VALUE;
    private int minAsleep = INITIAL_UNKNOWN_VALUE;
    private int minAwake = INITIAL_UNKNOWN_VALUE;
    private int minToAsleep = INITIAL_UNKNOWN_VALUE;
    private int numberOfAsleepInNaps = INITIAL_UNKNOWN_VALUE;
    private String level = BLANK_STRING;
    private int second = INITIAL_UNKNOWN_VALUE;


    private List<SleepSet> sleepSetArrayList = new ArrayList<>();

    public FitbitHourlySleepMeasurementComponent(Object object) throws IOException, ParseException {

        setId(FITBIT_HOURLY_SLEEP_COMPONENT_COUNTER++);
        parseRetrievedJsonObject(object);
    }

    public FitbitHourlySleepMeasurementComponent(String datetime, int duration, boolean ismainsleep, String type, int efficiency, String startTime, String endTime, int minAfterWakeup, int minAsleep, int minAwake, int minToAsleep) {
        this.datetime = datetime;
        this.duration = duration;
        this.ismainsleep = ismainsleep;
        this.type = type;
        this.efficiency = efficiency;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minAfterWakeup = minAfterWakeup;
        this.minAsleep = minAsleep;
        this.minAwake = minAwake;
        this.minToAsleep = minToAsleep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() { return user_id; }

    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getDatetime() { return datetime;}

    public void setDatetime(String datetime) { this.datetime = datetime; }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    public boolean isIsmainsleep() { return ismainsleep; }

    public void setIsmainsleep(boolean ismainsleep) { this.ismainsleep = ismainsleep; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public int getEfficiency() { return efficiency; }

    public void setEfficiency(int efficiency) { this.efficiency = efficiency; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }

    public void setEndTime(String endTime) { this.endTime = endTime; }

    public int getMinAfterWakeup() { return minAfterWakeup; }

    public void setMinAfterWakeup(int minAfterWakeup) { this.minAfterWakeup = minAfterWakeup; }

    public int getMinAsleep() { return minAsleep; }

    public void setMinAsleep(int minAsleep) { this.minAsleep = minAsleep; }

    public int getMinAwake() { return minAwake; }

    public void setMinAwake(int minAwake) { this.minAwake = minAwake; }

    public int getMintoAsleep() { return minToAsleep; }

    public void setMintoAsleep(int mintoasleep) { this.minToAsleep = mintoasleep; }

    public List<SleepSet> getSleepSetArrayList() { return sleepSetArrayList; }

    public void setSleepSetArrayList(ArrayList<SleepSet> sleepSetArrayList) { this.sleepSetArrayList = sleepSetArrayList; }

    public void setNumberOfAsleepInNaps(int numberOfAsleepInNaps) { this.numberOfAsleepInNaps = numberOfAsleepInNaps; }

    public int getNumberOfAsleepInNaps() { return numberOfAsleepInNaps; }

    public int getMinToAsleep() {
        return minToAsleep;
    }

    public void setMinToAsleep(int minToAsleep) {
        this.minToAsleep = minToAsleep;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    void parseRetrievedJsonObject(Object object) throws IOException, ParseException {

        JSONObject jsonObject = (JSONObject) object;

        if (jsonObject.containsKey("user_id") && !isNull(jsonObject.get("user_id"))) {
            setUser_id(Integer.parseInt(jsonObject.get("user_id").toString()));
        }
        if (jsonObject.containsKey("duration") && !isNull(jsonObject.get("duration"))) {
            setDuration(Integer.parseInt(jsonObject.get("duration").toString()));
        }
        if (jsonObject.containsKey("date_of_sleep") && !isNull(jsonObject.get("date_of_sleep"))) {
            setDatetime(String.valueOf(jsonObject.get("date_of_sleep").toString()));
        }
        if (jsonObject.containsKey("efficiency\n") && !isNull(jsonObject.get("efficiency"))) {
            setEfficiency(Integer.parseInt(jsonObject.get("efficiency\n").toString()));
        }
        if (jsonObject.containsKey("start_time") && !isNull(jsonObject.get("start_time"))) {
            setStartTime(String.valueOf(jsonObject.get("start_time").toString()));
        }
        if (jsonObject.containsKey("end_time") && !isNull(jsonObject.get("end_time"))) {
            setEndTime(String.valueOf(jsonObject.get("end_time").toString()));
        }
        if (jsonObject.containsKey("is_main_sleep") && !isNull(jsonObject.get("is_main_sleep"))) {
            if (jsonObject.get("is_main_sleep").toString().equals("true"))
                setIsmainsleep(true);
            else
                setIsmainsleep(false);
        }
        if (jsonObject.containsKey("type") && !isNull(jsonObject.get("type"))) {
            setType(String.valueOf(jsonObject.get("type").toString()));
        }
        if (jsonObject.containsKey("minutes_after_wakeup") && !isNull(jsonObject.get("minutes_after_wakeup"))) {
            setMinAfterWakeup(Integer.parseInt(jsonObject.get("minutes_after_wakeup").toString()));
        }
        if (jsonObject.containsKey("minutes_asleep") && !isNull(jsonObject.get("minutes_asleep"))) {
            setMinAsleep(Integer.parseInt(jsonObject.get("minutes_asleep").toString()));
        }
        if (jsonObject.containsKey("minutes_awake") && !isNull(jsonObject.get("minutes_awake"))) {
            setMinAwake(Integer.parseInt(jsonObject.get("minutes_awake").toString()));
        }
        if (jsonObject.containsKey("minute_to_asleep") && !isNull(jsonObject.get("minute_to_asleep"))) {
            setMintoAsleep(Integer.parseInt(jsonObject.get("minute_to_asleep").toString()));
        }
        if (jsonObject.containsKey("sleep_set") && !isNull(jsonObject.get("sleep_set"))) {
            int sleepSetId = 0;
            JSONArray sleepSetArray = (JSONArray) jsonObject.get("sleep_set");
            for (Object ss : sleepSetArray) {

                SleepSet sleepSet = new SleepSet();

                sleepSet.setSleepSetId(sleepSetId++);

                JSONObject sleepSetJsonObject = (JSONObject) ss;
                if (sleepSetJsonObject.containsKey("date") && !isNull(jsonObject.get("date"))){
                    sleepSet.setDate(String.valueOf(sleepSetJsonObject.get("date").toString()));
                }
                if (sleepSetJsonObject.containsKey("level") && !isNull(jsonObject.get("level"))){
                    sleepSet.setLevel(String.valueOf(sleepSetJsonObject.get("level").toString()));
                }
                if (sleepSetJsonObject.containsKey("second") && !isNull(jsonObject.get("second"))){
                    sleepSet.setDurationInSecs(Integer.parseInt(sleepSetJsonObject.get("second").toString()));
                }

                sleepSetArrayList.add(sleepSet);
            }
        }

    }

    @Override
    public String toString() {
        return "HourlySleepMeasurement{" +"user_id=" + user_id + "datetime=" + datetime + ", duration=" + duration + ", efficiency=" + efficiency + ", type=" + type + ", ismainsleep="
                + ismainsleep + ", Starttime=" + startTime + ", Endtime=" + endTime + ", Min after wakeup=" + minAfterWakeup + ", Min Asleep=" + minAsleep + ", Mins awake=" + minAwake + ", Min to fall asleep=" + minToAsleep +'}' +"\n";

        }
}
