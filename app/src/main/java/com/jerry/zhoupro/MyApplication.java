package com.jerry.zhoupro;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

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

    static {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        UMShareAPI.get(this);
        Bmob.initialize(this, BMOB_ID);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig
                (this, UMENG_KEY, "Channel_ID", MobclickAgent.EScenarioType.E_UM_NORMAL));
    }

    public static Context getInstances() {
        return mApp;
    }
}
