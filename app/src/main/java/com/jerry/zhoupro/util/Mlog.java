package com.jerry.zhoupro.util;

import com.jerry.zhoupro.command.Key;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by wzl-pc on 2016/3/26.
 */
public class Mlog {

    public static String mTagPrefix = "JerryPro"; // 自定义Tag的前缀，可以是作者名
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int NONE = Log.ERROR+1;

    // 当前日志等级
    public static int LOGLEVEL = Log.VERBOSE;

    /**
     * 关闭日志
     */
    public static void closeFlog() {
        LOGLEVEL = Mlog.NONE;
    }

    /**
     * 设置日志等级.
     *
     * @param logLevel
     */
    public static void setLogLevel(int logLevel) {
        LOGLEVEL = logLevel;
    }

    /**
     * 判断某个等级日志能否被打印
     *
     */
    public static boolean isLoggable(int logLevel) {
        return (logLevel >= LOGLEVEL);
    }

    /**
     * Verbose 日志.
     *
     * @param tag
     * @param s
     */
    public static void v(String tag, String s) {
        if (Mlog.VERBOSE >= LOGLEVEL) Log.v(tag, s);
    }

    /**
     * Debug 日志.
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (Mlog.DEBUG >= LOGLEVEL) { Log.d(tag, msg); }
    }

    public static void d(String msg) {
        if (Mlog.DEBUG >= LOGLEVEL) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.d(tag, msg);
        }
    }

    /**
     * Info 日志.
     *
     * @param tag
     * @param s
     */
    public static void i(String tag, String s) {
        if (Mlog.INFO >= LOGLEVEL) Log.i(tag, s);
    }

    /**
     * Warning 日志.
     *
     * @param tag
     * @param s
     */
    public static void w(String tag, String s) {
        if (Mlog.WARN >= LOGLEVEL) Log.w(tag, s);
    }

    /**
     * Error 日志.
     *
     * @param tag
     * @param s
     */
    public static void e(String tag, String s) {
        if (Mlog.ERROR >= LOGLEVEL) Log.e(tag, s);
    }

    public static void e(String msg) {
        if (Mlog.ERROR >= LOGLEVEL) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.e(tag, msg);
        }
    }

    /**
     * Verbose 日志.
     *
     * @param tag
     * @param s
     * @param e
     */
    public static void v(String tag, String s, Throwable e) {
        if (Mlog.VERBOSE >= LOGLEVEL) Log.v(tag, s, e);
    }

    /**
     * Debug 日志.
     *
     * @param tag
     * @param s
     * @param e
     */
    public static void d(String tag, String s, Throwable e) {
        if (Mlog.DEBUG >= LOGLEVEL) Log.d(tag, s, e);
    }

    /**
     * Info 日志.
     *
     * @param tag
     * @param s
     * @param e
     */
    public static void i(String tag, String s, Throwable e) {
        if (Mlog.INFO >= LOGLEVEL) Log.i(tag, s, e);
    }

    /**
     * Warning 日志.
     *
     * @param tag
     * @param s
     * @param e
     */
    public static void w(String tag, String s, Throwable e) {
        if (Mlog.WARN >= LOGLEVEL) Log.w(tag, s, e);
    }

    /**
     * Error 日志.
     *
     * @param tag
     * @param s
     * @param e
     */
    public static void e(String tag, String s, Throwable e) {
        if (Mlog.ERROR >= LOGLEVEL) Log.e(tag, s, e);
    }

    /**
     * Verbose 格式化日志.
     *
     * @param tag
     * @param s
     * @param args
     */
    public static void v(String tag, String s, Object... args) {
        if (Mlog.VERBOSE >= LOGLEVEL) Log.v(tag, String.format(s, args));
    }

    /**
     * Debug 格式化日志.
     *
     * @param tag
     * @param s
     * @param args
     */
    public static void d(String tag, String s, Object... args) {
        if (Mlog.DEBUG >= LOGLEVEL) Log.d(tag, String.format(s, args));
    }

    /**
     * Info 格式化日志.
     *
     * @param tag
     * @param s
     * @param args
     */
    public static void i(String tag, String s, Object... args) {
        if (Mlog.INFO >= LOGLEVEL) Log.i(tag, String.format(s, args));
    }

    /**
     * Warning 格式化日志.
     *
     * @param tag
     * @param s
     * @param args
     */
    public static void w(String tag, String s, Object... args) {
        if (Mlog.WARN >= LOGLEVEL) Log.w(tag, String.format(s, args));
    }

    /**
     * Error 格式化日志.
     *
     * @param tag
     * @param s
     * @param args
     */
    public static void e(String tag, String s, Object... args) {
        if (Mlog.ERROR >= LOGLEVEL) Log.e(tag, String.format(s, args));
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s()"; // 占位符
        try {
            String callerClazzName = caller.getClassName(); // 获取到类名
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
            tag = String.format(tag, callerClazzName, caller.getMethodName()); // 替换
        } catch (IndexOutOfBoundsException e) {
            Log.d(mTagPrefix, e.toString());
            return mTagPrefix;
        }
        tag = TextUtils.isEmpty(mTagPrefix) ? tag : mTagPrefix + Key.COLON + tag;
        return tag;
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
}
