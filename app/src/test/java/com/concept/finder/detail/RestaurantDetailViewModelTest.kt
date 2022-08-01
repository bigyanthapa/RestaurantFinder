package com.concept.finder.detail

import com.concept.finder.BaseTest
import com.concept.finder.detail.RestaurantDetailViewModel.RestaurantDetailState.Error
import com.concept.finder.detail.RestaurantDetailViewModel.RestaurantDetailState.Success
import com.concept.finder.model.RestaurantDetailResponse
import com.concept.finder.repo.RestaurantRepository
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
class RestaurantDetailViewModelTest : BaseTest() {

    private val testId = "testId"
    private val testDetailResponse = mockk<RestaurantDetailResponse>(relaxed = true) {
        every { result } returns mockk(relaxed = true) {
            every { id } returns testId
        }
    }
    private val mockRepository: RestaurantRepository = mockk(relaxed = true)
    private val viewModel =
        RestaurantDetailViewModel(coroutinesTestRule.testDispatcherProvider, mockRepository)

    @Nested
    inner class FetchDetails {
        private var result: RestaurantDetailViewModel.RestaurantDetailState? = null

        @BeforeEach
        fun beforeEach() {
            viewModel.restaurantDetailState.observeForever { result = it }
        }

        @Test
        fun `fetchDetail should return error when restaurant detail is null`() = coroutinesTestRule.testDispatcher.runBlockingTest {
            coEvery { mockRepository.fetchDetail(any()) } returns null
            viewModel.fetchDetail("1")

            result shouldBe Error
        }

        @Test
        fun `fetchDetail should return success when restaurant detail is not null`() = coroutinesTestRule.testDispatcher.runBlockingTest {
            coEvery { mockRepository.fetchDetail(any()) } returns testDetailResponse
            viewModel.fetchDetail("1")

            result.shouldBeTypeOf<Success>()
            (result as Success).detail.id shouldBe testId
        }
    }
}