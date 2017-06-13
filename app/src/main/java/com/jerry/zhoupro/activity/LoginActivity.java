package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.User;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.listener.MyTextWatcherListener;
import com.jerry.zhoupro.util.ImeUtils;
import com.jerry.zhoupro.util.PreferenceUtil;
import com.jerry.zhoupro.util.StringUtils;
import com.jerry.zhoupro.util.ViewUtil;
import com.jerry.zhoupro.widget.MyEditText;
import com.jerry.zhoupro.widget.PhoneNumEditText;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends TitleBaseActivity {

    private boolean isHidden, isEverTouch;
    private String lastPhone = PreferenceUtil.getPreference(Key.USER_MOBLIE);

    @BindView(R.id.et_account)
    PhoneNumEditText mEtAccount;
    @BindView(R.id.et_passwd)
    MyEditText mEtPasswd;
    @BindView(R.id.iv_pwd_show)
    ImageView mIvPwdShow;
    @BindView(R.id.tv_login)
    TextView mTvLogin;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        mEtAccount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!isEverTouch) {
                    mEtAccount.getText().clear();
                    mEtAccount.setCursorVisible(true);
                    isEverTouch = true;
                }
                return false;
            }
        });
        mEtAccount.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                mTvLogin.setEnabled(mEtPasswd.getText().length() > 5 && s.length() > 10);
            }
        });

        mEtPasswd.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                mTvLogin.setEnabled(mEtAccount.getText().length() > 10 && s.length() > 5);
            }
        });
        if (!TextUtils.isEmpty(lastPhone)) {
            mEtAccount.setText(StringUtils.phoneMosaic(lastPhone));
        }
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.login);
    }

    @OnClick({R.id.tv_reg, R.id.iv_pwd_show, R.id.tv_login})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reg:
                startActivityForResult(new Intent(this, RegisterActivity.class), Key.REGISTER);
                finish();
                break;
//            case R.id.tv_forget_pwd:
//                startActivityForResult(new Intent(this, ResetPwdActivity.class), Key.CODE_101);
//                break;
            case R.id.iv_pwd_show:
                if (isHidden) {
                    mEtPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvPwdShow.setImageResource(R.drawable.eye_hide);
                } else {
                    mEtPasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvPwdShow.setImageResource(R.drawable.eye_show);
                }
                isHidden = !isHidden;
                ViewUtil.setEditSelection(mEtPasswd);
                break;
            case R.id.tv_login:
                String username = isEverTouch ? mEtAccount.getText().toString().trim() : lastPhone;
                username = username.replaceAll(Key.SPACE, Key.NIL);// 去除空格
                String passWd = mEtPasswd.getText().toString().trim();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passWd)) {
                    return;
                }
                User user = new User();
                user.setUsername(username);
                user.setPassword(passWd);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(final User user, final BmobException e) {
                        if (e != null) {
                            Mlog.e(e.toString());
                            toast(R.string.login_fail);
                        }
                        UserManager.getInstance().saveToLocal(user);
                        ImeUtils.hideIme(LoginActivity.this);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                break;
            default:
                super.onClick(v);
                break;
        }
    }
}
