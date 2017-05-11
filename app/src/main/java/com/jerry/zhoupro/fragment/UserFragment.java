package com.jerry.zhoupro.fragment;


import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jerry.zhoupro.R;

import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class UserFragment extends TitleBaseFragment {


    @BindView(R.id.ptz_user)
    PullToZoomScrollViewEx mPtzUser;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.me);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        setGone(titleBack);
        setGone(titleMore);
        loadViewForCode();
    }

    private void loadViewForCode() {
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.profile_head_view, null, false);//头部扩展view
        View zoomView = LayoutInflater.from(getContext()).inflate(R.layout.profile_zoom_view, null, false);//拉伸背景view
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.profile_content_view, null, false);
        mPtzUser.setHeaderView(headView);
        mPtzUser.setZoomView(zoomView);
        mPtzUser.setScrollContentView(contentView);
    }
}
