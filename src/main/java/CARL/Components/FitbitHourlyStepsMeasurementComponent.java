package CARL.Components;

import static AuxiliaryPackage.AuxiliaryPackageConstants.BLANK_STRING;
import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;

public class FitbitHourlyStepsMeasurementComponent {

    private int id= INITIAL_UNKNOWN_VALUE;
    private String user_id = BLANK_STRING;
    private String datetime= BLANK_STRING;
    private String rate = BLANK_STRING;

    public FitbitHourlyStepsMeasurementComponent() {
    }

    public FitbitHourlyStepsMeasurementComponent(String dateTime, String rate) {
        setDateTime(dateTime);
        setRate(rate);
    }
    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public String getUser_id() { return user_id; }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getDateTime() {
        return datetime;
    }

    public void setDateTime(String dateTime) {
        this.datetime = dateTime;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "HourlyStepsMeasurement{" + "user_id =" + user_id + "datetime=" + datetime + "rate=" + rate + '}';
    }

}
