package CARL.Connectors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    private static String url1;
    private static String url2;

    public HttpRequest() {
    }

    public String http_request(String url, String token) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // String token="3f3622c428a02e5a45c2af99c07a92d971d683e0";
        con.setRequestProperty("Authorization", "Token " + token);
        // con.setRequestProperty("Token",Base64.encode(token.getBytes()));
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //System.out.println(response.toString());
        //print in String
        return response.toString();
    }
    //Read JSON response and print

}