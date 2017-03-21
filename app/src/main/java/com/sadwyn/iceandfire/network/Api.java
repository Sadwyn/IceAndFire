package com.sadwyn.iceandfire.network;


import com.sadwyn.iceandfire.models.Character;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api {
    @GET("/api/characters")
    Call<List<Character>> getData(@Query("page") int page, @Query("size") int size);
}
