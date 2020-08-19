package com.unbounds.trakt.utils

fun <T> List<T>.replaceOrAdd(newValue: T, block: (T) -> Boolean) = if (any { block(it) }) map {
    if (block(it)) newValue else it
} else plus(newValue)

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean) = map {
    if (block(it)) newValue else it
}
