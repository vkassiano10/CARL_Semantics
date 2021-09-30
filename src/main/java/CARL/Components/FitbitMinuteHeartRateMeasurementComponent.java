package CARL.Components;

import static AuxiliaryPackage.AuxiliaryPackageConstants.BLANK_STRING;
import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;

public class FitbitMinuteHeartRateMeasurementComponent {

    private int id = INITIAL_UNKNOWN_VALUE;
    private String user_id = BLANK_STRING;
    private String datetime = BLANK_STRING;
    private String resting_hr = BLANK_STRING;

    public FitbitMinuteHeartRateMeasurementComponent() {
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getResting_hr() {
        return resting_hr;
    }

    public void setResting_hr(String resting_hr) {
        this.resting_hr = resting_hr;
    }

    @Override
    public String toString() {
        return "MinuteHeartRateMeasurement{" +"user_id=" + user_id + "datetime=" + datetime + ", RestingHeartRate=" + resting_hr + '}';
    }
}
