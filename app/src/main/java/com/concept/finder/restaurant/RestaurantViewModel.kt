package com.concept.finder.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concept.finder.mapper.toDomainModel
import com.concept.finder.model.RestaurantResult
import com.concept.finder.model.domain.Restaurant
import com.concept.finder.repo.RestaurantRepository
import com.concept.finder.util.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class RestaurantViewModel(private val dispatchers: CoroutineDispatchers, private val restaurantRepository: RestaurantRepository) : ViewModel() {

    val cachedRestaurants: MutableList<Restaurant> = mutableListOf()

    private var _restaurantState = MutableLiveData<RestaurantState>()
    val restaurantState: LiveData<RestaurantState> = _restaurantState

    fun fetchRestaurants(location: String) = viewModelScope.launch(dispatchers.IO) {
        _restaurantState.postValue(RestaurantState.Loading)
        restaurantRepository.fetchRestaurants(location)?.let { response ->
            cachedRestaurants.clear()
            cachedRestaurants.addAll(response.results.toDomainModel())
            _restaurantState.postValue(RestaurantState.Success(cachedRestaurants))
        } ?: run {
            _restaurantState.postValue(RestaurantState.Error)
        }
    }

    sealed class RestaurantState {
        object Loading: RestaurantState()
        object Error: RestaurantState()
        data class Success(val restaurants: List<Restaurant>): RestaurantState()
    }
}