package com.agritech.empmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;


import com.agritech.empmanager.R;

import java.util.Calendar;

/**
 * Created by Vamsi Smart on 5/24/2018.
 */
public class PrefUtilities {

    private SharedPreferences preferences;
    Context context;


    private PrefUtilities(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }


    public static PrefUtilities with(Context context) {
        return new PrefUtilities(context);
    }


    /*
     * Add required methods
     *
     * */

    public void saveUserId(String uid) {

        preferences.edit().putString(context.getString(R.string.pref_key_user_id), uid).apply();


    }

    public String getUserId() {
        return preferences.getString(context.getString(R.string.pref_key_user_id), "");
    }


    public void saveFCMId(String fcm) {

        preferences.edit().putString(context.getString(R.string.pref_key_fcm_id), fcm).apply();


    }

    public String getFCMId() {
        return preferences.getString(context.getString(R.string.pref_key_fcm_id), "");
    }


    public String getName() {
        return preferences.getString(context.getString(R.string.pref_key_name), "");
    }


    public void saveName(String displayName) {

        preferences.edit().putString(context.getString(R.string.pref_key_name), displayName).apply();

    }
}
