package com.jerry.zhoupro.pop;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.util.DisplayUtil;
import com.jerry.zhoupro.util.ToastTools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * @author wzl 2015-9-6 类说明：对话框基类
 */
public class BaseDialog extends Dialog {

    protected Context mContext;
    protected TextView mPositiveButton;
    private TextView mNegativeButton;
    private View splitLine;
    private TextView mMessageView;
    private int margin = 15;// 默认边距是交互性对话框

    public BaseDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }

    public BaseDialog(Context context, boolean isOp) {
        this(context);
        if (!isOp) {
            margin = 41;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        // 默认的样式@android:style/Theme.Dialog 对应的style 有pading属性
        Window win = getWindow();
        win.getDecorView().setPadding(DisplayUtil.dip2px(margin), 0, DisplayUtil.dip2px(margin), 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        init();
    }

    private void init() {
        mPositiveButton = (TextView) findViewById(R.id.confirm_tv);
        mNegativeButton = (TextView) findViewById(R.id.cancel_tv);
        splitLine = findViewById(R.id.split_line);
        OnClickEvent mClickEvent = new OnClickEvent();
        if (mPositiveButton == null || mNegativeButton == null) {
            return;
        }
        mPositiveButton.setOnClickListener(mClickEvent);
        mNegativeButton.setOnClickListener(mClickEvent);
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
        if (null != mPositiveButton) {
            mPositiveButton.setOnClickListener(listener);
        }
    }

    /**
     * 设置取消按钮点击事件
     */
    public void setNegativeButtonListener(View.OnClickListener listener) {
        if (null != mNegativeButton) {
            mNegativeButton.setOnClickListener(listener);
        }
    }

    public void setPositiveButtonText(int id) {
        if (null != mPositiveButton) {
            mPositiveButton.setText(getContext().getResources().getString(id));
        }
    }

    public void setNegativeButtonText(int id) {
        if (null != mNegativeButton) {
            mNegativeButton.setText(getContext().getResources().getString(id));
        }
    }

    public void setPositiveButtonText(String text) {
        if (null != mPositiveButton) {
            mPositiveButton.setText(text);
        }
    }

    public void setNegativeButtonText(String text) {
        if (null != mNegativeButton) {
            mNegativeButton.setText(text);
        }
    }

    protected void setPositiveButtonGone() {
        if (null != mPositiveButton) {
            mPositiveButton.setVisibility(View.GONE);
        }
        if (null != splitLine) {
            splitLine.setVisibility(View.GONE);
        }
    }

    public void setNegativeButtonGone() {
        if (null != mNegativeButton) {
            mNegativeButton.setVisibility(View.GONE);
        }
        if (null != splitLine) {
            splitLine.setVisibility(View.GONE);
        }
    }

    public void setMessage(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (null != mMessageView) {
            mMessageView.setText(message);
        }
    }

    public void setMessage(int resId) {
        if (null != mMessageView) {
            mMessageView.setText(resId);
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

    protected void toast(String string) {
        ToastTools.showShort(mContext, string);
    }
}
