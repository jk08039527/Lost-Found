package com.jerry.zhoupro.activity;

import com.baidu.mapapi.map.MapView;
import com.jerry.zhoupro.R;

import android.widget.Button;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.bmapView)
    MapView mBmapView;

    @Override
    protected void initAction() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBmapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBmapView.onDestroy();
    }
}
