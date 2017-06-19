package com.jerry.zhoupro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Created by wzl-pc on 2017/4/21 类说明：spannable string tool
 */

public final class SpannableStringUtils {

	private SpannableStringUtils() {
	}

	public static class Builder {

		private static final int DEFAULT_FOREGROUND_COLOR = 0x333333;
		private SpannableStringBuilder mBuilder;
		private CharSequence text;
		private ClickableSpan clickSpan;

		private int flag;
		@ColorInt
		private int foregroundColor;
		private int size;

		private int start;
		private int end;

		public Builder(CharSequence text) {
			this.text = text;
			flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
			foregroundColor = DEFAULT_FOREGROUND_COLOR;
			mBuilder = new SpannableStringBuilder();
		}

		/**
		 * 设置标识
		 *
		 * @param flag
		 *            <ul>
		 *            <li>{@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}</li>
		 *            <li>{@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}</li>
		 *            <li>{@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}</li>
		 *            <li>{@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}</li>
		 *            </ul>
		 */
		public Builder setFlag(final int flag) {
			this.flag = flag;
			return this;
		}

		/**
		 * 设置前景色
		 */
		public Builder setForegroundColor(@ColorInt int color) {
			this.foregroundColor = color;
			return this;
		}

		public Builder setStartEnd(int start, int end) {
			this.start = start;
			this.end = end;
			return this;
		}

		public Builder setSize(final int size) {
			this.size = size;
			return this;
		}

		public Builder setClickSpan(ClickableSpan clickSpan) {
			this.clickSpan = clickSpan;
			return this;
		}

		/**
		 * 追加样式
		 */
		public Builder append(@NonNull CharSequence text) {
			setSpans();
			this.text = text;
			return this;
		}

		public Builder append(int text) {
			setSpans();
			this.text = String.valueOf(text);
			return this;
		}

		public Builder append(float text) {
			setSpans();
			this.text = String.valueOf(text);
			return this;
		}

		public SpannableStringBuilder build() {
			setSpans();
			return mBuilder;
		}

		private void setSpans() {
			if (start == 0 && end == 0) {
				start = mBuilder.length();
				mBuilder.append(this.text);
				end = mBuilder.length();
			} else {
				mBuilder.append(this.text);
			}

			if (foregroundColor != DEFAULT_FOREGROUND_COLOR) {
				mBuilder.setSpan(new ForegroundColorSpan(foregroundColor), start, end, flag);
				foregroundColor = DEFAULT_FOREGROUND_COLOR;
			}

			if (size != 0) {
				mBuilder.setSpan(new AbsoluteSizeSpan(size), start, end, flag);
				size = 0;
			}

			if (clickSpan != null) {
				mBuilder.setSpan(clickSpan, start, end, flag);
				clickSpan = null;
			}
			flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
			start = 0;
			end = 0;
		}

		@Override
		public String toString() {
			return mBuilder.toString();
		}
	}

	/**
	 * 
	 * @param originStr
	 *            字符源
	 * @param keyWord
	 *            需要改变颜色的字符
	 * @param highLightColor
	 *            高亮的颜色
	 */
	public static CharSequence setSpan(String originStr, String keyWord, @ColorInt int highLightColor) {
		SpannableString ss = new SpannableString(originStr);
		Pattern p = Pattern.compile(keyWord);
		Matcher m = p.matcher(ss);
		while (m.find()) {
			ss.setSpan(new ForegroundColorSpan(highLightColor), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}
}
