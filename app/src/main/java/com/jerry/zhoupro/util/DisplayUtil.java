package com.jerry.zhoupro.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.view.View;

public class DisplayUtil {

	private DisplayUtil() {
    }

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(double dipValue) {
		return (int) (dipValue * getDisplayDensity() + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(double pxValue) {
		return (int) (pxValue / getDisplayDensity() + 0.5f);
	}

	/**
	 * 获取手机屏幕的像素高
	 */
	public static int getDisplayHeight() {
		return Resources.getSystem().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取手机屏幕的像素宽
	 */
	public static int getDisplayWidth() {
		return Resources.getSystem().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕密度
	 */
	public static float getDisplayDensity() {
		return Resources.getSystem().getDisplayMetrics().density;
	}

	/**
	 * 获取手机状态栏的高度
	 */
	public static int getStatusBarHeight(Context context) {
		int statusHeight;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	/**
	 * 获得View在屏幕的位置
	 */
	public static Rect getRectOnScreen(View v) {
		if (v == null) {
			return new Rect();
		}
		int[] point = new int[2];
		v.getLocationOnScreen(point);
		return new Rect(point[0], point[1], point[0] + v.getWidth(), point[1] + v.getHeight());
	}

	/**
	 * 获取dimen的像素值
	 */
	public static int getDimensionPixelSize(Context context, @DimenRes int dimenId) {
		return context.getResources().getDimensionPixelSize(dimenId);
	}
}
