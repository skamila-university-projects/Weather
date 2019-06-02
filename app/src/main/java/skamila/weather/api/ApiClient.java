package skamila.weather.api;

import android.content.Context;

import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ApiClient {

    private String data;

    public String getResponse(Context context, String url, final FileManager fileManager){

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        data = response;
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

        return data;

    }

}
