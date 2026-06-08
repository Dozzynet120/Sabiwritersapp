package com.peterclement.sabiwritersapp

import android.app.Application
import com.google.firebase.FirebaseApp

class SabiWritersApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
