package com.jerry.zhoupro.presenter.user;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.TitleBaseActivity;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.presenter.listener.MyTextWatcherListener;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.util.TimeTask;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterVerifyCodePresenter extends TitleBaseActivity implements TimeTask.CountOver {

    @BindView(R.id.phone_num_text)
    TextView mPhoneNumText;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.cal_tv)
    TextView mCalTv;
    @BindView(R.id.button_commit)
    TextView mButtonCommit;
    private String mPhone;
    private TimeTask mCountDownTimer;

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
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.cal_tv:
                loadingDialog();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sendTime = format.format(new Date());
                BmobSMS.requestSMSCode(mPhone, sendTime, new QueryListener<Integer>() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        if (ex != null) {
                            Mlog.e(ex.toString());
                            return;
                        }
                        toast(R.string.cacha_has_send);
                        closeLoadingDialog();
                        countDown();
                    }
                });
                break;
            case R.id.button_commit:
                checkVerifyCode();
                break;
            default:
                super.onClick(view);
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
        BmobSMS.verifySmsCode(mPhone, code, new UpdateListener() {
            @Override
            public void done(final BmobException e) {
                if (e != null) {//短信验证码已验证成功
                    toast(R.string.verify_fail);
                } else {
                    closeLoadingDialog();
                    mButtonCommit.setText(getString(R.string.ok));
                    mButtonCommit.setEnabled(true);
                    //登录流程
                    Intent intent = new Intent();
                    intent.putExtra(Key.has_register, true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void countDown() {
        mCountDownTimer = new TimeTask(60000, 1000, this);
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
