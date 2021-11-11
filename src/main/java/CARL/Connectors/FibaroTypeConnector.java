package CARL.Connectors;

import CARL.entities.User;

import java.util.List;

public class FibaroTypeConnector {

    static String deviceId = "0";


    /**
     * This string is the url of the API that servers the fibaro component measurements.
     */
    String url = "https://carl.iti.gr/api/device/threshold/" + deviceId + "?format=json";

    public FibaroTypeConnector(List<User> users) {

        String jsonstr;

        for (int k = 0; k < users.size(); k++) {
            HttpRequest n = new HttpRequest();
            try {
                jsonstr = n.http_request(url, users.get(k).getToken());
                users.get(k).populateFibaroComponentsList(jsonstr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
