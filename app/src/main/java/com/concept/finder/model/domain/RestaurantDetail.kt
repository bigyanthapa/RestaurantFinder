package com.concept.finder.model.domain

data class RestaurantDetail(
    val id: String = "",
    val icon: String = "",
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val rating: Float = 0f,
    val openNow: Boolean = false,
    val weekSchedule: List<String> = emptyList()
)