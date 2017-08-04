package com.jerry.zhoupro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jerry.zhoupro.app.Key;

import android.text.TextUtils;

public class PatternsUtil {

	private PatternsUtil() {}

	public static boolean check(String str, String regex) {
		boolean flag;
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 邮箱格式简单判断
	 */
	public static boolean isEmail(String paramString) {
		return !TextUtils.isEmpty(paramString) && Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$").matcher(paramString).matches();
	}

	/**
	 * 验证是否是正确的手机号码 ,不包含座机
	 */
	public static boolean isTelPhone(String paramString) {
		return paramString != null && Pattern.compile("[1]\\d{10}$").matcher(paramString).matches();
	}

	/**
	 * 验证是不是整数
	 */
	public static boolean isInteger(String paramString) {
		return !TextUtils.isEmpty(paramString) && Pattern.compile("^-?\\d+$").matcher(paramString).matches();
	}

	/**
	 * 判断是否是qq号码
	 */
	public static boolean isQQ(String QQ) {
		String regex = "[1-9][0-9]{4,13}";
		return check(QQ, regex);
	}

	/**
	 * 密码强度验证,返回整数
	 */
	public static int passwordGrade(String pwd) {
		int score = 0;
		String[] regexArr = { "[0-9]", "[a-z]", "[A-Z]", "[\\W_]" };
		int repeatCount = 0;
		String prevChar = "";

		// check length
		int len = pwd.length();
		score += len > 18 ? 18 : len;
		// check type
		for (int i = 0, num = regexArr.length; i < num; i++) {
			Pattern pattern = Pattern.compile(regexArr[i]);
			Matcher matcher = pattern.matcher(pwd);
			if (matcher.find()) {
				score += 4;
			}
		}

		String tag = "PatternsUtil";

		// bonus point
		for (int i = 0, num = regexArr.length; i < num; i++) {

			Pattern pattern = Pattern.compile(regexArr[i]);
			Matcher matcher = pattern.matcher(pwd);
			int number = 0;
			while (matcher.find()) {
				number++;
			}
			if (number >= 2) {
				score += 2;
			}

			if (number >= 5) {
				score += 2;
			}
		}

		// deduction
		for (int i = 0, num = pwd.length(); i < num; i++) {

			if (String.valueOf(pwd.charAt(i)).equals(prevChar))
				repeatCount++;
			else
				prevChar = String.valueOf(pwd.charAt(i));
		}
		score -= repeatCount;

		return score;
	}

	/**
	 * 判断String中是否包含中文
	 */
	public static boolean isChinese(String string) {
		if (null == string) {
			return false;
		}
		Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
		for (int i = 0; i < string.length(); i++) {
			Matcher m = p.matcher(string.charAt(i) + Key.NIL);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 过滤掉中文
	 */
	public static String filterChinese(String string) {
		if (null == string) {
			return Key.NIL;
		}
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
		Matcher m = p.matcher(string);
		return m.replaceAll(Key.NIL);
	}

	/**
	 * 判断字符串是否为数字
	 *
	 * @param str
	 *            需要判断的字符串
	 * @return true:是字符串；false:不是字符串
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 判断字符串是否为正整数或正小数
	 *
	 * @param str
	 *            需要判断的字符串
	 * @return true:是字符串；false:不是字符串
	 */
	public static boolean isNumOrFloat(String str) {
		Pattern pattern = Pattern.compile("^\\d+$|^\\d+\\.\\d+$");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
}
