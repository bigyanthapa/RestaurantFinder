package com.concept.finder.service

import com.concept.finder.model.RestaurantDetailResponse
import com.concept.finder.model.RestaurantResponse
import retrofit2.http.*

interface RestaurantService {
    @GET("nearbysearch/json")
    suspend fun fetchRestaurants(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") type: String,
        @Query("key") key: String
    ): RestaurantResponse

    @GET("details/json")
    suspend fun fetchDetail(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String,
        @Query("key") key: String,
    ): RestaurantDetailResponse
}