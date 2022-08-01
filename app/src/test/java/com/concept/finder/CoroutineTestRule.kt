package com.concept.finder

import com.concept.finder.util.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()): TestWatcher() {

    val testDispatcherProvider = CoroutineDispatcherProvider(testDispatcher)

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    class CoroutineDispatcherProvider(testDispatcher: TestCoroutineDispatcher) :
        CoroutineDispatchers {
        override val DEFAULT: CoroutineDispatcher = testDispatcher
        override val IO: CoroutineDispatcher = testDispatcher
        override val MAIN: CoroutineDispatcher = testDispatcher
        override val UNCONFINED: CoroutineDispatcher = testDispatcher
    }
}