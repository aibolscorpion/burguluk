package kz.shymkent.relaxhouse

import android.util.Log
import com.onesignal.OneSignal
import org.json.JSONObject
import com.onesignal.OneSignal.PostNotificationResponseHandler
import kz.shymkent.relaxhouse.mainActivity.MainActivityViewModel.Companion.playerIdList
import org.json.JSONException

object PushNotification {
    @JvmStatic
    fun createPushNotification(text: String) {
        try {
            OneSignal.postNotification(JSONObject("{'contents': {'en':'" + text + "'}, 'include_player_ids': [" + "'" + playerIdList[0] + "','" + playerIdList[1] + "','" + playerIdList[2] + "']}"),
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