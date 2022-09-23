import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Earthquake{
    private String country;
    private String place;
    private double magnitude;
    private String date;
    private String time;

    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Earthquake(String country, String place, double magnitude, String date, String time) {
        this.country = country;
        this.place = place;
        this.magnitude = magnitude;
        this.date = date;
        this.time = time;
    }
}

class Country{
    private String name;
    private double minLatitude;
    private double minLongitude;
    private double maxLatitude;
    private double maxLongitude;

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", minLatitude=" + minLatitude +
                ", minLongitude=" + minLongitude +
                ", maxLatitude=" + maxLatitude +
                ", maxLongitude=" + maxLongitude +
                '}';
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public Country(String name, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        this.name = name;
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
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

            ArrayList<Earthquake> earthquakes = getEarthquakes(content.toString(), country);
            printEarthquakes(earthquakes);
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

    private static ArrayList<Earthquake> getEarthquakes(String content, Country country){
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        JSONObject contentJson = new JSONObject(content);
        JSONArray features = contentJson.getJSONArray("features");


        for(Object feature : features){
            try{
            String countryName = country.getName();
            String place = ((JSONObject)feature).getJSONObject("properties").get("place").toString();
            double magnitude = ((JSONObject)feature).getJSONObject("properties").getDouble("mag");
            Timestamp timestamp = new Timestamp(((JSONObject)feature).getJSONObject("properties").getLong("time"));
            String date = timestamp.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String time = timestamp.toLocalDateTime().format(DateTimeFormatter.ISO_TIME);
            Earthquake earthquake = new Earthquake(countryName, place, magnitude, date, time);
            earthquakes.add(earthquake);
            }
            catch (JSONException e){
                continue;
            }
        }
        return earthquakes;
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

        System.out.println(content);

        JSONObject response = new JSONObject(content.toString());
        JSONObject results = response.getJSONArray("results").getJSONObject(0);
        JSONObject geometry =  results.getJSONObject("geometry");
        JSONObject bounds = geometry.getJSONObject("viewport");
        JSONObject northeast = bounds.getJSONObject("northeast");
        JSONObject southwest = bounds.getJSONObject("southwest");

        double minLatitude = Math.min(northeast.getDouble("lat"), southwest.getDouble("lat"));
        double maxLatitude = Math.max(northeast.getDouble("lat"), southwest.getDouble("lat"));
        double minLongitude = Math.min(northeast.getDouble("lng"), southwest.getDouble("lng"));
        double maxLongitude = Math.max(northeast.getDouble("lng"), southwest.getDouble("lng"));

        System.out.println(new Country(country, minLatitude, minLongitude, maxLatitude, maxLongitude));

        return new Country(country, minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

    private static String getDate(int countOfDays){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.minusDays(countOfDays).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static void printEarthquakes(ArrayList<Earthquake> earthquakes){
        System.out.println("Country\t|\tPlace of Earthquake\t|\tMagnitude\t|\tDate\t|\tTime");
        System.out.println("-------------------------------------------------------------------------------------");

        for(Earthquake earthquake : earthquakes){
            System.out.print(earthquake.getCountry() + "\t|\t");
            System.out.print(earthquake.getPlace() + "\t|\t");
            System.out.print(earthquake.getMagnitude() + "\t|\t");
            System.out.print(earthquake.getDate() + "\t|\t");
            System.out.print(earthquake.getTime() + "\t\t");
            System.out.println();
        }
    }

}
