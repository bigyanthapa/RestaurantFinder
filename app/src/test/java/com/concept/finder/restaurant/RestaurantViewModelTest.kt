package com.concept.finder.restaurant

import com.concept.finder.BaseTest
import com.concept.finder.model.RestaurantResponse
import com.concept.finder.model.RestaurantResult
import com.concept.finder.repo.RestaurantRepository
import com.concept.finder.restaurant.RestaurantViewModel.RestaurantState.Error
import com.concept.finder.restaurant.RestaurantViewModel.RestaurantState.Success
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantViewModelTest : BaseTest() {

    private val testId = "testId"
    private val testRestaurantResult = mockk<RestaurantResult>(relaxed = true) {
        every { id } returns testId
    }
    private val testResponse = mockk<RestaurantResponse>(relaxed = true) {
        every { results } returns listOf(testRestaurantResult)
    }

    private val mockRepository: RestaurantRepository = mockk(relaxed = true)
    private val viewModel =
        RestaurantViewModel(coroutinesTestRule.testDispatcherProvider, mockRepository)

    @Nested
    inner class FetchRestaurants {
        private var result: RestaurantViewModel.RestaurantState? = null

        @BeforeEach
        fun beforeEach() {
            viewModel.restaurantState.observeForever { result = it }
        }

        @Test
        fun `fetchRestaurants should return error when restaurant response is null`() =
            coroutinesTestRule.testDispatcher.runBlockingTest {
                coEvery { mockRepository.fetchRestaurants(any()) } returns null
                viewModel.fetchRestaurants("1")

                result shouldBe Error
                viewModel.cachedRestaurants.isEmpty() shouldBe true
            }

        @Test
        fun `fetchDetail should return success when restaurant detail is not null`() =
            coroutinesTestRule.testDispatcher.runBlockingTest {
                coEvery { mockRepository.fetchRestaurants(any()) } returns testResponse
                viewModel.fetchRestaurants("1")

                result.shouldBeTypeOf<Success>()
                (result as Success).restaurants.first().id shouldBe testId
            }
    }
}