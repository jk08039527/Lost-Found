package com.jerry.zhoupro;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.jerry.zhoupro.command.Constants;

/**
 * Created by Administrator on 2016/3/26.
 */
public class MyApplication extends Application {
    public static Activity currActiviey;
    public static MyApplication mApp;
    private SharedPreferences settings = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        settings = this.getSharedPreferences(Constants.SharedPreferences_URL, 0);
    }
}
