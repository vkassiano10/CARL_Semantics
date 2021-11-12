/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CARL.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import CARL.Components.*;
import kotlin.Pair;
import kotlin.Triple;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static AuxiliaryPackage.AuxiliaryPackageConstants.BLANK_STRING;
import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;
import static CARL.ComplexEventProcessing.*;

/**
 *
 * @author apliatsios, Vasileiadis Angelos, Kassiano Vasileios
 */
public class User {

    private String username = BLANK_STRING;
    private String token = BLANK_STRING;
    private int    id = INITIAL_UNKNOWN_VALUE;

    public List<FibaroComponent>                           userFibaroList           = new ArrayList<>();
    public List<FibaroWattageComponent>                    userFibaroWattageList    = new ArrayList<>();
    public List<DeviceThresholds>                    userDeviceThresholdsList    = new ArrayList<>();
    public List<FibaroEventComponent>                      fibaroEventComponents    = new ArrayList<>();

    public List<FitbitDailySleepMeasurementComponent>      userDailySleepArray      = new ArrayList<>();

    public List<FitbitHourlySleepMeasurementComponent>     userHourlySleepArray     = new ArrayList<>();
    public List<FitbitDailyStepsMeasurementComponent>      userDailyStepsArray      = new ArrayList<>();
    public List<FitbitHourlyStepsMeasurementComponent>     userHourlyStepsArray     = new ArrayList<>();
    public List<FitbitDailyHeartRateMeasurementComponent>  userDailyHeartRateArray  = new ArrayList<>();
    public List<FitbitHourlyHeartRateMeasurementComponent> userHourlyHeartRateArray = new ArrayList<>();
    public List<FitbitMinuteHeartRateMeasurementComponent> userMinuteHeartRateArray = new ArrayList<>();

    public Map<String, Pair<Integer, Integer>>             userHeartRateZones       = new HashMap<>();


    // These variables contain meta-calculated values that need to be stored in the graph db in order for the rules to run smoothly.

    /**
     * This variable holds the number of times the user was asleep in nap. The keyset holds the datetimes and the
     * valuesSet holds the respective number of asleep in naps.
     */
    public Map<String, Integer> numberOfAsleepInNapsMap = new TreeMap<>();

    /**
     * This variable holds the duration in seconds for each Heart Rate Zone, in serial order of appearance of the HR zones, for each day
     */
    public List<Triple<String, String, Integer>> zonesDurationList = new ArrayList<>();

    /**
     * This variable holds the number of times (count) that the user has passed by for each of the following states(asleep, wake, restless, )
     * for each day.
     */
    public Map<String, Triple> userSleepCountsMap = new HashMap<>();

    /**
     * This variable holds the information regarding whether the nap end time (in case it exists) is close to the Sleep start time (in case it exists),
     * for each day.
     */
    public Map<String, Boolean> napEndTimeCloseToSleepStartTime = new HashMap<>();

    /**
     * This is the void constructor of the class.
     */
    public User() { }

    public User(String username, String token, int id) {
        setUsername(username);
        setToken(token);
        setId(id);
        //fillHeartRateZones();
    }

    public List<FibaroWattageComponent> getUserFibaroWattageList() { return userFibaroWattageList; }

    public List<FitbitHourlySleepMeasurementComponent> getUserHourlySleepArray() { return userHourlySleepArray; }

    public List<FitbitHourlyStepsMeasurementComponent> getUserHourlyStepsArray() { return userHourlyStepsArray; }

    public List<FitbitHourlyHeartRateMeasurementComponent> getUserHourlyHeartRateArray() { return userHourlyHeartRateArray; }

    public int getId() { return id; }

    public List<FitbitDailySleepMeasurementComponent> getUserDailySleepArray() { return userDailySleepArray; }

    public List<FitbitDailyStepsMeasurementComponent> getUserDailyStepsArray() { return userDailyStepsArray; }

    public List<FitbitDailyHeartRateMeasurementComponent> getUserDailyHeartRateArray() { return userDailyHeartRateArray; }

    public List<FibaroComponent> getUserFibaroList() { return userFibaroList; }

    public void setUserFibaroList(List<FibaroComponent> userFibaroList) { this.userFibaroList = userFibaroList; }

    private UserThresholds userThresholds = new UserThresholds();

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public void setId(int id) { this.id = id; }

    public UserThresholds getUserThresholds() { return userThresholds; }

