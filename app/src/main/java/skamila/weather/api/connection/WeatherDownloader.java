package skamila.weather.api.connection;

import android.content.Context;
import android.os.AsyncTask;

import skamila.weather.controller.activity.WeatherMain;


public class WeatherDownloader extends AsyncTask<String, Integer, DownloadedData> {

    private Context context;
    private String url;

    public WeatherDownloader(Context contex, String url) {
        this.context = contex;
        this.url = url;
    }

    @Override
    protected DownloadedData doInBackground(String... strings) {
        return ApiClient.sendRequest(url);
    }

    @Override
    protected void onPostExecute(DownloadedData result) {
        ((WeatherMain) context).prepareForecast(result.data);
    }

}
