package edu.stanford.zoha.simpleyelp

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

public interface YelpService{
    @GET("businesses/search")
    fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("term") searchTerm: String,
        @Query("location") location: String) : retrofit2.Call<YelpSearchResult>
}