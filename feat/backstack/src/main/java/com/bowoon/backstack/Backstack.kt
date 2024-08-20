package com.bowoon.backstack

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class Backstack @AssistedInject constructor(
    @Assisted("startId") private val startId: Int
) {
    @AssistedFactory
    interface BackstackFactory {
        fun create(@Assisted("startId") startId: Int): Backstack
    }

    private val backstack = mutableListOf<Int>(startId)

    fun add(id: Int) {
        if (backstack.contains(id)) {
            backstack.remove(id)
            backstack.add(id)
        }

        if (backstack.isEmpty() || peek() != id) {
            backstack.add(id)
        }
    }

    fun peek(): Int = backstack.last()

    fun remove() {
        backstack.removeLast()
    }

    fun clear() {
        backstack.clear()
    }

    fun size(): Int = backstack.size

    fun isEmpty(): Boolean = backstack.size == 1

    fun getStartId(): Int = startId
}