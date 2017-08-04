package com.jerry.zhoupro.widget;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jerry.zhoupro.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
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

    private HeadClickListener mHeadClickListener;
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

    public UserHeadView(final Context context, final HeadClickListener headClickListener) {
        super(context);
        mHeadClickListener = headClickListener;
        View.inflate(context, R.layout.profile_head_view, this);
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

    public void setHeadImg(String uri) {
        if (TextUtils.isEmpty(uri)) {
            mIvUserHead.setImageResource(R.drawable.ic_img_user_default);
        } else {
            Glide.with(getContext()).load(uri)
                    .transition(new DrawableTransitionOptions().dontTransition())
                    .thumbnail(Glide.with(getContext()).load(R.drawable.ic_img_user_default))
                    .into(mIvUserHead);
        }
    }

    public void setHeadImg(final Bitmap bm) {
        if (bm == null) {
            mIvUserHead.setImageResource(R.drawable.ic_img_user_default);
        } else {
            mIvUserHead.setImageBitmap(bm);
        }
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

    @OnClick({R.id.iv_user_head, R.id.tv_register, R.id.tv_login, R.id.tv_logout})
    public void onClick(View view) {
        if (mHeadClickListener == null) {return;}
        switch (view.getId()) {
            case R.id.iv_user_head:
                mHeadClickListener.changePicClick();
                break;
            case R.id.tv_register:
                mHeadClickListener.registerClick();
                break;
            case R.id.tv_login:
                mHeadClickListener.loginClick();
                break;
            case R.id.tv_logout:
                mHeadClickListener.logoutClick();
                break;
        }
    }

    public interface HeadClickListener {

        void changePicClick();

        void registerClick();

        void loginClick();

        void logoutClick();
    }
}
