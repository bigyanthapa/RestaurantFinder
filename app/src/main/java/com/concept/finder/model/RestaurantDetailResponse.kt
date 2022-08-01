package com.concept.finder.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantDetailResponse(
    @Json(name = "result")
    val result: RestaurantDetailResult
)

@JsonClass(generateAdapter = true)
data class RestaurantDetailResult(
    @Json(name = "place_id")
    val id: String? = null,
    @Json(name = "name")
    val name: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "formatted_address")
    val address: String,
    @Json(name = "formatted_phone_number")
    val phone: String,
    @Json(name = "rating")
    val rating: Float,
    @Json(name = "opening_hours")
    val openingHours: OpeningHours? = null
)

@JsonClass(generateAdapter = true)
data class OpeningHours(
    @Json(name = "open_now")
    val openNow: Boolean,
    @Json(name = "weekday_text")
    val weekSchedule: List<String>
)