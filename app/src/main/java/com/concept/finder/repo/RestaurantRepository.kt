package com.concept.finder.repo

import com.concept.finder.BuildConfig
import com.concept.finder.model.RestaurantDetailResponse
import com.concept.finder.model.RestaurantResponse
import com.concept.finder.service.RestaurantService

const val SEARCH_RADIUS = "1500"
const val SEARCH_TYPE = "restaurant"
const val DETAIL_FIELDS =
    "place_id,name,icon,formatted_address,formatted_phone_number,rating,opening_hours"

interface RestaurantRepository {

    suspend fun fetchRestaurants(location: String): RestaurantResponse?

    suspend fun fetchDetail(id: String): RestaurantDetailResponse?
}

class RestaurantRepositoryImpl(private val service: RestaurantService) : RestaurantRepository {

    override suspend fun fetchRestaurants(location: String): RestaurantResponse? {
        return try {
            service.fetchRestaurants(location, SEARCH_RADIUS, SEARCH_TYPE, BuildConfig.MAPS_API_KEY)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun fetchDetail(id: String): RestaurantDetailResponse? {
        return try {
            service.fetchDetail(id, DETAIL_FIELDS, BuildConfig.MAPS_API_KEY)
        } catch (e: Exception) {
            null
        }
    }
}