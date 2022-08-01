package com.concept.finder.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.concept.finder.R
import com.concept.finder.databinding.FragmentRestaurantDetailBinding
import com.concept.finder.detail.RestaurantDetailViewModel.RestaurantDetailState.Error
import com.concept.finder.detail.RestaurantDetailViewModel.RestaurantDetailState.Loading
import com.concept.finder.detail.RestaurantDetailViewModel.RestaurantDetailState.Success
import com.concept.finder.model.domain.RestaurantDetail
import com.concept.finder.repo.Repos
import com.concept.finder.util.CoroutineDispatchersImpl
import com.google.android.material.snackbar.Snackbar

class RestaurantDetailFragment : Fragment() {

    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel = RestaurantDetailViewModel(CoroutineDispatchersImpl(), Repos.restaurants)

    private val args: RestaurantDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        val placeId = args.id
        viewModel.fetchDetail(placeId)
    }

    private fun setupObservers() {
        viewModel.restaurantDetailState.observe(viewLifecycleOwner) { restaurantDetailState ->
            restaurantDetailState?.let { state ->
                when(state) {
                    Loading -> binding.progressBar.isVisible = true
                    is Success -> onRestaurantDetailReceived(state.detail)
                    Error -> onError()
                }
            }
        }
    }

    private fun onRestaurantDetailReceived(detail: RestaurantDetail) {
        binding.apply {
            progressBar.isVisible = false
            content.isVisible = true
            name.text = detail.name
            icon.load(detail.icon)
            address.text = detail.address
            phone.text = detail.phone
            rating.text = root.context.getString(R.string.rating, detail.rating)
            schedule.text = detail.weekSchedule.joinToString("\n")
        }
    }

    private fun onError() {
        binding.progressBar.isVisible = false
        Snackbar.make(
            binding.root,
            R.string.detail_empty_error_message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}