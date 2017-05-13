package com.jerry.zhoupro.fragment;


import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.LoginActivity;
import com.jerry.zhoupro.activity.RegisterActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class UserFragment extends TitleBaseFragment implements View.OnClickListener {

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
        headView.findViewById(R.id.tv_register).setOnClickListener(this);
        headView.findViewById(R.id.tv_login).setOnClickListener(this);
        View zoomView = LayoutInflater.from(getContext()).inflate(R.layout.profile_zoom_view, null, false);//拉伸背景view
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.profile_content_view, null, false);
        mPtzUser.setHeaderView(headView);
        mPtzUser.setZoomView(zoomView);
        mPtzUser.setScrollContentView(contentView);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.tv_login:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
    }
}
