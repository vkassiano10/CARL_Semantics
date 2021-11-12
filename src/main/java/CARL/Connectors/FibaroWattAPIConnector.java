package CARL.Connectors;

import CARL.entities.User;

import java.util.List;

/**
 * This class represents the CARL Fibaro API connector, i.e. the process of retrieving the Fibaro Measurements.
 * The purpose of this class is also to create and populate Ypostirizi Fibaro Component instances, based on the json files
 * that are retrieved through the API requests to the dedicated Ypostirizo server.
 */
public class FibaroWattAPIConnector {

    /**
     * This string is the url of the API that servers the fibaro component measurements.
     */
    //change url with device id
    String url = "https://carl.iti.gr/api/device/v2/fibaro-wattage/2021-10-01/2021-10-10/?format=json";

    public FibaroWattAPIConnector(List<User> users) {

        String jsonstr;

        //for (int k = ; k < users.size(); k++) {
            HttpRequest n = new HttpRequest();
            try {
                users.get(0).status();
                jsonstr = n.http_request(url, users.get(0).getToken());
                users.get(0).populateFibaroWattageComponentsList(jsonstr);
                //System.out.println("Jsons response:" + jsonstr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        //}

    }


}

