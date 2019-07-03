package skamila.weather.api;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import skamila.weather.api.forecast.Forecast;
import skamila.weather.controller.WeatherMain;


public class ApiClient extends AsyncTask<String, Integer, DownloadedData> {


        private Context context;
        private String url;

        public ApiClient(Context contex, String url){
            this.context = contex;
            this.url = url;
        }

        @Override
        protected DownloadedData doInBackground(String... strings) {
            try {
                URL url = new URL(this.url);
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
            return null;
        }

        @Override
        protected void onPostExecute(DownloadedData result) {
            ((WeatherMain)context).prepareForecast(result.data);
        }




    private String data;
    private boolean isReady;

    public void loadResponse(Context context, String url, final FileManager fileManager){

        RequestQueue queue = Volley.newRequestQueue(context);

        queue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        data = response;
                        isReady = true;
                        fileManager.saveToFile(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                throw new IllegalArgumentException();
            }

        });
        queue.add(stringRequest);

    }

    public boolean isReady() {
        return isReady;
    }

    public String getResponse(){
        return data;
    }

}
