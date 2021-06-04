package com.example.favoriterecipes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    //0
    @GET("/search")
    Call<Response> search(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key);

    //1
    @GET("/search")
    Call<Response> searchWithDiet(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key,
                                  @Query("diet") String diet);

    //2
    @GET("/search")
    Call<Response> searchWithCaloriesRange(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key,
                                           @Query("calories") String calories_range);

    //3
    @GET("/search")
    Call<Response> searchWitExcluded(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key,
                                     @Query("excluded") ArrayList<String> excludeds);
    //4
    @GET("/search")
    Call<Response> searchWitExcludedAndCalories(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key,
                                                @Query("calories") String calories_range, @Query("excluded") ArrayList<String> excludeds);

    //5
    @GET("/search")
    Call<Response> searchWitExcludedAndDiet(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key,
                                            @Query("diet") String diet, @Query("excluded") ArrayList<String> excludeds);

    //6
    @GET("/search")
    Call<Response> searchWithCaloriesRangeAndDiet(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key,
                                                  @Query("diet") String diet, @Query("calories") String calories_range);

    //7
    @GET("/search")
    Call<Response> searchWithAllFilters(@Query("q") String q, @Query("app_id") String app_id, @Query("app_key") String app_key,
                                                  @Query("diet") String diet, @Query("calories") String calories_range,
                                                  @Query("excluded") ArrayList<String> excludeds);

}
