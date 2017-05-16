package com.jerry.zhoupro.view;

import com.jerry.zhoupro.R;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzl-pc on 2017/5/13.
 */

public class UserHeadView extends LinearLayout {

    private Context mContext;
    private ClickListener mClickListener;
    @BindView(R.id.iv_user_head)
    ImageView mIvUserHead;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_logout)
    TextView mTvLogout;

    public UserHeadView(final Context context) {
        super(context);
        mContext = context;
        View.inflate(mContext, R.layout.profile_head_view, this);
        ButterKnife.bind(this);
    }

    public ImageView getIvUserHead() {
        return mIvUserHead;
    }

    public TextView getTvUserName() {
        return mTvUserName;
    }

    public TextView getTvRegister() {
        return mTvRegister;
    }

    public TextView getTvLogin() {
        return mTvLogin;
    }

    public TextView getTvLogout() {
        return mTvLogout;
    }

    public void setPhotoImg(Uri uri) {
        mIvUserHead.setImageURI(uri);
    }

    public void setUserText(String userText) {
        mTvUserName.setText(userText);
    }

    public void updateUI(boolean hasLogin) {
        if (hasLogin) {
            mTvRegister.setVisibility(GONE);
            mTvLogin.setVisibility(GONE);
            mTvLogout.setVisibility(VISIBLE);
        } else {
            mTvRegister.setVisibility(VISIBLE);
            mTvLogin.setVisibility(VISIBLE);
            mTvLogout.setVisibility(GONE);
        }
    }

    @OnClick({R.id.tv_register, R.id.tv_login, R.id.tv_logout})
    public void onClick(View view) {
        if (mClickListener == null) {return;}
        switch (view.getId()) {
            case R.id.tv_register:
                mClickListener.register();
                break;
            case R.id.tv_login:
                mClickListener.login();
                break;
            case R.id.tv_logout:
                mClickListener.logout();
                break;
        }
    }

    public void setClickListener(final ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {

        void register();

        void login();

        void logout();
    }
}
