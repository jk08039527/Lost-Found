package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;

import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.content)
    FrameLayout mContent;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tab_home, R.id.tab_unite, R.id.tab_live, R.id.tab_award, R.id.tab_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home:
                break;
            case R.id.tab_unite:
                break;
            case R.id.tab_live:
                break;
            case R.id.tab_award:
                break;
            case R.id.tab_me:
                break;
        }
    }
}
