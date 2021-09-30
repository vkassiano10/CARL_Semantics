package Exercise.SecondTask.Entities;

import org.joda.time.DateTime;
import org.json.simple.JSONObject;

import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;
import static Exercise.Auxiliary.Constants.*;
import static java.util.Objects.isNull;

/**
 * This class represents an observation.
 *
 * @author  Vasileiadis Angelos
 * @version Q2_2021
 * @since   Q2_2021
 */
public class Observation {

    /**
     * This variable represents a unique id for this observation.
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
     * This is the constructor of the class.
     *
     * @param observation The observation, as an Object object.
     */
    public Observation(Object observation) {

        // Create a unique id for the specific Observation object.
        setId(++OBSERVATION_ID);

        // Cast the input observation object to a JSONObject object.
        JSONObject observationJsonObject = (JSONObject) observation;

        // Set the class's variables through the corresponding setters.
        setStarTime(new DateTime(observationJsonObject.get(START_NAME_STRING)));
        setStopTime(new DateTime(observationJsonObject.get(END_NAME_STRING)));
        setContent((String) observationJsonObject.get(CONTENT_NAME_STRING));
    }

    /**
     * This is the getter of the id class variable.
     *
     * @return the id of the observation, as an integer number.
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
     * This is the setter of the id class variable.
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

        // Sanity checking
        if (isNull(content) || content.isEmpty())
            return;

        this.content = content;
    }

    /**
     * This method is used in order to visualize the contents of the observation, in a pretty, eye-friendly manner.
     */
    public void visualize() {

        System.out.println("========");
        System.out.println("Start Time of observation : " + getStarTime());
        System.out.println("Stop Time of observation  : " + getStopTime());
        System.out.println("Content of observation    : " + getContent());
        System.out.println("========");
        System.out.println();

    }
}
