package skamila.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import skamila.weather.api.DownloadedData;

public class ApiClient {

    public static DownloadedData sendRequest(String _url) {
        try {
            URL url = new URL(_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String string = "";
            while((string = bufferedReader.readLine()) != null){
                stringBuilder.append(string);
            }
            return new DownloadedData(connection.getResponseCode(), stringBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DownloadedData(-1, "");

    }

}
