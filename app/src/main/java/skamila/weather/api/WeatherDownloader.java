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

import skamila.weather.ApiClient;
import skamila.weather.api.forecast.Forecast;
import skamila.weather.controller.WeatherMain;


public class WeatherDownloader extends AsyncTask<String, Integer, DownloadedData> {

        private Context context;
        private String url;

        public WeatherDownloader(Context contex, String url){
            this.context = contex;
            this.url = url;
        }

        @Override
        protected DownloadedData doInBackground(String... strings) {
            return ApiClient.sendRequest(url);
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
