import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Earthquake{
    private String country;
    private String placeOfEarthQuake;
    private int magnitude;
    private String date;
}

class Country{
    private double minLatitude;
    private double minLongitude;
    private double maxLatitude;
    private double maxLongitude;

    public Country(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
    }
}


public class Main {
    private static final String baseURL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";
    private static final String googleGeocodingAPIKey = "AIzaSyDegPsjWrSEmH7PcnQKz8hwwY_8kFqZMXQ";
    private static final String googleGeocodingBaseURL = "https://maps.googleapis.com/maps/api/geocode/json";
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        /*String input = scanner.nextLine();
        String country = input.split(", ")[0];
        String countOfDays = input.split(", ")[1];
         */

        String url = baseURL;

        try{
            getCoordinate("Turkey");
        }
        catch (Exception e){
            System.out.println(e);
        }

        /*
        try{
            URL url = new URL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

        }
        catch (MalformedURLException e){

        }
        catch (IOException e){

        }
        */
    }

    public static String addParameter(String parameter, String value){
        return baseURL  + "&" + parameter + "=" + value;
    }

    private static Country getCoordinate(String country) throws IOException {
        String urlString = googleGeocodingBaseURL + "?address=" + country + "&key=" +  googleGeocodingAPIKey;
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        System.out.println(status);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();


        JSONObject response = new JSONObject(content.toString());
        JSONObject results = response.getJSONArray("results").getJSONObject(0);
        JSONObject geometry =  results.getJSONObject("geometry");
        JSONObject bounds = geometry.getJSONObject("bounds");
        JSONObject northeast = bounds.getJSONObject("northeast");
        JSONObject southwest = bounds.getJSONObject("southwest");

        double minLatitude = Math.min(northeast.getDouble("lat"), southwest.getDouble("lat"));
        double maxLatitude = Math.max(northeast.getDouble("lat"), southwest.getDouble("lat"));
        double minLongitude = Math.min(northeast.getDouble("lng"), southwest.getDouble("lng"));
        double maxLongitude = Math.max(northeast.getDouble("lng"), southwest.getDouble("lng"));

        System.out.println(minLatitude + " " + maxLatitude  + " " + minLongitude +" "  + maxLongitude);

        return new Country(minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

}
