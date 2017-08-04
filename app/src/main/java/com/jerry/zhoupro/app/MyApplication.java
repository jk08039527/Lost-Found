package com.jerry.zhoupro.app;

import com.baidu.mapapi.SDKInitializer;
import com.jerry.zhoupro.util.Mlog;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
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
        PlatformConfig.setQQZone("1106223214", "nF9ambER3c9bIYEW");
        PlatformConfig.setSinaWeibo("1652481113", "be2af8b9f9af661dc848acb17facbd63", "http://sns.whalecloud.com");
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

        SophixManager.getInstance().setContext(this)
                .setAppVersion("1.0")
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Mlog.d("补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            Mlog.d("生效需要重启");
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            SophixManager.getInstance().cleanPatches();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Mlog.d("其它错误信息");
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    public static Context getInstance() {
        return mApp;
    }
}
