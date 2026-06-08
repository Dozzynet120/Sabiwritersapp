package com.peterclement.sabiwritersapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

/**
 * Data class representing a Booking
 */
data class Booking(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val service: String = "",
    val notes: String = ""
)

/**
 * ViewModel for handling bookings
 */
class BookingViewModel : ViewModel() {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // LiveData holding the list of all bookings
    private val _bookingState = MutableLiveData<List<Booking>>(emptyList())
    val bookingState: LiveData<List<Booking>> = _bookingState

    // LiveData for the last submitted booking (used for next screen)
    private val _lastBooking = MutableLiveData<Booking?>(null)
    val lastBooking: LiveData<Booking?> = _lastBooking

    /**
     * Add a new booking to Firestore
     *
     * @param booking The booking to add
     * @param onSuccess Lambda executed when booking is successfully added
     * @param onError Lambda executed when an error occurs
     */
    fun addBooking(
        booking: Booking,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("bookings")
            .add(booking)
            .addOnSuccessListener {
                // Update LiveData for last booking
                _lastBooking.postValue(booking)

                // Update the booking list immediately
                val updatedList = _bookingState.value.orEmpty().toMutableList()
                updatedList.add(booking)
                _bookingState.postValue(updatedList)

                onSuccess()
            }
            .addOnFailureListener { e ->
                val errorMsg = when (e) {
                    is FirebaseFirestoreException -> e.message ?: "Firestore error"
                    else -> e.localizedMessage ?: "Unknown error"
                }
                Log.e("BookingViewModel", "Failed to add booking", e)
                onError(errorMsg)
            }
    }

    /**
     * Fetch all bookings from Firestore
     */
    fun fetchBookings() {
        db.collection("bookings")
            .get()
            .addOnSuccessListener { snapshot ->
                val bookings = snapshot.documents.map { doc ->
                    Booking(
                        name = doc.getString("name") ?: "",
                        email = doc.getString("email") ?: "",
                        phone = doc.getString("phone") ?: "",
                        service = doc.getString("service") ?: "",
                        notes = doc.getString("notes") ?: ""
                    )
                }
                _bookingState.postValue(bookings)
            }
            .addOnFailureListener { e ->
                Log.e("BookingViewModel", "Failed to fetch bookings", e)
            }
    }

    /**
     * Clear the last booking (optional, if you want to reset after viewing)
     */
    fun clearLastBooking() {
        _lastBooking.postValue(null)
    }
}
