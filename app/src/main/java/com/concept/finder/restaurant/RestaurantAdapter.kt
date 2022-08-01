package com.concept.finder.restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.concept.finder.R
import com.concept.finder.databinding.RestaurantItemBinding
import com.concept.finder.model.domain.Restaurant

class RestaurantAdapter(private val onRestaurantClicked:(id: String) -> Unit) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    var restaurants: List<Restaurant> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantAdapter.RestaurantViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = RestaurantItemBinding.inflate(layoutInflater, parent, false)
        return RestaurantViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: RestaurantAdapter.RestaurantViewHolder, position: Int) {
        if (position in restaurants.indices) {
            holder.bind(restaurants[position])
        }
    }

    override fun getItemCount(): Int = restaurants.size

    inner class RestaurantViewHolder(private val binding: RestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant) {
            binding.apply {
                name.text = restaurant.name
                icon.load(restaurant.icon)
                address.text = restaurant.vicinity
                rating.text = root.context.getString(R.string.rating, restaurant.rating)
                root.setOnClickListener { onRestaurantClicked(restaurant.id) }
            }
        }
    }
}