    public List<FibaroEventComponent> getFibaroEventComponents() { return fibaroEventComponents; }

    public List<FitbitMinuteHeartRateMeasurementComponent> getUserMinuteHeartRateArray() { return userMinuteHeartRateArray; }

    public Map<String, Pair<Integer, Integer>> getUserHeartRateZones() { return userHeartRateZones; }

    public List<Triple<String, String, Integer>> getZonesDurationList() { return zonesDurationList; }

    public Map<String, Integer> getNumberOfAsleepInNapsMap() { return numberOfAsleepInNapsMap; }

    public Map<String, Boolean> getNapEndTimeCloseToSleepStartTime() { return napEndTimeCloseToSleepStartTime; }

    public void populateFibaroComponentsList(String jsonStr) throws ParseException, IOException {

        org.json.simple.JSONObject responseJsonObject = (org.json.simple.JSONObject) new JSONParser().parse(jsonStr);

        if (responseJsonObject.containsKey("events")) {
            JSONArray responseJsonArray = (JSONArray) responseJsonObject.get("events");

            for (Object object : responseJsonArray) {

                FibaroComponent newFibaroComponent = new FibaroComponent(object);

                userFibaroList.add(newFibaroComponent);
            }
        }
    }

    public void populateFibaroWattageComponentsList(String jsonStr) throws ParseException, IOException {
        org.json.simple.JSONObject responseJsonObject = (org.json.simple.JSONObject) new JSONParser().parse(jsonStr);

        if (responseJsonObject.containsKey("wattage")) {
            JSONArray responseJsonArray = (JSONArray) responseJsonObject.get("wattage");

            for (Object object : responseJsonArray) {

                FibaroWattageComponent newFibaroWattageComponent = new FibaroWattageComponent(object);
//                System.out.println(object);
                System.out.println("Status: ");
                newFibaroWattageComponent.status();
                userFibaroWattageList.add(newFibaroWattageComponent);
            }
        }

        fibaroEventComponents = calculateWattageEvents(userFibaroWattageList);
        System.out.println("Number of new events: " + fibaroEventComponents.size());
        System.out.println("Events Info: ");
        for (FibaroEventComponent fbcmp: fibaroEventComponents
             ) {
            fbcmp.status();
        }
    }

    public void populateFibaroThresholdsList(String jsonStr) throws ParseException, IOException {
        org.json.simple.JSONObject responseJsonObject = (org.json.simple.JSONObject) new JSONParser().parse(jsonStr);

        if (responseJsonObject.containsKey("device")) {
            JSONArray responseJsonArray = (JSONArray) responseJsonObject.get("device");

            for (Object object : responseJsonArray) {

                DeviceThresholds newdt = new DeviceThresholds(object);
//                System.out.println(object);
                System.out.println("Status: ");
                newdt.status();
                userDeviceThresholdsList.add(newdt);
            }
        }

        fibaroEventComponents = calculateWattageEvents(userFibaroWattageList);
        System.out.println("Number of new events: " + fibaroEventComponents.size());
        System.out.println("Events Info: ");
        for (FibaroEventComponent fbcmp: fibaroEventComponents
        ) {
            fbcmp.status();
        }
    }


    public void populateSleep(String dailyJsonStr, String hourlyJsonStr) throws ParseException, IOException, java.text.ParseException {
        populateDailySleep(dailyJsonStr);
        populateHourlySleep(hourlyJsonStr);
    }

    public void populateDailySleep(String jsonStr) {

        JSONObject myResponse = new JSONObject(jsonStr);

        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<FitbitDailySleepMeasurementComponent>>() {
        }.getType();

        userDailySleepArray = gson.fromJson(myResponse.getJSONArray("Daily Sleep").toString(), userListType);
    }

    public void populateHourlySleep(String jsonStr) throws ParseException, IOException, java.text.ParseException {

        org.json.simple.JSONObject responseJsonObject = (org.json.simple.JSONObject) new JSONParser().parse(jsonStr);

        if (responseJsonObject.containsKey("Hourly Sleep")) {
            JSONArray responseJsonArray = (JSONArray) responseJsonObject.get("Hourly Sleep");

            for (Object object : responseJsonArray) {

                FitbitHourlySleepMeasurementComponent newFitbitHourlySleepComponent = new FitbitHourlySleepMeasurementComponent(object);

                userHourlySleepArray.add(newFitbitHourlySleepComponent);
            }
        }

        numberOfAsleepInNapsMap = calculateNumberOfAsleepInNaps(userHourlySleepArray);

        userSleepCountsMap = calculateSleepCounts(userHourlySleepArray);

        napEndTimeCloseToSleepStartTime = calculateNapEndTimeCloseToSleepStarTime(userHourlySleepArray);

    }


