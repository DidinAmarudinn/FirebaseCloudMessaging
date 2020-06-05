package com.example.demo_fcm;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME="fcmsharedpref";
    private static final String KEY_ACCES_TOKEN="token";

    private static Context context;
    private static SharedPrefManager preferences;

    public SharedPrefManager(Context context) {
     this.context=context;
    }
    public static synchronized SharedPrefManager sharedPrefManager(Context context){
        if (preferences==null)
            preferences=new SharedPrefManager(context);
        return preferences;
    }
    public boolean storeToken(String token){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_ACCES_TOKEN,token);
        editor.apply();
        return true;
    }
    public String getToken(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCES_TOKEN,null);
    }

}
