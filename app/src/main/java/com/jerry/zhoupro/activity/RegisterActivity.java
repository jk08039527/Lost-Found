package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.listener.MyTextWatcherListener;
import com.jerry.zhoupro.util.PatternsUtil;
import com.jerry.zhoupro.util.ViewUtil;
import com.jerry.zhoupro.widget.AlertIosDialog;
import com.jerry.zhoupro.widget.MyEditText;
import com.jerry.zhoupro.widget.PhoneNumEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends TitleBaseActivity {

    @BindView(R.id.et_account)
    PhoneNumEditText mAccountEt;
    @BindView(R.id.et_passwd)
    MyEditText mPasswdEt;
    @BindView(R.id.iv_pwd_show)
    ImageView mIvPwdShow;
    @BindView(R.id.tv_register)
    TextView mRegisterTv;
    private String mPhone, mPwd;
    private boolean isHidden;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.register);
    }

    @Override
    protected void beforeViews() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        mPhone = bundle.getString(Key.phone);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(mPhone)) {
            mAccountEt.setText(mPhone);
        }
        mAccountEt.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                mRegisterTv.setEnabled(mAccountEt.getText().toString().length() == 13 && mPasswdEt.getText().toString().length() > 5);
            }
        });
        mPasswdEt.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                mRegisterTv.setEnabled(mAccountEt.getText().toString().length() == 13 && mPasswdEt.getText().toString().length() > 5);
            }
        });
    }

    @OnClick({R.id.iv_pwd_show, R.id.tv_register})
    public void onClick(final View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_pwd_show:
                if (isHidden) {
                    mPasswdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvPwdShow.setImageResource(R.mipmap.eye_hide);
                } else {
                    mPasswdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvPwdShow.setImageResource(R.mipmap.eye_show);
                }
                isHidden = !isHidden;
                ViewUtil.setEditSelection(mPasswdEt);
                break;
            case R.id.tv_register:
                mPhone = mAccountEt.getText().toString().trim();
                mPhone = mPhone.replaceAll(Key.SPACE, Key.NIL);// 去除空格
                if (TextUtils.isEmpty(mPhone) && mPhone.length() != 11) {
                    toast(R.string.input_phone);
                    return;
                }
                mPwd = mPasswdEt.getText().toString().trim();
                if (TextUtils.isEmpty(mPwd)) {
                    toast(R.string.input_pwd);
                    return;
                }
                if (PatternsUtil.isChinese(mPwd)) {
                    toast(R.string.do_not_contains_chinese_in_pwd);
                    return;
                }
                if (PatternsUtil.passwordGrade(mPwd) < 15) {
                    toast(R.string.pwd_too_simple);
                    return;
                }
                final AlertIosDialog iosDialog = new AlertIosDialog(this);
                iosDialog.setTitleText(R.string.confirm_phone_num);
                iosDialog.setMessage(getString(R.string.send_captcha_to_phone) + '\n' + mPhone);
                iosDialog.setPositiveButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iosDialog.dismiss();
                    }
                });
                iosDialog.show();
                break;
        }
    }
}
