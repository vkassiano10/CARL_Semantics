package Exercise.SecondTask.Entities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static Exercise.Auxiliary.Constants.ACTIVITIES_NAME_STRING;
import static Exercise.Auxiliary.Constants.MODEL_NAME_STRING;
import static java.util.Objects.isNull;

/**
 * This class represents the person that contains all the activities.
 *
 * @author  Vasileiadis Angelos
 * @since   Q2_2021
 * @version Q2_2021
 */
public class Person {

    /**
     * This variable represents the activities that are described by the model.
     */
    private List<Activity> activities = new ArrayList<>();

    /**
     * This is the constructor of the class.
     *
     * @param object The model object based on which the model will be created, as an Object object.
     */
    public Person(Object object) throws IllegalAccessException {

        // Nullability sanity checking
        if (isNull(object))
            throw new IllegalAccessException("The input model is null.");

        // Cast the model key of the input object, as a JSONObject object.
        JSONObject modelJsonObject = (JSONObject) ((JSONObject) object).get(MODEL_NAME_STRING);

        // Cast the activities key of the model json object, as a JSONArray.
        JSONArray activitiesJsonOArray = (JSONArray) modelJsonObject.get(ACTIVITIES_NAME_STRING);

        // For each element of the activities Json Array, create a new Activity object, and add it to the activities
        // class variable.
        activitiesJsonOArray.forEach(i -> addToActivities(new Activity(i)));
    }

    /**
     * This is the getter of the activities class variable.
     *
     * @return The activities class variable, as a list of SecondExercise.SecondExercise.Activity objects.
     */
    public List<Activity> getActivities() { return activities; }

    /**
     * This is the setter of the activities class variable.
     *
     * @param activities The activities class variable, as a list of SecondExercise.SecondExercise.Activity objects.
     */
    public void setActivities(List<Activity> activities) { this.activities = activities;}

    /**
     * This method is used in order to safely insert an Activity object, into the class's actitivies class variable.
     *
     * @param activity The activity to insert, as an Activity object.
     */
    public void addToActivities(Activity activity) {

        // Sanity checking
        if (isNull(activity))
            return;

        activities.add(activity);
    }

    /**
     * This method is used in order to visualize the contents of the model, in a pretty, eye-friendly manner.
     */
    public void visualize() {

        getActivities().forEach(Activity::visualize);
    }
}
