package Exercise.Auxiliary;

import static AuxiliaryPackage.AuxiliaryPackageConstants.INITIAL_UNKNOWN_VALUE;

/**
 * This class contains all the constant variables that are used throughout the Semantics Exercise.
 *
 * @author  Vasileiadis Angelos
 * @since   Q2_2021
 * @version Q2_2021
 */
public class Constants {

    /**
     * This variable represents the Json File Name that points to the essential json file containing the data.
     */
    public static String JSON_FILE_NAME = "src\\main\\resources\\exercise\\example_observations.json";

    /**
     * This variable represent the model name as a string. It is used through json parsing procedure, and acts in order
     * to vanish magic words from our code.
     */
    public static String MODEL_NAME_STRING = "model";

    /**
     * This variable represent the activities name as a string. It is used through json parsing procedure, and acts in order
     * to vanish magic words from our code.
     */
    public static String ACTIVITIES_NAME_STRING = "activities";

    /**
     * This variable represent the observations name as a string. It is used through json parsing procedure, and acts in order
     * to vanish magic words from our code.
     */
    public static String OBSERVATIONS_NAME_STRING = "observations";

    /**
     * This variable represent the start name as a string. It is used through json parsing procedure, and acts in order
     * to vanish magic words from our code.
     */
    public static String START_NAME_STRING = "start";

    /**
     * This variable represent the end name as a string. It is used through json parsing procedure, and acts in order
     * to vanish magic words from our code.
     */
    public static String END_NAME_STRING = "end";

    /**
     * This variable represent the content name as a string. It is used through json parsing procedure, and acts in order
     * to vanish magic words from our code.
     */
    public static String CONTENT_NAME_STRING = "content";


    /**
     * This variable represents the activity id of the activity objects, in order to distinguish them.
     */
    public static int ACTIVITY_ID = INITIAL_UNKNOWN_VALUE;

    /**
     * This variable represents the observation id of the activity objects, in order to distinguish them.
     */
    public static int OBSERVATION_ID = INITIAL_UNKNOWN_VALUE;

    /**
     * This variable represents the name of the namespace that is used throughout the exercise.
     */
    public static String NAMESPACE_STRING = "https://www.semanticsExercise.com#";
}
