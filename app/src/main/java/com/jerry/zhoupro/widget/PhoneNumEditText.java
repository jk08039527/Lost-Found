package com.jerry.zhoupro.widget;

import java.util.regex.Pattern;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.listener.MyTextWatcherListener;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

/**
 * Created by th on 16/8/15. 类说明: 银行卡号/手机号码输入框
 */
public class PhoneNumEditText extends MyEditText {

	public static int numType; // 0: phone    1: bank
	public static int selectionNum = 3;
	public static int spaceNum = 2;

	private static final String patternPhone1 = "^(\\d{7}) (/\\d{4})$";// 1835723 7923
	private static final String patternPhone2 = "^(\\d{3}) (\\d{8})$";// 183 57127923

	private static final String patternBank1 = "^([\\d]{4}[\\s])([\\d]{4}[\\s])*([\\d]{7})$";
	private static final String patternBank2 = "^([\\d]{4}[\\s])([\\d]{4}[\\s])*([\\d]{8})$";
	private static final String patternBank3 = "^([\\d]{8}[\\s])([\\d]{4}[\\s])*([\\d]{4})$";
	private static final String patternBank4 = "^([\\d]{8}[\\s])([\\d]{4}[\\s])*([\\d]{3})$";

	public PhoneNumEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (isInEditMode()) {
			return;
		}

		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhoneNumEditText);

			if (typedArray != null) {
				numType = typedArray.getInt(R.styleable.PhoneNumEditText_type, 0);
				typedArray.recycle();
			}
		}

		init();
	}

	private void init() {
		if (numType == 1) {
			selectionNum = 4;
			spaceNum = 1;
		} else {
			selectionNum = 3;
			spaceNum = 2;
		}
		setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		addTextChangedListener(new MyTextWatcherListener() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str = s.toString().replace(Key.SPACE, Key.NIL);
				boolean deleteEmpty = deleteOrNot(numType, s.toString());

				if (deleteEmpty && before == 1) {
					start = start - 1;
					s = s.toString().substring(0, start) + s.toString().substring(start + 1);
					str = s.toString().replace(Key.SPACE, Key.NIL);
				}

				String endStr = Key.NIL;
				int len = str.length();
				for (int i = 0; i < len; i++) {
					endStr += str.charAt(i);
					if ((i + spaceNum) % 4 == 0 && (i + 1) != len) {
						endStr += Key.SPACE;
					}
				}

				if (endStr.endsWith(Key.SPACE)) {
					endStr = endStr.substring(0, endStr.lastIndexOf(Key.SPACE));
				}

				removeTextChangedListener(this);
				setText(endStr);
				addTextChangedListener(this);

				// 计算光标位置
				if (count == 0) {// 删除
					int selValue = start - (deleteEmpty ? 1 : 0) - (s.length() - endStr.length());
					setSelection(selValue > 0 ? selValue : 0);
				} else if (count == 1) {// 输入一个数字
					if ((start - selectionNum) % 5 == 0) {
						start++;
					}
					setSelection(start + count);
				} else if (count > 1) {// 输入多个数字
					setSelection(getText().toString().length());
				}
			}
		});
	}

	private boolean deleteOrNot(final int numType, final String s) {
		if (numType == 0) {
			return Pattern.compile(patternPhone1).matcher(s).find()
					|| Pattern.compile(patternPhone2).matcher(s).find();
		} else if (numType == 1) {
			return Pattern.compile(patternBank1).matcher(s).find()
					|| Pattern.compile(patternBank2).matcher(s).find()
					|| Pattern.compile(patternBank3).matcher(s).find()
					|| Pattern.compile(patternBank4).matcher(s).find();
		}
		return false;
	}
}
