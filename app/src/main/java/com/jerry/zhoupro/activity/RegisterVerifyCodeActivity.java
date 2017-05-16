package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.listener.MyTextWatcherListener;
import com.jerry.zhoupro.util.TimeCount;
import com.jerry.zhoupro.util.WeakHandler;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterVerifyCodeActivity extends TitleBaseActivity implements TimeCount.CountOver {

    @BindView(R.id.phone_num_text)
    TextView mPhoneNumText;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.cal_tv)
    TextView mCalTv;
    @BindView(R.id.button_commit)
    TextView mButtonCommit;
    private String mPhone;
    private TimeCount mCountDownTimer;
    private boolean isSMSLogin;

    @Override
    protected String getTitleText() {
        return getString(R.string.input_verify_code);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register_verify_code;
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        mPhone = getIntent().getStringExtra(Key.phone);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(mPhone)) {
            mPhoneNumText.setText(getString(R.string.input_phone_captcha, mPhone));
        }
        mEditCode.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                mButtonCommit.setEnabled(!TextUtils.isEmpty(mEditCode.getText()) && TextUtils.getTrimmedLength(mEditCode.getText()) > 3);
            }
        });

        countDown();

    }

    @OnClick({R.id.cal_tv, R.id.button_commit})
    public void onClick(final View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.cal_tv:
                toast(R.string.cacha_has_send);
                break;
            case R.id.button_commit:
                checkVerifyCode();
                break;
        }
    }

    private void checkVerifyCode() {
        String code = mEditCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            toast(R.string.captcha_not_null);
            return;
        }
        loadingDialog();
        mButtonCommit.setText(getString(R.string.wait_a_moment));
        mButtonCommit.setEnabled(false);
        //模拟请求网络
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadingDialog();
                mButtonCommit.setText(getString(R.string.ok));
                mButtonCommit.setEnabled(true);
                //登录流程
                Intent intent = new Intent();
                intent.putExtra(Key.has_register, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }, 800);
    }

    private void countDown() {
        mCountDownTimer = new TimeCount(60000, 1000, this);
        mCountDownTimer.start();
    }

    @Override
    public void onCountOver(final boolean isFinish) {
        mCalTv.setText(R.string.obtain_again);
        mCalTv.setEnabled(true);
        mCountDownTimer = null;
    }

    @Override
    public void onCountTick(final long millisUntilFinished) {
        mCalTv.setText(getString(R.string.remain_seconds, millisUntilFinished / 1000));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}
