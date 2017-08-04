package com.jerry.zhoupro.presenter.feedback;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.base.TitleBaseActivity;
import com.jerry.zhoupro.model.bean.FeedbackBean;
import com.jerry.zhoupro.model.bean.UserManager;
import com.jerry.zhoupro.presenter.listener.MyTextWatcherListener;
import com.jerry.zhoupro.ui.feedback.UserFeedbackActivity;
import com.jerry.zhoupro.util.ImeUtils;
import com.jerry.zhoupro.util.SpannableStringUtils;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackPresenter extends TitleBaseActivity {

    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.et_feedback_content)
    EditText mEtFeedbackContent;
    @BindView(R.id.tv_commit_suggestion)
    TextView mTvCommitSuggestion;
    @BindView(R.id.tv_content_size)
    TextView mTvContentSize;

    @Override
    protected String getTitleText() {
        return getString(R.string.feedback);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initData() {
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(R.string.user_feedback);
        // 设置编辑框的监听
        mEtFeedbackContent.addTextChangedListener(new MyTextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                showRemainInput(s.toString());
            }
        });
        showRemainInput(Key.NIL);
    }

    @OnClick({R.id.back, R.id.tv_commit_suggestion, R.id.tv_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ImeUtils.hideIme(this);
                finish();
                break;
            case R.id.tv_commit_suggestion:
                String content = mEtFeedbackContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    toast(R.string.input_your_feedback_please);
                    return;
                }

                if (content.length() < 3) {
                    toast(R.string.enter_least_3_msg_word);
                    return;
                }
                ImeUtils.hideIme(this);
                commitData();
                break;
            case R.id.tv_right:
                startActivity(new Intent(FeedbackPresenter.this, UserFeedbackActivity.class));
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void commitData() {
        // 上传数据
        FeedbackBean feedback = new FeedbackBean();
        feedback.setUser(UserManager.getInstance().getUser());
        feedback.setContent(mEtFeedbackContent.getText().toString());
        feedback.save(new SaveListener<String>() {
            @Override
            public void done(final String s, final BmobException e) {
                if (e != null) {
                    toast(R.string.error);
                    return;
                }
                mEtFeedbackContent.setText("");
                toast(R.string.conmmit_success);
            }
        });
    }

    private void showRemainInput(String inputStr) {
        int size = 200;
        if (!TextUtils.isEmpty(inputStr)) {
            size -= inputStr.length();
        }
        mTvContentSize.setText(new SpannableStringUtils.Builder(getString(R.string.input_remain))
                .append(size).setForegroundColor(ContextCompat.getColor(this, R.color.blue_primary))
                .append(getString(R.string.word)).build());
    }
}
