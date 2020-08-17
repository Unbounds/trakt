package com.unbounds.trakt.utils

import java.util.concurrent.atomic.AtomicBoolean

class Event<T>(private val value: T? = null) {

    private val isConsumed = AtomicBoolean(false)

    internal fun getValue(): T? =
            if (isConsumed.compareAndSet(false, true)) value
            else null
}

fun <T> T.toEvent() = Event(this)

fun <T> Event<T>.consume(block: (T) -> Unit): T? = getValue()?.also(block)
