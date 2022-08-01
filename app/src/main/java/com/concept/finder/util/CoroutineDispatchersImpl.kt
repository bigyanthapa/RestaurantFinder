package com.concept.finder.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CoroutineDispatchersImpl(
    override val DEFAULT: CoroutineDispatcher = Dispatchers.Default,
    override val IO: CoroutineDispatcher= Dispatchers.IO,
    override val MAIN: CoroutineDispatcher= Dispatchers.Main,
    override val UNCONFINED: CoroutineDispatcher= Dispatchers.Unconfined
) : CoroutineDispatchers