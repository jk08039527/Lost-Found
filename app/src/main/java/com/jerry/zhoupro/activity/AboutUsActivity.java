package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;

public class AboutUsActivity extends TitleBaseActivity {

    @Override
    protected String getTitleText() {
        return getString(R.string.about_us);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {

    }
}
