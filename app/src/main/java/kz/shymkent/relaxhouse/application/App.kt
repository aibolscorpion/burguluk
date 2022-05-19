package kz.shymkent.relaxhouse.application

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import com.onesignal.OneSignal
import kz.shymkent.relaxhouse.Constants.ONESIGNAL_APP_ID

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

    }

    companion object {
        @get:Synchronized
        lateinit var instance: App
    }

    init {
        instance = this
    }
}