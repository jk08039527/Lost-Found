package com.jerry.zhoupro.util;

import com.jerry.zhoupro.MyApplication;
import com.jerry.zhoupro.command.Key;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferenceUtil {

    protected PreferenceUtil() {
        throw new UnsupportedOperationException();
    }

    private static SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstances());

    public static void setPreference(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public static void setPreference(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public static void setPreference(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public static String getPreference(String key) {
        return sp.getString(key, Key.NIL);
    }
}