    public void populateSteps(String dailyJsonStr, String hourlyJsonStr) {
        populateDailySteps(dailyJsonStr);
        populateHourlySteps(hourlyJsonStr);
    }

    public void populateDailySteps(String jsonStr) {

        JSONObject myResponse = new JSONObject(jsonStr);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<FitbitDailyStepsMeasurementComponent>>() {}.getType();

        userDailyStepsArray = gson.fromJson(myResponse.getJSONArray("Daily Steps").toString(), userListType);

    }

    public  void populateHourlySteps(String jsonStr) {

        JSONObject myResponse = new JSONObject(jsonStr);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<FitbitHourlyStepsMeasurementComponent>>() {}.getType();

        userHourlyStepsArray = gson.fromJson(myResponse.getJSONArray("Hourly Steps").toString(), userListType);

    }

    public void populateHeartRate(String dailyJsonStr, String hourlyJsonStr, String minJsonStr) {
        populateDailyHeartRate(dailyJsonStr);
        populateHourlyHeartRate(hourlyJsonStr);
        populateMinuteHeartRate(minJsonStr);
    }

    public  void populateDailyHeartRate(String jsonStr) {

        JSONObject myResponse = new JSONObject(jsonStr);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<FitbitDailyHeartRateMeasurementComponent>>() {}.getType();
        try {
            userDailyHeartRateArray = gson.fromJson(myResponse.getJSONArray("Daily Heartrate").toString(), userListType);
        }catch (JSONException jsonException){
            System.out.println(jsonException.getMessage());
        }
    }

    public  void populateHourlyHeartRate(String jsonStr) {

        JSONObject myResponse = new JSONObject(jsonStr);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<FitbitHourlyHeartRateMeasurementComponent>>() {}.getType();
        try {
            userHourlyHeartRateArray = gson.fromJson(myResponse.getJSONArray("Hourly Heartrate").toString(), userListType);
        }catch (JSONException jsonException){
            System.out.println(jsonException.getMessage());
        }
    }

    public  void populateMinuteHeartRate(String jsonStr) {

        JSONObject myResponse = new JSONObject(jsonStr);
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<FitbitMinuteHeartRateMeasurementComponent>>() {}.getType();

        try {
            userMinuteHeartRateArray = gson.fromJson(myResponse.getJSONArray("Minute Heartrate").toString(), userListType);
        }catch (JSONException jsonException){
            System.out.println(jsonException.getMessage());
        }
        zonesDurationList = processMinuteHeartRate(userMinuteHeartRateArray, userHeartRateZones);
        //populateZonesToGraphDB();
    }

    public void status() {

        System.out.println("<username>" + getUsername());
        System.out.println("<token>" + getToken());

        userFibaroList.forEach(FibaroComponent::status);

        for (FitbitDailyStepsMeasurementComponent step : userDailyStepsArray)
            System.out.println(step);

        for (FitbitHourlyStepsMeasurementComponent step : userHourlyStepsArray)
            System.out.println(step);

        for (FitbitDailySleepMeasurementComponent sleep : userDailySleepArray)
            System.out.println(sleep);

        for (FitbitHourlySleepMeasurementComponent sleep : userHourlySleepArray)
            System.out.println(sleep);

        for (FitbitDailyHeartRateMeasurementComponent heartRate : userDailyHeartRateArray)
            System.out.println(heartRate);

        for (FitbitHourlyHeartRateMeasurementComponent heartRate : userHourlyHeartRateArray)
            System.out.println(heartRate);

        for (FitbitMinuteHeartRateMeasurementComponent heartRate : userMinuteHeartRateArray)
            System.out.println(heartRate);
    }

    public Map<String, Triple> getUserSleepCountsMap() {
        return userSleepCountsMap;
    }

    public List<DeviceThresholds> getUserDeviceThresholdsList() {
        return userDeviceThresholdsList;
    }

    public void setUserDeviceThresholdsList(List<DeviceThresholds> userDeviceThresholdsList) {
        this.userDeviceThresholdsList = userDeviceThresholdsList;
    }
}
