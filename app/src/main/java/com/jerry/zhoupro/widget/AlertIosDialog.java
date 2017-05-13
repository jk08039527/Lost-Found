package com.jerry.zhoupro.widget;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.util.DisplayUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 仿ios弹框
 */
public class AlertIosDialog extends Dialog {

    protected Context mContext;
    private int margin = 41;// 默认边距是交互性对话框

    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.txt_msg)
    TextView mTxtMsg;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.btn_neg)
    TextView mBtnNeg;
    @BindView(R.id.btn_pos)
    TextView mBtnPos;
    @BindView(R.id.img_line)
    ImageView mImgLine;

    public AlertIosDialog(@NonNull final Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_alertdialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        Window win = getWindow();
        win.getDecorView().setPadding(DisplayUtil.dip2px(margin), 0, DisplayUtil.dip2px(margin), 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        OnClickEvent mClickEvent = new OnClickEvent();
        if (mBtnNeg == null || mBtnPos == null) { return; }
        mBtnNeg.setOnClickListener(mClickEvent);
        mBtnPos.setOnClickListener(mClickEvent);
    }

    public void setTitleText(int id) {
        if (null != mTxtTitle) {
            mTxtTitle.setText(mContext.getString(id));
        }
    }

    private class OnClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    /**
     * 设置确定按钮点击事件
     */
    public void setPositiveButtonListener(View.OnClickListener listener) {
        if (null != mBtnPos) {
            mBtnPos.setOnClickListener(listener);
        }
    }

    /**
     * 设置取消按钮点击事件
     */
    public void setNegativeButtonListener(View.OnClickListener listener) {
        if (null != mBtnNeg) {
            mBtnNeg.setOnClickListener(listener);
        }
    }

    public void setPositiveButtonText(int id) {
        if (null != mBtnPos) {
            mBtnPos.setText(getContext().getResources().getString(id));
        }
    }

    public void setNegativeButtonText(int id) {
        if (null != mBtnNeg) {
            mBtnNeg.setText(getContext().getResources().getString(id));
        }
    }

    public void setPositiveButtonText(String text) {
        if (null != mBtnPos) {
            mBtnPos.setText(text);
        }
    }

    public void setNegativeButtonText(String text) {
        if (null != mBtnNeg) {
            mBtnNeg.setText(text);
        }
    }

    protected void setPositiveButtonGone() {
        if (null != mBtnPos) {
            mBtnPos.setVisibility(View.GONE);
        }
        if (null != mImgLine) {
            mImgLine.setVisibility(View.GONE);
        }
    }

    public void setNegativeButtonGone() {
        if (null != mBtnNeg) {
            mBtnNeg.setVisibility(View.GONE);
        }
        if (null != mImgLine) {
            mImgLine.setVisibility(View.GONE);
        }
    }

    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (null != mTxtMsg) {
            mTxtMsg.setText(message);
        }
    }

    public void setMessage(int resId) {
        if (null != mTxtMsg) {
            mTxtMsg.setText(resId);
        }
    }

    @Override
    public void show() {
        if (null != mContext && !((Activity) mContext).isFinishing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
