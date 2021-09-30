package AuxiliaryPackage;

import java.text.SimpleDateFormat;

public class AuxiliaryPackageConstants {
    /**
     * This variable represents the address of the Graph DB Server.
     */
//    public static String GRAPHDB_SERVER = "http://localhost:7200/";
    public static String GRAPHDB_SERVER = "http://160.40.49.192:89";

    /**
     * This variable represents the name of the repository id of interest.
     */
    public static String REPOSITORY_ID = "yposthrizo_db";

    /**
     * This variable is an auxiliary variable, in order to vanish magic numbers from our code.
     */
    public static int INITIAL_UNKNOWN_VALUE = -1;

    /**
     * This variable is an auxiliary variable representing a blank string.
     */
    public static String BLANK_STRING = "";


    // SETTERS - GETTERS

    /**
     * This is the setter of the graph db server constant.
     *
     * @param graphdbServer The graph db server constant, as a String value.
     */
    public static void setGraphdbServerConstant(String graphdbServer) { GRAPHDB_SERVER = graphdbServer; }

    /**
     * This is the setter of the repository id constant.
     *
     * @param repositoryId The repository id constant, as a String value.
     */
    public static void setRepositoryIdConstant(String repositoryId) { REPOSITORY_ID = repositoryId; }


    // FOR IPOSTIRIZW
    public static int IPOSTIRIZW_FIBARO_COMPONENT_COUNTER = 0;
    public static int IPOSTIRIZW_FIBARO_WATTAGE_COMPONENT_COUNTER = 0;
    public static int IPOSTIRIZW_FIBARO_EVENT_COMPONENT_COUNTER = 0;

    public static int FITBIT_DAILY_SLEEP_COMPONENT_COUNTER = 0;
    public static int FITBIT_DAILY_STEPS_COMPONENT_COUNTER = 0;
    public static int FITBIT_DAILY_HEART_RATE_COMPONENT_COUNTER = 0;
    public static int FITBIT_HOURLY_SLEEP_COMPONENT_COUNTER = 0;
    public static int FITBIT_HOURLY_STEPS_COMPONENT_COUNTER = 0;
    public static int FITBIT_HOURLY_HEART_RATE_COMPONENT_COUNTER = 0;

    public static SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

}
