package com.concept.finder.restaurant

import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.concept.finder.MainActivity
import com.concept.finder.R
import com.concept.finder.databinding.FragmentRestaurantMapBinding
import com.concept.finder.model.domain.Restaurant
import com.concept.finder.restaurant.RestaurantViewModel.RestaurantState.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class RestaurantMapFragment : Fragment(), MenuProvider, OnMapReadyCallback {

    private var _binding: FragmentRestaurantMapBinding? = null
    private val binding get() = _binding!!

    private val locationBuilder by lazy { StringBuilder() }
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        _binding = FragmentRestaurantMapBinding.inflate(inflater, container, false)
        setupSearchView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapsInitializer.initialize(requireActivity())
        (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(
            this
        )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_list_view, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list,
            androidx.appcompat.R.id.home-> {
                findNavController().navigate(R.id.restaurantMapToRestaurantAction)
                true
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val viewModel = (requireActivity() as MainActivity).viewModel
        viewModel.restaurantState.observe(viewLifecycleOwner) { restaurantState ->
            restaurantState?.let { state ->
                when (state) {
                    Loading -> binding.progressBar.isVisible = true
                    is Success -> onRestaurantReceived(state.restaurants)
                    Error -> onError()
                }
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location = query.orEmpty()
                val geoCoder = Geocoder(requireActivity())
                try {
                    val address = geoCoder.getFromLocationName(location, 1).first()
                    locationBuilder.clear().apply {
                        append(address.latitude, ",", address.longitude)
                    }
                    (requireActivity() as MainActivity).viewModel.fetchRestaurants(locationBuilder.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun onRestaurantReceived(restaurants: List<Restaurant>) {
        if (restaurants.isNotEmpty()) {
            animateCamera(restaurants)
        }
    }

    private fun onError() {
        binding.progressBar.isVisible = false
        Snackbar.make(
            binding.root,
            R.string.empty_restaurants_error,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun animateCamera(restaurants: List<Restaurant>) {
        googleMap?.let { map ->
            map.clear()
            for(restaurant in restaurants) {
                map.addMarker(MarkerOptions().position(restaurant.latLng).title(restaurant.name))
            }
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(restaurants.last().latLng, 16f)
            )
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }
}