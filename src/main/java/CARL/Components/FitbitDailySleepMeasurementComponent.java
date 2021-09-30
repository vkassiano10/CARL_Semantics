/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CARL.Components;

import static AuxiliaryPackage.AuxiliaryPackageConstants.BLANK_STRING;
import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;

/**
 *
 * @author apliatsios
 */
public class FitbitDailySleepMeasurementComponent {

    private int    id = INITIAL_UNKNOWN_VALUE;
    private String user_id = BLANK_STRING;

    private int light_minutes = INITIAL_UNKNOWN_VALUE;
    private int rem_minutes = INITIAL_UNKNOWN_VALUE;
    private int deep_minutes = INITIAL_UNKNOWN_VALUE;
    private int minutes_a_sleep = INITIAL_UNKNOWN_VALUE;
    private int wake_minutes = INITIAL_UNKNOWN_VALUE;
    private int asleep_minutes = INITIAL_UNKNOWN_VALUE;
    private int restless_minutes = INITIAL_UNKNOWN_VALUE;
    private int awake_minutes= INITIAL_UNKNOWN_VALUE;
    private float efficiency = INITIAL_UNKNOWN_VALUE;
    private int minutes_awake= INITIAL_UNKNOWN_VALUE;
    private int minutes_asleep= INITIAL_UNKNOWN_VALUE;
    private int wake_count= INITIAL_UNKNOWN_VALUE;
    private int light_count= INITIAL_UNKNOWN_VALUE;
    private int rem_count= INITIAL_UNKNOWN_VALUE;
    private int deep_count= INITIAL_UNKNOWN_VALUE;
    private String date = BLANK_STRING;

    public FitbitDailySleepMeasurementComponent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getLight_minutes() {
        return light_minutes;
    }

    public void setLight_minutes(int light_minutes) {
        this.light_minutes = light_minutes;
    }

    public int getRem_minutes() {
        return rem_minutes;
    }

    public void setRem_minutes(int rem_minutes) {
        this.rem_minutes = rem_minutes;
    }

    public int getDeep_minutes() {
        return deep_minutes;
    }

    public void setDeep_minutes(int deep_minutes) {
        this.deep_minutes = deep_minutes;
    }

    public int getMinutes_a_sleep() {
        return minutes_a_sleep;
    }

    public void setMinutes_a_sleep(int minutes_a_sleep) {
        this.minutes_a_sleep = minutes_a_sleep;
    }

    public int getWake_minutes() {
        return wake_minutes;
    }

    public void setWake_minutes(int wake_minutes) {
        this.wake_minutes = wake_minutes;
    }

    public int getAsleep_minutes() {
        return asleep_minutes;
    }

    public void setAsleep_minutes(int asleep_minutes) {
        this.asleep_minutes = asleep_minutes;
    }

    public int getRestless_minutes() {
        return restless_minutes;
    }

    public void setRestless_minutes(int restless_minutes) {
        this.restless_minutes = restless_minutes;
    }

    public int getAwake_minutes() {
        return awake_minutes;
    }

    public void setAwake_minutes(int awake_minutes) {
        this.awake_minutes = awake_minutes;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public int getMinutes_awake() {
        return minutes_awake;
    }

    public void setMinutes_awake(int minutes_awake) {
        this.minutes_awake = minutes_awake;
    }

    public int getMinutes_asleep() {
        return minutes_asleep;
    }

    public void setMinutes_asleep(int minutes_asleep) {
        this.minutes_asleep = minutes_asleep;
    }

    public int getWake_count() {
        return wake_count;
    }

    public void setWake_count(int wake_count) {
        this.wake_count = wake_count;
    }

    public int getLight_count() {
        return light_count;
    }

    public void setLight_count(int light_count) {
        this.light_count = light_count;
    }

    public int getRem_count() {
        return rem_count;
    }

    public void setRem_count(int rem_count) {
        this.rem_count = rem_count;
    }

    public int getDeep_count() {
        return deep_count;
    }

    public void setDeep_count(int deep_count) {
        this.deep_count = deep_count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SleepMeasurement{" +"user_id="+ user_id + "light_minutes=" + light_minutes + ", rem_minutes=" + rem_minutes + ", deep_minutes=" + deep_minutes + ", minutes_a_sleep=" + minutes_a_sleep + ", wake_minutes=" + wake_minutes + ", asleep_minutes=" + asleep_minutes + ", restless_minutes=" + restless_minutes + ", awake_minutes=" + awake_minutes + ", efficiency=" + efficiency + ", minutes_awake=" + minutes_awake + ", minutes_asleep=" + minutes_asleep + ", wake_count=" + wake_count + ", light_count=" + light_count + ", rem_count=" + rem_count + ", deep_count=" + deep_count + ", date=" + date + '}';
    }


}
