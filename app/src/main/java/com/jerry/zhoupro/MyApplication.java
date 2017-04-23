package com.jerry.zhoupro;

import com.baidu.mapapi.SDKInitializer;
import com.jerry.zhoupro.command.Constants;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

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
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }
}
