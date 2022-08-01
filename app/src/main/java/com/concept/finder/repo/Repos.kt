package com.concept.finder.repo

import com.concept.finder.service.Services

object Repos {
    val restaurants: RestaurantRepository by lazy {
        RestaurantRepositoryImpl(Services.eventsService)
    }
}