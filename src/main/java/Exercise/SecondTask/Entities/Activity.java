package Exercise.SecondTask.Entities;

import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;
import static Exercise.Auxiliary.Constants.*;
import static java.util.Objects.isNull;

/**
 * This class represents an activity.
 *
 * @author  Vasileiadis Angelos
 * @version Q2_2021
 * @since   Q2_2021
 */
public class Activity {

    /**
     * This variable represents the unique id of the activity.
     */
    private int id = INITIAL_UNKNOWN_VALUE;

    /**
     * This variable represents the start time of the activity, as a DateTime object.
     */
    private DateTime starTime = null;

    /**
     * This variable represents the stop time of the activity, as a DateTime object.
     */
    private DateTime stopTime = null;

    /**
     * This variable represents the description-content of an activity.
     */
    private String content = null;

    /**
     * This variable represents the observations of this activity, as a List of SecondExercise.SecondExercise.Observation objects.
     */
    private List<Observation> observations = new ArrayList<>();

    /**
     * This is the main constructor of the class.
     *
     * @param activity The object that represents the activity, as an Object object.
     */
    public Activity(Object activity) {

        // Create a unique id for the specific Activity object.
        setId(++ACTIVITY_ID);

        // Cast the input object to a JSONObject object.
        JSONObject activityJsonObject = (JSONObject) activity;

        // Set the class's variables through the corresponding setters.
        setStarTime(new DateTime(activityJsonObject.get(START_NAME_STRING)));
        setStopTime(new DateTime(activityJsonObject.get(END_NAME_STRING)));
        setContent((String) activityJsonObject.get(CONTENT_NAME_STRING));

        // Cast the observations contained in the activity json object to a JSONArray object.
        JSONArray observations = (JSONArray) activityJsonObject.get(OBSERVATIONS_NAME_STRING);

        // For each observation entry in the observations list.
        for (Object observationEntry : observations) {

            // Create a new observation object based on the observation entry.
            Observation observation = new Observation(observationEntry);

            // Add this observation to the observations class variable.
            addToObservations(observation);
        }
    }

    /**
     * This is the getter method of the id class variable.
     *
     * @return The id of the activity, as an integer number.
     */
    public int getId() { return id; }

    /**
     * This is the getter of the start time class variable.
     *
     * @return The start time of the activity, as a DateTime object.
     */
    public DateTime getStarTime() { return starTime; }

    /**
     * This is the getter of the stop time class variable.
     *
     * @return The stop time of the activity, as a DateTime object.
     */
    public DateTime getStopTime() { return stopTime; }

    /**
     * This is the getter of the content class variable.
     *
     * @return The content class variable, as a String value.
     */
    public String getContent() { return content; }

    /**
     * This is the getter of the observations class variable.
     *
     * @return The observations class variable, as a list of SecondExercise.SecondExercise.Observation objects.
     */
    public List<Observation> getObservations() { return observations; }

    /**
     * This is the setter method for the id class variable.
     *
     * @param id The id class variable, as an integer number.
     */
    public void setId(int id) { this.id = id; }

    /**
     * This is the setter of the start time variable.
     *
     * @param starTime The start time class variable, as a DateTime object.
     */
    public void setStarTime(DateTime starTime) { this.starTime = starTime; }

    /**
     * This is the setter of the stop time variable.
     *
     * @param stopTime The stop time class variable, as a DateTime object.
     */
    public void setStopTime(DateTime stopTime) {

        // Sanity checking
        if (stopTime.isBefore(starTime))
            throw new IllegalArgumentException("The stop time is before the start time.");

        this.stopTime = stopTime;
    }

    /**
     * This is the setter method of the content class variable.
     *
     * @param content The content class variable, as a String value.
     */
    public void setContent(String content) {

        if (isNull(content) || content.isEmpty())
            return;

        this.content = content;
    }

    /**
     * This is the setter of the observations class variable.
     *
     * @param observations The observations class variable, as a list of SecondExercise.SecondExercise.Observation objects.
     */
    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    /**
     * This is an auxiliary method to safely insert a observation object, into the observations class variable.
     *
     * @param observation The observation to insert, as an observation object.
     */
    public void addToObservations(Observation observation) {

        // Sanity checking
        if (isNull(observation))
            return;

        observations.add(observation);
    }

    /**
     * This method is used in order to visualize the contents of the activity, in a pretty, eye-friendly manner.
     */
    public void visualize() {

        System.out.println("=================================================================");
        System.out.println("Start Time of activity : " + getStarTime());
        System.out.println("Stop Time of activity  : " + getStopTime());
        System.out.println("Content of activity    : " + getContent());
        System.out.println("Observations           : \n");

        getObservations().forEach(Observation::visualize);
    }
}
