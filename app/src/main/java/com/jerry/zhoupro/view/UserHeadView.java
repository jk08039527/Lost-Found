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

import static com.jerry.zhoupro.R.id.tv_user_name;

/**
 * Created by wzl-pc on 2017/5/13.
 */

public class UserHeadView extends LinearLayout {

    protected Context mContext;
    @BindView(R.id.iv_user_head)
    ImageView mIvUserHead;
    @BindView(tv_user_name)
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
}
