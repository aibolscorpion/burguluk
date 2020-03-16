package kz.shymkent.relaxhouse;

import android.content.Context;
import android.content.SharedPreferences;

import kz.shymkent.relaxhouse.application.App;

public class SharedPreferencesTools {
    static SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences(Constants.APPLICATION_NAME, Context.MODE_PRIVATE);
    static SharedPreferences.Editor editor = sharedPreferences.edit();

    public static void savePhoneNumber(String phoneNumber){
        editor.putString(Constants.PHONE_NUMBER,phoneNumber);
        editor.commit();
    }
    public static String getPhoneNumber(){
        return sharedPreferences.getString(Constants.PHONE_NUMBER,"");
    }
    public static void saveCottageName(String cottageName){
        editor.putString(Constants.COTTAGE_NAME,cottageName);
        editor.commit();
    }
    public static String getCottagename(){
        return sharedPreferences.getString(Constants.COTTAGE_NAME,"");
    }

    public static void saveAuthorized(boolean authorized){
        editor.putBoolean(Constants.AUTHORIZED,authorized);
        editor.commit();
    }
    public static Boolean getAuthorized(){
        return sharedPreferences.getBoolean(Constants.AUTHORIZED,false);
    }

}
