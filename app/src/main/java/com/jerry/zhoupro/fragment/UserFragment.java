package com.jerry.zhoupro.fragment;


import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jerry.zhoupro.MLog.Mlog;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.LoginActivity;
import com.jerry.zhoupro.activity.RegisterActivity;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.User;
import com.jerry.zhoupro.util.PreferenceUtil;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class UserFragment extends TitleBaseFragment implements View.OnClickListener {

    private static final int REGISTER = 0x1;
    private static final int LOGIN = 0x2;

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
                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), REGISTER);
                break;
            case R.id.tv_login:
                startActivityForResult(new Intent(getContext(), LoginActivity.class), LOGIN);
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) { return; }
        switch (requestCode) {
            case REGISTER:
            case LOGIN:
                User user = new User();
                user.setMobilePhoneNumber(data.getStringExtra(Key.phone));
                user.setPassword(data.getStringExtra(Key.password));
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(final User user, final BmobException e) {
                        if (e != null) {
                            Mlog.e(e.toString());
                            toast(R.string.login_fail);
                        }
                        PreferenceUtil.setPreference(Key.USER, user.getObjectId());
                        PreferenceUtil.setPreference(Key.USER_MOBLIE, user.getMobilePhoneNumber());
                        PreferenceUtil.setPreference(Key.USER_NICKNAME, user.getUsername());
                        PreferenceUtil.setPreference(Key.USER_SESSIONTOKEN, user.getSessionToken());
                    }
                });
                break;
        }
    }
}
