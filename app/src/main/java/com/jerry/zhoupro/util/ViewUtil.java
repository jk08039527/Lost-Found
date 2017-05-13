package com.jerry.zhoupro.util;

import android.text.Selection;
import android.text.Spannable;
import android.widget.EditText;

public class ViewUtil {

	private ViewUtil() {}

	/**
	 * 切换后将EditText光标置于末尾
	 */
	public static void setEditSelection(EditText edittext) {
		CharSequence mCharSequence = edittext.getText();
		if (mCharSequence != null) {
			Spannable sTv = (Spannable) mCharSequence;
			Selection.setSelection(sTv, mCharSequence.length());
		}
	}
}
