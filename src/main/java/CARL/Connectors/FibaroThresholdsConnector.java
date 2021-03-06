package CARL.Connectors;

import CARL.entities.User;

import java.util.List;

/**
 * This class represents the CARL Fibaro API connector, i.e. the process of retrieving the Fibaro Measurements.
 * The purpose of this class is also to create and populate Ypostirizi Fibaro Component instances, based on the json files
 * that are retrieved through the API requests to the dedicated Ypostirizo server.
 */
public class FibaroThresholdsConnector {

    //change device id for all ids
    static String deviceId = "0";


    /**
     * This string is the url of the API that servers the fibaro component measurements.
     */
    String url = "https://carl.iti.gr/api/device/threshold/" + deviceId + "?format=json";

    public FibaroThresholdsConnector(List<User> users) {

        String jsonstr;

        for (int k = 0; k < users.size(); k++) {
            HttpRequest n = new HttpRequest();
            for (int i=0; i< users.get(k).userDeviceList.size(); i++){
                try {
                    jsonstr = n.http_request(url, users.get(k).getToken());
                    users.get(k).populateFibaroThresholdsList(jsonstr);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
