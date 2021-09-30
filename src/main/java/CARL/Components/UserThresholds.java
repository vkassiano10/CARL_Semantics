package CARL.Components;

import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;

public class UserThresholds {


    private int insomnia_TimeToFallAsleep = INITIAL_UNKNOWN_VALUE;
    private int restlessness_NumberOfInterruptionsInADay = INITIAL_UNKNOWN_VALUE;
    private int increasedNapping_asleepInNaps = INITIAL_UNKNOWN_VALUE;
    private int steps_Low = INITIAL_UNKNOWN_VALUE;
    private int steps_Medium = INITIAL_UNKNOWN_VALUE;
    private int fatBurnZone = INITIAL_UNKNOWN_VALUE;
    private int stressOrPainDuration = INITIAL_UNKNOWN_VALUE;
    private int inactivity_Duration = INITIAL_UNKNOWN_VALUE;
    private int lackOfExercise_Duration = INITIAL_UNKNOWN_VALUE;

    public UserThresholds(int insomnia_TimeToFallAsleep,
                          int restlessness_NumberOfInterruptionsInADay,
                          int increasedNapping_asleepInNaps,
                          int steps_Low,
                          int steps_Medium,
                          int fatBurnZone,
                          int stressOrPainDuration,
                          int inactivity_Duration,
                          int lackOfExercise_Duration) {
        this.insomnia_TimeToFallAsleep = insomnia_TimeToFallAsleep;
        this.restlessness_NumberOfInterruptionsInADay = restlessness_NumberOfInterruptionsInADay;
        this.increasedNapping_asleepInNaps = increasedNapping_asleepInNaps;
        this.steps_Low = steps_Low;
        this.steps_Medium = steps_Medium;
        this.fatBurnZone = fatBurnZone;
        this.stressOrPainDuration = stressOrPainDuration;
        this.inactivity_Duration = inactivity_Duration;
        this.lackOfExercise_Duration = lackOfExercise_Duration;
    }

    public UserThresholds() {

    }

    public int getInsomnia_TimeToFallAsleep() { return insomnia_TimeToFallAsleep; }

    public int getRestlessness_NumberOfInterruptionsInADay() { return restlessness_NumberOfInterruptionsInADay; }

    public int getIncreasedNapping_asleepInNaps() { return increasedNapping_asleepInNaps; }

    public int getSteps_Low() { return steps_Low; }

    public int getSteps_Medium() { return steps_Medium; }

    public int getFatBurnZone() { return fatBurnZone; }

    public int getStressOrPainDuration() { return stressOrPainDuration; }

    public int getInactivity_Duration() { return inactivity_Duration; }

    public int getLackOfExercise_Duration() { return lackOfExercise_Duration; }

    public void setInsomnia_TimeToFallAsleep(int insomnia_TimeToFallAsleep) { this.insomnia_TimeToFallAsleep = insomnia_TimeToFallAsleep; }

    public void setRestlessness_NumberOfInterruptionsInADay(int restlessness_NumberOfInterruptionsInADay) { this.restlessness_NumberOfInterruptionsInADay = restlessness_NumberOfInterruptionsInADay; }

    public void setIncreasedNapping_asleepInNaps(int increasedNapping_asleepInNaps) { this.increasedNapping_asleepInNaps = increasedNapping_asleepInNaps; }

    public void setSteps_Low(int steps_Low) { this.steps_Low = steps_Low; }

    public void setSteps_Medium(int steps_Medium) { this.steps_Medium = steps_Medium; }

    public void setFatBurnZone(int fatBurnZone) { this.fatBurnZone = fatBurnZone; }

    public void setStressOrPainDuration(int stressOrPainDuration) { this.stressOrPainDuration = stressOrPainDuration; }

    public void setInactivity_Duration(int inactivity_Duration) { this.inactivity_Duration = inactivity_Duration; }

    public void setLackOfExercise_Duration(int lackOfExercise_Duration) { this.lackOfExercise_Duration = lackOfExercise_Duration; }

}
