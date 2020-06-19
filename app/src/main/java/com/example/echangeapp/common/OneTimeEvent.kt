package com.example.echangeapp.common

class OneTimeEvent<T>(private val _value: T) {

    private var processed: Boolean = false

    val value: T?
        get() {
            if (!processed) {
                processed = true
                return _value
            }
            return null
        }
}