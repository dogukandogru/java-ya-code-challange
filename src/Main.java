import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2022-01-01&endtime=2022-01-02";
    public static Scanner scanner;

    public static void main(String[] args) {
        scanner = getScanner();
        String input = scanner.nextLine();
        String country = input.split(", ")[0];
        String countOfDays = input.split(", ")[1];

        try{
            URL urlObject = createURLObject(URL);
            HttpURLConnection con = openConnection(urlObject);
            con.setRequestMethod("GET");


            Map<String, String> parameters = new HashMap<>();
            parameters.put("param1", "val");

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();


        }
        catch (Exception e){
            // continue;
        }
    }

    public static URL createURLObject(String url) throws MalformedURLException {
        return new URL(url);
    }

    public static HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public static Scanner getScanner(){
        return new Scanner(System.in);
    }
}
