package kz.shymkent.relaxhouse

import android.util.Log
import com.onesignal.OneSignal
import org.json.JSONObject
import com.onesignal.OneSignal.PostNotificationResponseHandler
import org.json.JSONException

object PushNotification {
    @JvmStatic
    fun createPushNotification(text: String) {
        try {
            OneSignal.postNotification(JSONObject("{'contents': {'en':'" + text + "'}, 'include_player_ids': [" + "'" + Constants.COTTAGE_OWNER_PLAYER_ID + "','" + Constants.COTTAGE_ADMINISTRATOR_PLAYER_ID + "','" + Constants.ANDROID_EMULATOR_PLAYER_ID + "']}"),
                object : PostNotificationResponseHandler {
                    override fun onSuccess(response: JSONObject) {
                        Log.i("OneSignalExample", "postNotification Success: $response")
                    }

                    override fun onFailure(response: JSONObject) {
                        Log.e("OneSignalExample", "postNotification Failure: $response")
                    }
                })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}