package com.concept.finder.service

import com.concept.finder.network.Http

object Services {
    val eventsService: RestaurantService by lazy { Http.retrofit.create(RestaurantService::class.java) }
}