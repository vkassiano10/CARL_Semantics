package CARL.entities;

public class SleepSet {
    private int sleepSetId;
    private String date;
    private String level;
    private int durationInSecs;

    public SleepSet() {
    }

    public SleepSet(int sleepSetId, String date, String level, int durationInSecs) {
        this.sleepSetId = sleepSetId;
        this.date = date;
        this.level = level;
        this.durationInSecs = durationInSecs;
    }

    public int getSleepSetId() {
        return sleepSetId;
    }

    public void setSleepSetId(int sleepSetId) {
        this.sleepSetId = sleepSetId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getDurationInSecs() {
        return durationInSecs;
    }

    public void setDurationInSecs(int durationInSecs) {
        this.durationInSecs = durationInSecs;
    }

    @Override
    public String toString() {
        return "HourlySleepMeasurement{" + "SleepSet id=" + sleepSetId + "datetime=" + date + ", duration=" + durationInSecs + ", level=" + level + '}';
    }
}
