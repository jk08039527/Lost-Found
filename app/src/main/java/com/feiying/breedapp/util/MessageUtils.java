package com.feiying.breedapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebView;

import com.feiying.breedapp.FYLog.Flog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/27.
 * 消息提示框工具栏
 */
public class MessageUtils {
    private static String tag = "MessageUtils";
    private Handler mHandler = new Handler();
    private static Builder builder;
    //	public WebView webView;
    private static AlertDialog dialog;
    private static List<AlertDialog> dialogs = new ArrayList<AlertDialog>();

    public MessageUtils(WebView webView, Builder builder) {
        MessageUtils.builder = builder;
//		this.webView = webView;
    }

    /**
     * 确认信息选择提示框
     *
     * @param confirmStr  确认消息
     * @param callbackFun 确定后回调函数
     */
    public void confirmWin(final String confirmStr, final String callbackFun) {
        Flog.d("TAG", "confirmStr:" + confirmStr + "," + callbackFun);
        mHandler.post(new Runnable() {
            public void run() {
                builder.setMessage(confirmStr);
                builder.setTitle("提示");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }

    /**
     * 调用提示信息
     * 供webview使用
     *
     * @param str         显示的提示信息
     * @param callbackfun js回调函数名
     */
    public void alertinfo(final String str, final String callbackfun) {
        Flog.d("TAG", str + "," + callbackfun);
        mHandler.post(new Runnable() {
            public void run() {
                builder.setMessage(str);
                builder.setTitle("提示");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                builder.show();
            }
        });
    }

    // 用于屏蔽home键,和back键
    public static void showDialog(AlertDialog dialog, boolean isCatchBackKey) {
        if (isCatchBackKey) {
            showDialog(dialog);
            return;
        }
        MessageUtils.dialog = dialog;
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 用于屏蔽home键,和back键
    public static void showDialog(AlertDialog dialog) {
        MessageUtils.dialog = dialog;
        try {
            dialog.show();
            Flog.d(tag, "3进入showDialog.用于屏蔽home键,和back键.");
            Flog.d(tag, "5进入showDialog.");
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    switch (keyCode) {
                        // 屏蔽home键
                        case KeyEvent.KEYCODE_HOME:
                            Flog.d(tag, "home键已屏蔽.");
                            return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Builder builder,
                                  boolean isSetTitle, boolean isCatchHomeKey) {
        if (null == builder) {
            Flog.i(tag, "显示对话框,builder is null.");
            return;
        }
        builder.setCancelable(false);
        if (isSetTitle)
            builder.setTitle("提示");
        AlertDialog dialog = builder.create();
        dialogs.add(dialog);
        builder.show();
        // 不屏蔽home键
        if (!isCatchHomeKey)
            return;
        try {
            Flog.d(tag, "3进入showDialog.用于屏蔽home键,和back键.");
            // TYPE_KEYGUARD : 背景不是透明的
            // TYPE_KEYGUARD_DIALOG : 对话框显示不出来
            dialog.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
            Flog.d(tag, "5进入showDialog.");
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_HOME:
                            Flog.d(tag, "home键已屏蔽.");
                            return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示dialog,只有一个确定按钮
     *
     * @param ctx
     * @param message
     */
    public static void showDialogOk(Context ctx, String message) {
        dialog = new Builder(ctx).setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        showDialog(dialog);
        return;
    }

    public static void dismiss() {
        if (null != dialog && dialog.isShowing())
            dialog.dismiss();
    }

    /**
     * 可添加确定、取消相关事件弹出框
     *
     * @param c
     * @param negativeListener
     * @param positiveListener
     * @param msg
     */
    public static void showOnClickDialog(final Context c, final DialogInterface.OnClickListener negativeListener,
                                         final DialogInterface.OnClickListener positiveListener, final String msg) {
        ((Activity) c).runOnUiThread(new Runnable() {
            public void run() {
                try {
                    builder = new Builder(c);
                    builder.setCancelable(false);
                    builder.setMessage(msg);
                    builder.setTitle("提示");
                    builder.setNegativeButton("确定", negativeListener);
                    if (positiveListener != null) {
                        builder.setPositiveButton("取消", positiveListener);
                    }
                    builder.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
