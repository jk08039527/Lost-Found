package com.jerry.zhoupro.fragment;


import com.jerry.zhoupro.R;

import android.view.View;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class ReleaseFragment extends TitleBaseFragment {

    @Override
    public int getContentLayout() {
        return R.layout.fragment_release;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.realese);
    }

    @Override
    public void initView(final View view) {
        super.initView(view);
        setGone(titleBack);
    }
}
