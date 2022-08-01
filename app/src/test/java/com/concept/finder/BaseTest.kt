package com.concept.finder

import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.*
import org.junit.Rule
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantTaskExecutorExtension::class, MockKExtension::class)
abstract class BaseTest {
    @get:Rule val coroutinesTestRule = CoroutineTestRule()
}