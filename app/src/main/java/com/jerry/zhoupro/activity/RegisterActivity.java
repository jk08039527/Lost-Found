package com.jerry.zhoupro.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.User;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.listener.MyTextWatcherListener;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.util.PatternsUtil;
import com.jerry.zhoupro.util.ViewUtil;
import com.jerry.zhoupro.widget.MyEditText;
import com.jerry.zhoupro.widget.NoticeDialog;
import com.jerry.zhoupro.widget.PhoneNumEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends TitleBaseActivity {

    @BindView(R.id.et_account)
    PhoneNumEditText mAccountEt;
    @BindView(R.id.et_passwd)
    MyEditText mPasswdEt;
    @BindView(R.id.iv_pwd_show)
    ImageView mIvPwdShow;
    @BindView(R.id.tv_register)
    TextView mRegisterTv;
    @BindView(R.id.et_nickname)
    MyEditText mEtNickname;
    private String mPhone;
    private String mPwd;
    private boolean isHidden;
    private User mUser;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.register);
    }

    @Override
    protected void beforeViews() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        mPhone = bundle.getString(Key.phone);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(mPhone)) {
            mAccountEt.setText(mPhone);
        }
        mAccountEt.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                mRegisterTv.setEnabled(mAccountEt.getText().toString().length() == 13 && mPasswdEt.getText().toString().length() > 5);
            }
        });
        mPasswdEt.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                mRegisterTv.setEnabled(mAccountEt.getText().toString().length() == 13 && mPasswdEt.getText().toString().length() > 5);
            }
        });
    }

    @OnClick({R.id.iv_pwd_show, R.id.tv_register})
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.iv_pwd_show:
                if (isHidden) {
                    mPasswdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvPwdShow.setImageResource(R.drawable.eye_hide);
                } else {
                    mPasswdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvPwdShow.setImageResource(R.drawable.eye_show);
                }
                isHidden = !isHidden;
                ViewUtil.setEditSelection(mPasswdEt);
                break;
            case R.id.tv_register:
                mPhone = mAccountEt.getText().toString().trim();
                mPhone = mPhone.replaceAll(Key.SPACE, Key.NIL);// 去除空格
                if (TextUtils.isEmpty(mPhone) && mPhone.length() != 11) {
                    toast(R.string.input_phone);
                    return;
                }
                mPwd = mPasswdEt.getText().toString().trim();
                if (TextUtils.isEmpty(mPwd)) {
                    toast(R.string.input_pwd);
                    return;
                }
                if (PatternsUtil.isChinese(mPwd)) {
                    toast(R.string.do_not_contains_chinese_in_pwd);
                    return;
                }
                if (PatternsUtil.passwordGrade(mPwd) < 15) {
                    toast(R.string.pwd_too_simple);
                    return;
                }
                final NoticeDialog noticeDialog = new NoticeDialog(this);
                noticeDialog.show();
                noticeDialog.setTitleText(R.string.confirm_phone_num);
                noticeDialog.setMessage(getString(R.string.send_captcha_to_phone) + '\n' + mPhone);
                noticeDialog.setPositiveButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();
                        mUser = new User();
                        mUser.setUsername(mPhone);
                        mUser.setMobilePhoneNumber(mPhone);
                        mUser.setMobilePhoneNumberVerified(false);
                        mUser.setPassword(mPwd);
                        mUser.setMobilePhoneNumberVerified(true);
                        mUser.setNickname(mEtNickname.getText().toString().trim());
                        mUser.signUp(new SaveListener<User>() {
                            @Override
                            public void done(final User s, final BmobException e) {
                                if (e != null) {
                                    Mlog.e(e.toString());
                                    switch (e.getErrorCode()) {
                                        case 202:
                                            toast(R.string.already_register);
                                            return;
                                        default:
                                            toast(R.string.register_fail);
                                            return;
                                    }
                                }
                                executeVerifyCallBack();
                            }
                        });
                    }
                });
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void executeVerifyCallBack() {
        mRegisterTv.setText(getString(R.string.wait_a_moment));
        mRegisterTv.setEnabled(false);
        loadingDialog();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = format.format(new Date());
        BmobSMS.requestSMSCode(mPhone, sendTime, new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex != null) {
                    Mlog.e(ex.toString());
                    return;
                }
                closeLoadingDialog();
                mRegisterTv.setText(getString(R.string.register));
                mRegisterTv.setEnabled(true);
                Intent intent = new Intent(RegisterActivity.this, RegisterVerifyCodeActivity.class);
                intent.putExtra(Key.phone, mPhone);
                startActivityForResult(intent, Key.REGISTER);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { return; }
        UserManager.getInstance().saveToLocal(mUser);
        setResult(RESULT_OK);
        finish();
    }
}
