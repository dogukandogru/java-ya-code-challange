import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

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
        String input = scanner.nextLine();
        int countOfDays = Integer.parseInt(input.split(", ")[1]);


        try{
            Country country = getCountry(input.split(", ")[0]);
            String starttime = getDate(countOfDays);
            String urlString = baseURL + "&minlatitude=" + country.getMinLatitude() + "&maxlatitude=" + country.getMaxLatitude()
                    + "&minlongitude=" + country.getMinLongitude() + "&maxlongitude=" + country.getMaxLongitude() + "&starttime=" + starttime;

            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            System.out.println(content);
        }
        catch (MalformedURLException e){
            System.out.println(e);
        }
        catch (IOException e){
            System.out.println(e);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static String addParameter(String parameter, String value){
        return baseURL  + "&" + parameter + "=" + value;
    }

    private static Country getCountry(String country) throws IOException {
        String urlString = googleGeocodingBaseURL + "?address=" + country + "&key=" +  googleGeocodingAPIKey;
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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

        return new Country(minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

    private static String getDate(int countOfDays){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.minusDays(countOfDays).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
