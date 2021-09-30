/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CARL.entities;
import java.util.Date;

/**
 *
 * @author apliatsios
 */
public class PatientQ {
    private Date Date;
    private String id;
    private int sleepHours;
    private int asleepHours;
    private String sleepQuality;
    private int activityHours;
    private int activityStepsHours;
    private int stressHours;
    private int stressDuration;
    public int TP;
    public int FP;
    

    public PatientQ() {
    }

    public PatientQ(Date Date, String id, int sleepHours, int asleepHours, String sleepQuality, int activityHours, int activityStepsHours, int stressHours, int stressDuration, int TP, int FP) {
        this.Date = Date;
        this.id = id;
        this.sleepHours = sleepHours;
        this.asleepHours = asleepHours;
        this.sleepQuality = sleepQuality;
        this.activityHours = activityHours;
        this.activityStepsHours = activityStepsHours;
        this.stressHours = stressHours;
        this.stressDuration = stressDuration;
        this.TP = TP;
        this.FP = FP;
    }


    public Date getDate() {
        return Date;
    }

    public String getId() {
        return id;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public int getAsleepHours() {
        return asleepHours;
    }

    public String getSleepQuality() {
        return sleepQuality;
    }

    public int getActivityHours() {
        return activityHours;
    }

    public int getActivityStepsHours() {
        return activityStepsHours;
    }

    public int getStressHours() {
        return stressHours;
    }

    public int getStressDuration() {
        return stressDuration;
    }

    public void setDate(Date Date) {
        this.Date = Date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSleepHours(int sleepHours) {
        this.sleepHours = sleepHours;
    }

    public void setAsleepHours(int asleepHours) {
        this.asleepHours = asleepHours;
    }

    public void setSleepQuality(String sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public void setActivityHours(int activityHours) {
        this.activityHours = activityHours;
    }

    public void setActivityStepsHours(int activityStepsHours) {
        this.activityStepsHours = activityStepsHours;
    }

    public void setStressHours(int stressHours) {
        this.stressHours = stressHours;
    }

    public void setStressDuration(int stressDuration) {
        this.stressDuration = stressDuration;
    }

    @Override
    public String toString() {
        return "PatientQ{" + "Date=" + Date + ", id=" + id + ", sleepHours=" + sleepHours + ", asleepHours=" + asleepHours + ", sleepQuality=" + sleepQuality + ", activityHours=" + activityHours + ", activityStepsHours=" + activityStepsHours + ", stressHours=" + stressHours + ", stressDuration=" + stressDuration + ", TP=" + TP + ", FP=" + FP +"Precision=" +precision()+ '}';
    }

    public int getTP() {
        return TP;
    }

    public int getFP() {
        return FP;
    }

    public double precision(){
    return TP/(TP+FP+1);
    }
    
}
