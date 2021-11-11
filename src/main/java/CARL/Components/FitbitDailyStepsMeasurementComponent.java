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
 * @author vkassiano
 */
public class FitbitDailyStepsMeasurementComponent {

    private int id= INITIAL_UNKNOWN_VALUE;
    private int user_id= INITIAL_UNKNOWN_VALUE;
    private String datetime= BLANK_STRING;
    private double rate= INITIAL_UNKNOWN_VALUE;

    public FitbitDailyStepsMeasurementComponent() {
    }

    public FitbitDailyStepsMeasurementComponent(String datetime, int rate) {
        setDatetime(datetime);
        setRate(rate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getUser_id() { return user_id; }

    public void setUser_id(int user_id) { this.user_id = user_id; }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "DailyStepsMeasurement{" + "user_id =" + user_id + "datetime=" + datetime + "rate=" + rate + '}';
    }
    
}
