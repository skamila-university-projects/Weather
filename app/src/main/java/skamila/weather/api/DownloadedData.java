package skamila.weather.api;

public class DownloadedData{

    public final int status;
    public final String data;

    public DownloadedData(int status, String data) {
        this.status = status;
        this.data = data;
    }

}
