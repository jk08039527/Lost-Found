package com.jerry.zhoupro.util;

import android.os.CountDownTimer;

/**
 * Created by wzl-pc on 2017/5/22.
 */

public class TimeTask {

    private CountDownTimer mCountDownTimer;
    private TimeOverListerner mTimeOverListerner;

    public TimeTask(int time_ms, TimeOverListerner timeOverListerner) {
        mCountDownTimer = new CountDownTimer(time_ms, time_ms) {

            @Override
            public void onTick(final long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (mTimeOverListerner != null) { mTimeOverListerner.onFinished(); }
                this.cancel();
            }
        };
        mTimeOverListerner = timeOverListerner;
    }

    public void start() {
        if (mCountDownTimer != null) { mCountDownTimer.start(); }
    }

    public void cancel() {
        if (mCountDownTimer != null) { mCountDownTimer.cancel(); }
    }

    public interface TimeOverListerner {

        void onFinished();
    }
}
