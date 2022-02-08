package com.example.floppy.data.Conexion.retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WallpapersApi {
    @GET("/images.json")
    Call<ArrayList<String>> getImages();
}
