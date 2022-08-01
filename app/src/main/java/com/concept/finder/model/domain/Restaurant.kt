package com.concept.finder.model.domain

import com.google.android.gms.maps.model.LatLng

data class Restaurant(val id: String, val name: String, val latLng: LatLng, val icon: String, val vicinity: String, val rating: Float)