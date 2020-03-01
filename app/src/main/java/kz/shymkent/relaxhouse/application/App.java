package kz.shymkent.relaxhouse.application;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class App extends Application {
    public void onCreate(){
        super.onCreate();

        AndroidThreeTen.init(this);
    }
}
