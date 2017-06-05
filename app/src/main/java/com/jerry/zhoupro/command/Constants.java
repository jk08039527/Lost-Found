package com.jerry.zhoupro.command;

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
    public static final String TAB_HOME = "tabHome";
    public static final String TAB_FIND = "tabFind";
    public static final String TAB_MSG = "tabMsg";
    public static final String TAB_ME = "tabMe";

    /**
     * Lost & Found
     */
    public static final int LOST = 0x1;
    public static final int FOUND = 0x2;
    public static final String PATH_ROOT_CATCH = Environment.getExternalStorageDirectory() + File.separator + "JerryPro" + File.separator;
    public static final String PATH_SETTING_CATCH = PATH_ROOT_CATCH + "catch" + File.separator;
    public static final String PATH_HEAD_PICTURE = PATH_SETTING_CATCH + "head.jpg";
    public static final String PATH_RELEASE_PIC = PATH_SETTING_CATCH + "release.jpg";
    public static final String PATH_HEAD_CATCH_PICTURE = PATH_SETTING_CATCH + "head_catch.jpg";

}
