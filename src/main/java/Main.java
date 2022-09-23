import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private static final String baseURL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";
    private static final String googleGeocodingAPIKey = "AIzaSyDegPsjWrSEmH7PcnQKz8hwwY_8kFqZMXQ";
    private static final String googleGeocodingBaseURL = "https://maps.googleapis.com/maps/api/geocode/json";

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        try{
            Country country = getCountry(input.split(", ")[0].replace(" ","%20"));
            int countOfDays = Integer.parseInt(input.split(", ")[1]);
            String starttime = getDate(countOfDays);
            String urlString = addParameters(country, starttime);

            String content = sendHttpRequest(urlString);

            ArrayList<Earthquake> earthquakes = getEarthquakes(content, country);
            printEarthquakes(earthquakes, countOfDays);
        }
        catch (MalformedURLException e){
            System.out.println("URL is malformed!");
            System.exit(-1);
        }
        catch (IOException e){
            System.out.println("IO exception occured, try again!");
            System.exit(-1);
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static String sendHttpRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        if(status != 200){
            System.out.println("Error occurred when sending http request");
            System.exit(-1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        return content.toString();
    }

    public static String addParameters(Country country, String starttime){
        return baseURL + "&minlatitude=" + country.getMinLatitude() + "&maxlatitude=" + country.getMaxLatitude()
                + "&minlongitude=" + country.getMinLongitude() + "&maxlongitude=" + country.getMaxLongitude() + "&starttime=" + starttime;
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
            catch (JSONException ignored){
            }
        }
        return earthquakes;
    }

    private static Country getCountry(String country) throws IOException {
        String urlString = googleGeocodingBaseURL + "?address=" + country + "&key=" +  googleGeocodingAPIKey;
        String content = sendHttpRequest(urlString);

        JSONObject response = new JSONObject(content);
        JSONObject results = response.getJSONArray("results").getJSONObject(0);
        JSONObject geometry =  results.getJSONObject("geometry");
        JSONObject bounds = geometry.getJSONObject("viewport");
        JSONObject northeast = bounds.getJSONObject("northeast");
        JSONObject southwest = bounds.getJSONObject("southwest");

        double minLatitude = Math.min(northeast.getDouble("lat"), southwest.getDouble("lat"));
        double maxLatitude = Math.max(northeast.getDouble("lat"), southwest.getDouble("lat"));
        double minLongitude = Math.min(northeast.getDouble("lng"), southwest.getDouble("lng"));
        double maxLongitude = Math.max(northeast.getDouble("lng"), southwest.getDouble("lng"));

        return new Country(country, minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

    private static String getDate(int countOfDays){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.minusDays(countOfDays).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static void printEarthquakes(ArrayList<Earthquake> earthquakes, int countOfDays){
        if(earthquakes.size() == 0){
            System.out.println(String.format("No Earthquakes were recorded past %s days", countOfDays));
        }
        else{
            System.out.println("Country\t|\tPlace of Earthquake\t|\tMagnitude\t|\tDate\t|\tTime");
            System.out.println("-------------------------------------------------------------------------------------");

            for(Earthquake earthquake : earthquakes){
                System.out.print(earthquake.getCountry().replace("%20", " ") + "\t|\t");
                System.out.print(earthquake.getPlace() + "\t|\t");
                System.out.print(earthquake.getMagnitude() + "\t|\t");
                System.out.print(earthquake.getDate() + "\t|\t");
                System.out.print(earthquake.getTime() + "\t\t");
                System.out.println();
            }
        }
    }
}
