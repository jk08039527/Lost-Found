package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;

public class LoginActivity extends TitleBaseActivity{
    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleText() {
        return getString(R.string.login);
    }
}
