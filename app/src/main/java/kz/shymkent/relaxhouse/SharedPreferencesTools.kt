package kz.shymkent.relaxhouse

import android.content.Context
import kz.shymkent.relaxhouse.application.App

object SharedPreferencesTools {
    private val sharedPreferences = App.instance.getSharedPreferences(Constants.APPLICATION_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    @JvmStatic
    fun savePhoneNumber(phoneNumber: String?) {
        editor.putString(Constants.PHONE_NUMBER, phoneNumber)
        editor.commit()
    }

    @JvmStatic
    val phoneNumber: String?
        get() = sharedPreferences.getString(Constants.PHONE_NUMBER, "")

    @JvmStatic
    fun saveCottageName(cottageName: String?) {
        editor.putString(Constants.COTTAGE_NAME, cottageName)
        editor.commit()
    }

    @JvmStatic
    val cottageName: String?
        get() = sharedPreferences.getString(Constants.COTTAGE_NAME, "")

    @JvmStatic
    fun saveAuthorized(authorized: Boolean) {
        editor.putBoolean(Constants.AUTHORIZED, authorized)
        editor.commit()
    }

    @JvmStatic
    val authorized: Boolean
        get() = sharedPreferences.getBoolean(Constants.AUTHORIZED, false)

}