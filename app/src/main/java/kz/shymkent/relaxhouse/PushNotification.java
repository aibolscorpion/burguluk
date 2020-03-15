package kz.shymkent.relaxhouse;

import android.util.Log;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class PushNotification  {
    public static void createPushNotification(String text){
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'"+text+"'}, 'include_player_ids': [" + "'"+ Constants.COTTAGE_OWNER_PLAYER_ID+"','"+Constants.COTTAGE_ADMINISTRATOR_PLAYER_ID+"','"+Constants.COTTAGE_CLEANER_PLAYER_ID+"']}"),
                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("OneSignalExample", "postNotification Success: " + response.toString());
                        }
                        @Override
                        public void onFailure(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
