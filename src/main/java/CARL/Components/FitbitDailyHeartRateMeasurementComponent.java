package CARL.Components;

import static AuxiliaryPackage.AuxiliaryPackageConstants.BLANK_STRING;
import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;

public class FitbitDailyHeartRateMeasurementComponent {


    private int   id  = INITIAL_UNKNOWN_VALUE;
    private String user_id = BLANK_STRING;
    private String datetime = BLANK_STRING;
    private String resting_hr = BLANK_STRING;

    public FitbitDailyHeartRateMeasurementComponent() {
    }

    public FitbitDailyHeartRateMeasurementComponent(String dateTime, String resting_hr) {
        this.datetime = dateTime;
        this.resting_hr = resting_hr;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUser_id() { return user_id; }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getDateTime() {
        return datetime;
    }

    public void setDateTime(String dateTime) {
        this.datetime = dateTime;
    }

    public String getResting_hr() {
        return resting_hr;
    }

    public void setResting_hr(String rate) {
        this.resting_hr = resting_hr;
    }

    @Override
    public String toString() {
        return "DailyHeartRateMeasurement{" +"user_id=" + user_id + "datetime=" + datetime + ", RestingHeartRate=" + resting_hr + '}';
    }

}
