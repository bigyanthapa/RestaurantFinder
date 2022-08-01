package com.concept.finder.repo

import com.concept.finder.BaseTest
import com.concept.finder.model.RestaurantDetailResponse
import com.concept.finder.model.RestaurantResponse
import com.concept.finder.model.RestaurantResult
import com.concept.finder.service.RestaurantService
import io.kotlintest.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantRepositoryImplTest : BaseTest() {

    private val testId = "testId"
    private val testRestaurantResult = mockk<RestaurantResult>(relaxed = true) {
        every { id } returns testId
    }
    private val testResponse = mockk<RestaurantResponse>(relaxed = true) {
        every { results } returns listOf(testRestaurantResult)
    }
    private val testDetailResponse = mockk<RestaurantDetailResponse>(relaxed = true) {
        every { result } returns mockk(relaxed = true) {
            every { id } returns testId
        }
    }

    private val mockService: RestaurantService = mockk(relaxed = true)
    private val repository = RestaurantRepositoryImpl(mockService)

    @Nested
    inner class FetchRestaurants {

        @Test
        fun `fetchRestaurants should return error when restaurant response is null`() =
            coroutinesTestRule.testDispatcher.runBlockingTest {
                coEvery { mockService.fetchRestaurants(any(), any(), any(), any()) } throws Exception()
                repository.fetchRestaurants("1") shouldBe null
            }

        @Test
        fun `fetchDetail should return success when restaurant detail is not null`() =
            coroutinesTestRule.testDispatcher.runBlockingTest {
                coEvery { mockService.fetchRestaurants(any(), any(), any(), any()) } returns testResponse
                repository.fetchRestaurants("1") shouldBe testResponse
            }
    }

    @Nested
    inner class FetchDetails {

        @Test
        fun `fetchDetail should return error when restaurant detail is null`() = coroutinesTestRule.testDispatcher.runBlockingTest {
            coEvery { mockService.fetchDetail(any(), any(), any()) } throws Exception()

            repository.fetchDetail("1") shouldBe null
        }

        @Test
        fun `fetchDetail should return success when restaurant detail is not null`() = coroutinesTestRule.testDispatcher.runBlockingTest {
            coEvery { mockService.fetchDetail(any(), any(), any()) } returns testDetailResponse

            repository.fetchDetail("1") shouldBe testDetailResponse
        }
    }
}