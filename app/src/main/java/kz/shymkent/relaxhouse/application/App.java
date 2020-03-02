package kz.shymkent.relaxhouse.application;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.onesignal.OneSignal;

public class App extends Application {
    public static App mInstance;
    public App(){
        mInstance = this;
    }
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        AndroidThreeTen.init(this);
    }
    public static synchronized App getInstance(){
        return mInstance;
    }
}
