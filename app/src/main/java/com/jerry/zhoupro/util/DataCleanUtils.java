package com.jerry.zhoupro.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * Created by wzl-pc on 2016/3/27.
 * DataCleanUtil工具类:清理app产生的缓存信息
 */
public class DataCleanUtils {

    private static DataCleanUtils inst;
    private static String CATCH_DATABASE;
    private static String CATCH_WEBVIEW;
    private static String CATCH_WEBVIEW_DB;
    private static String CATCH_WEBVIEW_CATCH_DB;
    private Context mContext;

    private DataCleanUtils(Context context) {
        mContext = context;
        CATCH_DATABASE = "/data/data/" + context.getPackageName() + "/databases";
        CATCH_WEBVIEW = "/data/data/" + context.getPackageName() + "/app_webview";
        CATCH_WEBVIEW_DB = "/data/data/" + context.getPackageName() + "/databases/webview.db";
        CATCH_WEBVIEW_CATCH_DB = "/data/data/" + context.getPackageName() + "/databases/webviewCache.db";
    }

    public static DataCleanUtils getInstance(Context context) {
        if (inst == null) {
            synchronized (DataCleanUtils.class) {
                if (inst == null) {
                    inst = new DataCleanUtils(context);
                }
            }
        }
        return inst;
    }

    /**
     * 获取应用缓存大小
     *
     * @return
     */
    public String getCatchSize() {
        long size = FileUtils.getFolderSize(mContext.getCacheDir())//内存缓存
                + FileUtils.getFolderSize(mContext.getFilesDir())//文件缓存
                + FileUtils.getFolderSize(mContext.getExternalCacheDir())//外部缓存
                + FileUtils.getFolderSize(new File(CATCH_DATABASE))
                + FileUtils.getFolderSize(new File(CATCH_WEBVIEW))//数据库缓存
                + FileUtils.getFolderSize(new File(CATCH_WEBVIEW_DB))//数据库缓存
                + FileUtils.getFolderSize(new File(CATCH_WEBVIEW_CATCH_DB));//数据库缓存
        return FileUtils.getFormatSize(size);
    }

    /**
     * 清除本应用所有的缓存
     *
     */
    public void cleanCatch() {
        FileUtils.deleteFiles(mContext.getCacheDir());
        FileUtils.deleteFiles(mContext.getFilesDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtils.deleteFiles(mContext.getExternalCacheDir());
        }
        FileUtils.deleteFiles(new File(CATCH_DATABASE));
//        FileUtils.deleteFiles(new File("/data/data/" + mContext.getPackageName() + "/shared_prefs"));
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     *
     */
    public void cleanWebCache() {
        FileUtils.deleteFiles(new File(CATCH_WEBVIEW));
        FileUtils.deleteFiles(new File(CATCH_WEBVIEW_DB));
        FileUtils.deleteFiles(new File(CATCH_WEBVIEW_CATCH_DB));
    }

    /**
     * 按名字清除本应用数据库
     *
     * @param dbName
     */
    public void cleanDatabaseByName(String dbName) {
        mContext.deleteDatabase(dbName);
    }
}
