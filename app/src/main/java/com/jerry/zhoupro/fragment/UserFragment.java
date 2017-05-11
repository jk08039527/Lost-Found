package com.jerry.zhoupro.fragment;


import com.ecloud.pulltozoomview.PullToZoomRecyclerViewEx;
import com.jerry.zhoupro.R;

import android.view.View;

import butterknife.BindView;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class UserFragment extends TitleBaseFragment {


    @BindView(R.id.ptz_user)
    PullToZoomRecyclerViewEx mPtzUser;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.tab_me);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        setGone(titleBack);
        setGone(titleMore);
    }
}
