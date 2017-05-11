package com.jerry.zhoupro.fragment;


import com.jerry.zhoupro.R;

import android.view.View;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class MsgFragment extends TitleBaseFragment {

    @Override
    public int getContentLayout() {
        return R.layout.fragment_msg;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.message);
    }

    @Override
    public void initView(final View view) {
        super.initView(view);
        setGone(titleBack);
        setGone(titleMore);
    }
}
