package com.jerry.zhoupro.activity;

import com.baidu.mapapi.map.MapView;
import com.jerry.zhoupro.R;

import android.widget.Button;

import butterknife.BindView;

public class MapActivity extends BaseActivity {

    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.bmapView)
    MapView mBmapView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected void initData() {

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
