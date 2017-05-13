package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link AppCompatActivity} subclass.
 */
abstract class TitleBaseActivity extends BaseActivity {

    @BindView(R.id.back)
    TextView titleBack;
    @BindView(R.id.iv_close)
    ImageView titleClose;
    @BindView(R.id.tv_title)
    TextView titleText;
    @BindView(R.id.iv_more)
    ImageView titleMore;
    @BindView(R.id.tv_right)
    TextView titleRight;

    protected abstract String getTitleText();

    @Override
    public void initView() {
        super.initView();
        titleText.setText(getTitleText());
    }

    protected void setGone(View view) {
        view.setVisibility(View.GONE);
    }

    protected void setVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    protected void setInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.back})
    public void onClick(final View v) {
        finish();
    }
}
