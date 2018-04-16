package com.jerry.zhoupro.base;

import com.jerry.zhoupro.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class TitleBaseActivity extends BaseActivity {

    @BindView(R.id.back)
    TextView titleBack;
    @BindView(R.id.iv_close)
    ImageView titleClose;
    @BindView(R.id.tv_title)
    TextView titleText;
    @BindView(R.id.iv_more)
    ImageView titleMore;
    @BindView(R.id.tv_right)
    protected TextView titleRight;

    protected abstract String getTitleText();

    @Override
    public void initView() {
        super.initView();
        titleText.setText(getTitleText());
    }

    @OnClick({R.id.back})
    public void onClick(final View v) {
        if (v.getId() == R.id.back) { onBackPressed(); }
    }
}
