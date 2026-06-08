package com.peterclement.sabiwritersapp.ui.model
data class Booking(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val service: String = "",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)