package com.angel.projectcapteurs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView txtTemp;
    private ImageView imgBackGround;

    private Retrofit retrofit;
    private LocationManager locationManager;
    private Location location;
    private Handler handler=new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable=new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable,5000);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    OnGPS();
                }else{
                    getLocation();
                }
            }
        },5000);
        super.onResume();
    }

    private void OnGPS() { final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("enable gps").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void getLocation(){
        if(ActivityCompat.
                checkSelfPermission(
                        MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.
                        checkSelfPermission(
                                MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            Location l=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(l!=null) {


                callAPI(l.getLatitude(), l.getLongitude());
                txtLatitude.setText("Latitude : "+l.getLatitude());
                txtLongitude.setText("Longitude : "+l.getLongitude());
            }else{
                Toast.makeText(this, "Impossible de trouver l’endroit.", Toast.LENGTH_SHORT).show();

            }
        }

    }
    private void initComponents() {
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtTemp=findViewById(R.id.txtTemp);
        imgBackGround = findViewById(R.id.imgBackground);
        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void callAPI(double latitude, double longitude) {
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
