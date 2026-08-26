package com.saltech.urdocs.util

object NativeBuffer {
    private val store = mutableMapOf<String, String>()

    fun set(key: String, value: String) {
        store[key] = value
    }

    fun get(key: String): String? {
        return store[key]
    }
}
