package com.jerry.zhoupro;

import com.baidu.mapapi.SDKInitializer;
import com.jerry.zhoupro.command.Constants;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/3/26.
 */
public class MyApplication extends Application {
    public static Activity currActiviey;
    public static MyApplication mApp;
    private SharedPreferences settings = null;
    private static final String BMOB_ID = "6f8c415586cfef2a87f05cfdc6965346";

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        settings = this.getSharedPreferences(Constants.SharedPreferences_URL, 0);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        Bmob.initialize(this, BMOB_ID);
    }

    public static Context getInstances() {
        return mApp;
    }
}
