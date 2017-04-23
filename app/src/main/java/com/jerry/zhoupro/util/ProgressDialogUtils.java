package com.jerry.zhoupro.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2016/3/27.
 * ProgressDialogUtils工具类：进度条生成帮助类
 */
public class ProgressDialogUtils {

    private ProgressDialogUtils() throws IllegalAccessException {
        throw new IllegalAccessException("工具类无法实例化!");
    }

    public static ProgressDialog createProgress(Context context, String msg) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
