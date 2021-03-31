package com.angel.projectcapteurs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.angel.projectcapteurs.model.Main;
import com.angel.projectcapteurs.model.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private TextView txtLatitude;
    private TextView txtLongitude;
    private ImageView imgBackGround;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        callAPI(1f, 3f);
    }

    private void initComponents() {
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        imgBackGround = findViewById(R.id.imgBackground);
    }

    private void callAPI(float latitude, float longitude) {
        AsyncTaskRunner runner = new AsyncTaskRunner();
        // runner.execute("35", "139");
        runner.execute(String.valueOf(latitude), String.valueOf(longitude));
    }




    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp = null;
        @Override
        protected String doInBackground(String... params) {
            try {

                Log.d("MYDATA", Arrays.toString(params));
                    URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?" +
                        "lat=" + params[0] + "&lon=" + params[1]  +
                        "&appid=2e2becf15ab311f47c7cd361617b06e4&units=metric&lang=fr");

                // fake url
                // URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=2e2becf15ab311f47c7cd361617b06e4&units=metric&lang=fr");


                String jsonS = "";
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());

                    Gson gson = new Gson();
                    Response res = gson.fromJson(response.toString(), Response.class);

                    resp = ""+res.getMain().getTemp();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            // txtLatitude.setText(result);
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {
            // txtLatitude.setText(text[0]);
        }
    }
}
