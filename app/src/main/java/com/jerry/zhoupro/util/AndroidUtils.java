package com.jerry.zhoupro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/27.
 * AndroidUtil工具类: 获取DeviceID，设备信息，px与dx的互换，系统版本号，应用版本号
 */
public class AndroidUtils {
    public static final String TAG = "AndroidUtil";

    /**
     * 设备唯一号存储
     */
    public static final String MOBILE_SETTING = "FY_MOBILE_SETTING";

    /**
     * 设备唯一表示
     */
    public static final String MOBILE_UUID = "FY_MOBILE_UUID";

    // 获取根目录路径
    public static String getSDPath() {
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        // 如果有sd卡，则返回sd卡的目录
        if (hasSDCard) {
            return Environment.getExternalStorageDirectory().getPath();
        } else
            // 如果没有sd卡，则返回存储目录
            return Environment.getDownloadCacheDirectory().getPath();
    }

    /**
     * 获取手机序列号
     */
    public static String getDeviceId(Context ctx) {
        String deviceID = "";
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() != null) {
            deviceID = tm.getDeviceId();
        } else {
            deviceID = getUUID(ctx);
        }
        return deviceID;
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        SharedPreferences mShare = context.getSharedPreferences(MOBILE_SETTING,
                0);
        String uuid = "";
        if (mShare != null
                && !TextUtils.isEmpty(mShare.getString(MOBILE_UUID, ""))) {
            uuid = mShare.getString(MOBILE_UUID, "");
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString(MOBILE_UUID, uuid).commit();
        }
        Mlog.d("getUUID", "getUUID : " + uuid);
        return uuid;
    }

    /**
     * 获取客户端的设备信息
     */
    public static String getClientDeviceInfo(Context ctx) {
        String deviceID = "";
        String serial = "";
        deviceID = getDeviceId(ctx);
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            Mlog.e("TAG", "get the system sn ERROR!", e);
        }
        Mlog.d("serial", "deviceID:" + deviceID);
        String buildVersion = android.os.Build.VERSION.RELEASE;
        return deviceID + "|android" + "|android|" + buildVersion + "|android";
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 取得操作系统版本号
     */
    public static String getOSVersion(Context ctx) {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取应用版本号
     *
     * @param ctx
     * @return
     */
    public static String getAPKVersion(Context ctx) {
        PackageInfo pi = null;
        try {
            pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Mlog.e(TAG, e.getMessage(), e);
        }
        return pi.versionName;
    }
}
