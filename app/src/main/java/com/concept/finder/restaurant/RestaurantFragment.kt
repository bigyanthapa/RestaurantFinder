package com.concept.finder.restaurant

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.concept.finder.MainActivity
import com.concept.finder.R
import com.concept.finder.databinding.FragmentRestaurantBinding
import com.concept.finder.extension.setDivider
import com.concept.finder.model.domain.Restaurant
import com.concept.finder.restaurant.RestaurantViewModel.RestaurantState.Error
import com.concept.finder.restaurant.RestaurantViewModel.RestaurantState.Loading
import com.concept.finder.restaurant.RestaurantViewModel.RestaurantState.Success
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

class RestaurantFragment : Fragment(), MenuProvider {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationBuilder by lazy { StringBuilder() }

    private var _binding: FragmentRestaurantBinding? = null
    private val binding get() = _binding!!

    private val restaurantAdapter: RestaurantAdapter =
        RestaurantAdapter { id -> onRestaurantClicked(id) }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                onLocationPermissionGranted()
            } else {
                onLocationPermissionDeniedError()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        binding.restaurants.apply {
            setDivider(R.drawable.list_divider)
            adapter = restaurantAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hasPermissions()) {
            onLocationPermissionGranted()
        } else {
            permissionLauncher.launch(PERMISSIONS)
        }

        setupObservers()
    }

    private fun hasPermissions(): Boolean = PERMISSIONS.all {
        ActivityCompat.checkSelfPermission(
            requireActivity(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun onLocationPermissionGranted() {
        val cachedRestaurants = (requireActivity() as MainActivity).viewModel.cachedRestaurants
        if (cachedRestaurants.isEmpty()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    locationBuilder.clear().apply {
                        append(it.latitude, ",", it.longitude)
                    }
                    (requireActivity() as MainActivity).viewModel.fetchRestaurants(locationBuilder.toString())
                } ?: run {
                    onEmptyLocationError()
                }
            }
        } else {
            onRestaurantReceived(cachedRestaurants)
        }
    }

    private fun onRestaurantClicked(id: String) {
        val action = RestaurantFragmentDirections.restaurantDetailAction(id)
        findNavController().navigate(action)
    }

    private fun setupObservers() {
        (requireActivity() as MainActivity).viewModel.restaurantState.observe(viewLifecycleOwner) { restaurantState ->
            restaurantState?.let { state ->
                when (state) {
                    Loading -> binding.progressBar.isVisible = true
                    is Success -> onRestaurantReceived(state.restaurants)
                    Error -> onError()
                }
            }
        }
    }

    private fun onRestaurantReceived(restaurants: List<Restaurant>) {
        binding.progressBar.isVisible = false
        binding.restaurants.isVisible = true
        if (restaurants.isNotEmpty()) {
            restaurantAdapter.restaurants = restaurants
        }
    }

    private fun onError() {
        onLocationPermissionDeniedError()
        Snackbar.make(
            binding.root,
            R.string.empty_restaurants_error,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun onLocationPermissionDeniedError() {
        binding.progressBar.isVisible = false
        binding.permissionMessageContainer.isVisible = true
        binding.restaurants.isVisible = false
    }

    private fun onEmptyLocationError() {
        Snackbar.make(
            binding.root,
            R.string.location_empty_error_message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_map_view, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_map -> {
                findNavController().navigate(R.id.restaurantToRestaurantMapAction)
                true
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val PERMISSIONS = arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    }
}