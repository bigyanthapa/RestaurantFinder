package com.concept.finder.mapper

import com.concept.finder.model.RestaurantDetailResponse
import com.concept.finder.model.RestaurantResult
import com.concept.finder.model.domain.Restaurant
import com.concept.finder.model.domain.RestaurantDetail
import com.google.android.gms.maps.model.LatLng

fun List<RestaurantResult>.toDomainModel(): List<Restaurant> {
    return this.map {
        Restaurant(
            id = it.id,
            name = it.name,
            latLng = LatLng(it.geometry.location?.lat ?: 0.0, it.geometry.location?.lng ?: 0.0),
            icon = it.icon,
            vicinity = it.vicinity,
            rating = it.rating
        )
    }
}

fun RestaurantDetailResponse.toDomainModel(): RestaurantDetail {
    return with(this.result){
        RestaurantDetail(
            id = id ?: "",
            icon = icon,
            name = name,
            address = address,
            phone = phone,
            rating = rating,
            openNow = openingHours?.openNow ?: false,
            weekSchedule = openingHours?.weekSchedule.orEmpty()
        )
    }
}


