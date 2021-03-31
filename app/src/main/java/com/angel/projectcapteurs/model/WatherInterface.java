package com.angel.projectcapteurs.model;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

//?lat=45.1834546&lon=5.742217&appid=2e2becf15ab311f47c7cd361617b06e4&units=metric&lang=fr
/*
http://api.openweathermap.org/data/2.5/weather?lat=45.1834546&lon=5.742217&appid=2e2becf15ab311f47c7cd361617b06e4&units=metric&lang=fr
 */
public interface WatherInterface {
    @GET("/weather?lat={lat}&lon={lon}&appid=2e2becf15ab311f47c7cd361617b06e4&units=metric&lang=fr")
    Call<List> getMeteo(@Path("lat") float lat, @Path("lon") float lon);
}
