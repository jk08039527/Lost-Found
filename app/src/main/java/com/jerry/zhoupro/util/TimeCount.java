package com.jerry.zhoupro.util;

import android.os.CountDownTimer;

/**
 * @author wzl 2015-7-30 类说明：倒计时工具
 */
public class TimeCount extends CountDownTimer {

	private CountOver mCountOver;

	public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	public TimeCount(long millisInFuture, long countDownInterval, CountOver countOver) {
		super(millisInFuture, countDownInterval);
		this.mCountOver = countOver;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (mCountOver != null) {
			mCountOver.onCountTick(millisUntilFinished);
		}
	}

	@Override
	public void onFinish() {
		this.cancel();
		if (mCountOver != null) {
			mCountOver.onCountOver(true);
		}
	}

	public void setOnCountOverListener(CountOver countOver) {
		this.mCountOver = countOver;
	}

	/**
	 * 倒计时回调接口
	 */
	public interface CountOver {
		void onCountOver(boolean isFinish);

		void onCountTick(long millisUntilFinished);
	}

	public static class SimpleCountOverListener implements CountOver {

		@Override
		public void onCountOver(boolean isFinish) {

		}

		@Override
		public void onCountTick(long millisUntilFinished) {

		}
	}

}
