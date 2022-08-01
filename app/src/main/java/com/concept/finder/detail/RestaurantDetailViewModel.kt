package com.concept.finder.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concept.finder.mapper.toDomainModel
import com.concept.finder.model.domain.RestaurantDetail
import com.concept.finder.repo.RestaurantRepository
import com.concept.finder.util.CoroutineDispatchers
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(private val dispatchers: CoroutineDispatchers, private val restaurantRepository: RestaurantRepository) :
    ViewModel() {

    private var _restaurantDetailState = MutableLiveData<RestaurantDetailState>()
    val restaurantDetailState: LiveData<RestaurantDetailState> = _restaurantDetailState

    fun fetchDetail(placeId: String) = viewModelScope.launch(dispatchers.IO) {
        _restaurantDetailState.postValue(RestaurantDetailState.Loading)
        restaurantRepository.fetchDetail(placeId)?.let {
            _restaurantDetailState.postValue(RestaurantDetailState.Success(it.toDomainModel()))
        } ?: run {
            _restaurantDetailState.postValue(RestaurantDetailState.Error)
        }
    }

    sealed class RestaurantDetailState {
        object Loading : RestaurantDetailState()
        object Error : RestaurantDetailState()
        data class Success(val detail: RestaurantDetail) : RestaurantDetailState()
    }
}