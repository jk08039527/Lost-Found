package com.jerry.zhoupro.util;

import java.lang.reflect.Method;

import com.jerry.zhoupro.MyApplication;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.SplashActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

/**
 * Created by wzl-pc on 2016/3/27.
 * AndroidUtil工具类: 获取DeviceID，设备信息，px与dx的互换，系统版本号，应用版本号
 */
public class AndroidUtils {
    public static final String TAG = "AndroidUtil";
    private static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

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
     * 获取客户端的设备信息
     */
    public static String getClientDeviceInfo(Context ctx) {
        String deviceID = "";
        String serial = "";
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();
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
     * 取得操作系统版本号
     */
    public static String getOSVersion(Context ctx) {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取sdk版本号
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取应用版本号
     */
    public static int getVersionCode() {
        Context context = MyApplication.getInstances().getApplicationContext();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 添加桌面快捷方式
     */
    public static void addAppShortcutToDesktop(Context context) {
        /*if (filterNoShortcut(context)) {
            return;
		}*/

        String title = context.getString(R.string.app_name);// 名称

        delLauncherIcon(context, SplashActivity.class, title);// for v2.0

        if (hasShortcut(context, title)) {// 桌面已经存在快捷方式
            Mlog.d("shortcut", "桌面快捷方式已经存在--------");
            return;
        }

        Mlog.d("shortcut", "桌面快捷方式不存在");
        Intent addShortCut = new Intent(ACTION_ADD_SHORTCUT);
        Intent intent = new Intent(Intent.ACTION_MAIN);

        ComponentName comp = new ComponentName(context, SplashActivity.class);
        intent.setComponent(comp);

        // 设置快捷方式的标题
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        addShortCut.putExtra("duplicate", false);
        // 设置快捷方式的图标
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        // 设置快捷方式对应的Intent
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播添加快捷方式
        context.sendBroadcast(addShortCut);
    }

    /**
     * 第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
     */
    private static void delLauncherIcon(Context context, Class<?> cls, String name) {
        Mlog.d("shortcut", "删除桌面快捷方式");
        Intent shortcut = new Intent(ACTION_UNINSTALL_SHORTCUT);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        ComponentName comp = new ComponentName(context, cls);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        context.sendBroadcast(shortcut);
    }

    /**
     * 根据 title 判断快捷方式是否存在；尚未考虑非原生桌面
     */
    private static boolean hasShortcut(Context context, String title) {
        boolean result = false;

        String uriString;
        if (getSdkVersion() < Build.VERSION_CODES.KITKAT) {
            uriString = "content://com.android.launcher2.settings/favorites?notify=true";
        } else {
            uriString = "content://com.android.launcher3.settings/favorites?notify=true";
        }
        Cursor c = null;
        try {
            final Uri CONTENT_URI = Uri.parse(uriString);
            c = context.getContentResolver().query(CONTENT_URI, null, "title=?", new String[]{title}, null);
            if (c != null && c.getCount() > 0) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return result;
    }
}
