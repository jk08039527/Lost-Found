package com.feiying.breedapp;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.feiying.breedapp.command.FYConstants;

/**
 * Created by Administrator on 2016/3/26.
 */
public class FYApplication extends Application {
    public static Activity currActiviey;
    public static FYApplication mApp;
    private SharedPreferences settings = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        settings = this.getSharedPreferences(
                FYConstants.SharedPreferences_URL, 0);
    }
}
