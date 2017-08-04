package com.jerry.zhoupro.app;

import java.io.File;

import android.os.Environment;

/**
 * Created by wzl-pc on 2016/4/4.
 * 初始化设置类
 */
public class Constants {
    public static String SharedPreferences_URL="SETTING_INFOS";

    /**
     * Bottom Tab shift
     */
    public static final int TAB_HOME = 0;
    public static final int TAB_FIND = 1;
    public static final int TAB_MSG = 2;
    public static final int TAB_ME = 3;

    /**
     * Lost & Found
     */
    public static final int LOST = 0;
    public static final int FOUND = 1;
    public static final String PATH_ROOT_CATCH = Environment.getExternalStorageDirectory() + File.separator + "JerryPro" + File.separator;
    public static final String PATH_SETTING_CATCH = PATH_ROOT_CATCH + "catch" + File.separator;

}
