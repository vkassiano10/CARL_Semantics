package CARL.Connectors;

import CARL.entities.User;

import java.util.List;

/**
 * This class represents the CARL Fibaro API connector, i.e. the process of retrieving the Fibaro Measurements.
 * The purpose of this class is also to create and populate Ypostirizi Fibaro Component instances, based on the json files
 * that are retrieved through the API requests to the dedicated Ypostirizo server.
 */
public class FibaroAPIConnector {

    static String startDate = "2021-05-28";
    static String stopDate  = "2021-05-28";

    /**
     * This string is the url of the API that servers the fibaro component measurements.
     */
    String url = "https://carl.iti.gr/api/device/v2/fibaro/temperature/" + startDate + "/" + stopDate + "?format=json";

    public FibaroAPIConnector(List<User> users) {

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
