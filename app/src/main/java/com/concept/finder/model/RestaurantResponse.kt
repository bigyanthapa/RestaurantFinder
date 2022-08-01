package com.concept.finder.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantResponse(
    @Json(name = "results")
    val results: List<RestaurantResult>
)

@JsonClass(generateAdapter = true)
data class RestaurantResult(
    @Json(name = "place_id")
    val id: String,
    @Json(name = "geometry")
    val geometry: Geometry,
    @Json(name = "name")
    val name: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "vicinity")
    val vicinity: String,
    @Json(name = "rating")
    val rating: Float
)

@JsonClass(generateAdapter = true)
data class Geometry(
    @Json(name = "location") var location: Location? = Location(),
    @Json(name = "viewport") var viewport: Viewport? = Viewport()
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "lat") var lat: Double? = null,
    @Json(name = "lng") var lng: Double? = null
)

@JsonClass(generateAdapter = true)
data class Viewport(
    @Json(name = "northeast") var northeast: Port? = Port(),
    @Json(name = "southwest") var southwest: Port? = Port()
)

@JsonClass(generateAdapter = true)
data class Port(
    @Json(name = "lat") var lat: Double? = null,
    @Json(name = "lng") var lng: Double? = null
)