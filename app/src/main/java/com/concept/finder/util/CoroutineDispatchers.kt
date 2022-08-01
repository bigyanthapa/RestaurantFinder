package com.concept.finder.util

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val DEFAULT: CoroutineDispatcher
    val IO: CoroutineDispatcher
    val MAIN: CoroutineDispatcher
    val UNCONFINED: CoroutineDispatcher
}