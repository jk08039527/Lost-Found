package com.jerry.zhoupro;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.analytics.MobclickAgent;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by wzl-pc on 2016/3/26.
 */
public class MyApplication extends Application {
    public static MyApplication mApp;
    private static final String BMOB_ID = "6f8c415586cfef2a87f05cfdc6965346";
    private static final String UMENG_KEY = "5917b7d22ae85b614a001cd9";

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        Bmob.initialize(this, BMOB_ID);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig
                (this, UMENG_KEY, "Channel_ID", MobclickAgent.EScenarioType.E_UM_NORMAL));
    }

    public static Context getInstances() {
        return mApp;
    }
}
