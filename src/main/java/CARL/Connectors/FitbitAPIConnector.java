package CARL.Connectors;

import CARL.entities.User;

import java.util.List;

public class FitbitAPIConnector {

    static String startDate = "2020-06-29";
    static String stopDate  = "2020-07-29";

    //url for daily and hourly sleep data
    public static String dailySleepDataAPI = "https://carl.iti.gr/api/device/v2/sleep/day/sum/"+ startDate +"/"+ stopDate +"/?format=json";
    public static String hourlySleepDataAPI = "https://carl.iti.gr/api/device/v2/sleep/hour/sum/"+ startDate + "/"+ stopDate +"/?format=json";

    //url for daily and hourly steps data
    public static String dailyStepsDataAPI = "https://carl.iti.gr/api/device/v2/steps/day/avg/" + startDate + "/" + stopDate + "/?format=json";
    public static String hourlyStepsDataAPI = "https://carl.iti.gr/api/device/v2/steps/hour/avg/" + startDate + "/" + stopDate + "/?format=json";

    //url for daily and hourly heart rate data
    public static String dailyHeartRateAPI = "https://carl.iti.gr/api/device/v2/heartrate/day/avg/" + startDate + "/" + stopDate +"/?format=json";
    public static String hourlyHeartRateAPI = "https://carl.iti.gr/api/device/v2/heartrate/hour/avg/" + startDate + "/" + stopDate + "/?format=json";
    public static String minuteHeartRateAPI = "https://carl.iti.gr/api/device/v2/heartrate/minute/avg/" + startDate + "/" + stopDate + "/?format=json";

    public FitbitAPIConnector(List<User> users) {

        String dailyJsonStr, hourlyJsonStr, minJsonStr;

        for (int k = 0; k < users.size(); k++) {

            // Auxiliary variable to increase readability of the code.
            User userUnderExamination = users.get(k);

            HttpRequest n = new HttpRequest();
            try {
                dailyJsonStr = n.http_request(dailySleepDataAPI, users.get(k).getToken());
                hourlyJsonStr = n.http_request(hourlySleepDataAPI, users.get(k).getToken());

                userUnderExamination.populateSleep(dailyJsonStr, hourlyJsonStr);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                dailyJsonStr = n.http_request(dailyStepsDataAPI, users.get(k).getToken());
                hourlyJsonStr = n.http_request(hourlyStepsDataAPI, users.get(k).getToken());

                userUnderExamination.populateSteps(dailyJsonStr, hourlyJsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                dailyJsonStr = n.http_request(dailyHeartRateAPI, users.get(k).getToken());
                hourlyJsonStr = n.http_request(hourlyHeartRateAPI, users.get(k).getToken());
                minJsonStr = n.http_request(minuteHeartRateAPI, users.get(k).getToken());

                userUnderExamination.populateHeartRate(dailyJsonStr, hourlyJsonStr, minJsonStr);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //userUnderExamination.status();
        }
    }
}
