package com.jerry.zhoupro.util;

import android.os.CountDownTimer;

/**
 * @author wzl 2015-7-30 类说明：倒计时工具
 */
public class TimeTask extends CountDownTimer {

	private CountOver mCountOver;

	public TimeTask(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	public TimeTask(long millisInFuture, long countDownInterval, CountOver countOver) {
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

	public static class OverDo {

		private CountDownTimer mCountDownTimer;

		public OverDo(int time_ms, final TimeOverListerner timeOverListerner) {
			mCountDownTimer = new CountDownTimer(time_ms, time_ms) {

				@Override
				public void onTick(final long millisUntilFinished) {

				}

				@Override
				public void onFinish() {
					if (timeOverListerner != null) { timeOverListerner.onFinished(); }
					if (mCountDownTimer != null) { mCountDownTimer.cancel(); }
				}
			}.start();
		}
	}

	public interface TimeOverListerner {

		void onFinished();
	}

	/**
	 * 倒计时回调接口
	 */
	public interface CountOver {

		void onCountOver(boolean isFinish);

		void onCountTick(long millisUntilFinished);
	}
}
