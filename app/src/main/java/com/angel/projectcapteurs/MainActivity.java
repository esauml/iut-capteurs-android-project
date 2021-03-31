package com.angel.projectcapteurs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.angel.projectcapteurs.model.Main;
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

//                    URL url = new URL ("http://api.openweathermap.org/data/2.5/weather?lat=" +
//                    "{"+ latitude +"}&lon={"+ longitude +"}" +
//                            "&appid=2e2becf15ab311f47c7cd361617b06e4&units=metric&lang=fr");

                    // fake url
                    URL url = new URL ("http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=2e2becf15ab311f47c7cd361617b06e4&units=metric&lang=fr");


                    String jsonS = "";
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        jsonS += inputLine;
                    }

                    Gson gson = new Gson();
                    Main weather = gson.fromJson(jsonS, Main.class);

                    txtLatitude.setText(""+weather.getTemp());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }
}