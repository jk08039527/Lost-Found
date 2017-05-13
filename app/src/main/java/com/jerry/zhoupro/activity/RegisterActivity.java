package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.widget.MyEditText;
import com.jerry.zhoupro.widget.PhoneNumEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import butterknife.BindView;

public class RegisterActivity extends TitleBaseActivity {

    @BindView(R.id.account_et)
    PhoneNumEditText mAccountEt;
    @BindView(R.id.passwd_et)
    MyEditText mPasswdEt;
    @BindView(R.id.register_tv)
    TextView mRegisterTv;
    private String mPhone, mPwd;

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
    }
}
