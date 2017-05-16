package com.jerry.zhoupro.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by wzl on 05/04/2017
 *
 * @description soft input method utils
 */

public class ImeUtils {

    private ImeUtils() {}

    /**
     * 弹出软键盘
     */
    public static void showIme(View view) {
        view.requestFocus();
        if (view.getContext() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean hideIme(Activity activity) {
        if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null
                || activity.getWindow().getDecorView().getWindowToken() == null) {
            return false;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * 输入法管理内存泄漏
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        try {
            for (String param : arr) {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (null != obj_get && obj_get instanceof View) {
                    if (((View) obj_get).getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * hide ime when click outside
     * only call in {@link Activity#dispatchTouchEvent(MotionEvent)} recently
     */
    public static boolean hideImeOutside(Activity activity, MotionEvent ev) {
        View v = activity.getCurrentFocus();

        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText) {
            int[] locations = new int[2];
            v.getLocationOnScreen(locations);
            float x = ev.getRawX() + v.getLeft() - locations[0];
            float y = ev.getRawY() + v.getTop() - locations[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                boolean b = hideIme(activity);
                v.clearFocus();
                return b;
            }
        }
        return false;
    }
}
