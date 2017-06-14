package com.jerry.zhoupro.util;

import java.util.Locale;

import com.jerry.zhoupro.MyApplication;
import com.jerry.zhoupro.command.Key;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferenceUtil {

    public static final String FIRST_INSTALLED = "first_installed";
    public static final String FIRST_OPEN_FOR_VERSION = "first_open_for_version";
    public static final String FIRST_SET_MISSING_DATA = "first_set_missing_data";

    protected PreferenceUtil() {
        throw new UnsupportedOperationException();
    }

    private static SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());

    /**
     * 判断是否第一次
     */
    public static boolean isFirst(String key) {
        return sp.getBoolean(key, true);
    }

    public static boolean isFirst(String key, String tag) {
        return isFirst(String.format("%s%s", key, tag));
    }

    public static boolean isFirst(String key, int tag) {
        return isFirst(String.format(Locale.getDefault(), "%s%d", key, tag));
    }

    /**
     * 将key设置为不是第一次
     */
    public static void setNotFirst(String key) {
        setPreference(key, false);
    }

    /**
     * 需要区分key的
     *
     * @param tag
     */
    public static void setNotFirst(String key, String tag) {
        setNotFirst(String.format("%s%s", key, tag));
    }

    public static void setNotFirst(String key, int tag) {
        setNotFirst(String.format(Locale.getDefault(), "%s%d", key, tag));
    }


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
