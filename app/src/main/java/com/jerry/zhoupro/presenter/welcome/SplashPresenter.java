package com.jerry.zhoupro.presenter.welcome;

import com.bumptech.glide.Glide;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.ui.main.MainActivity;
import com.jerry.zhoupro.util.ImeUtils;
import com.jerry.zhoupro.util.TimeTask;
import com.jerry.zhoupro.widget.SkipView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;


public class SplashPresenter extends AppCompatActivity implements SkipView.SkipListener {

    private boolean canSkip = true;// 是否能跳到首页

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(this).load(R.drawable.bizhi).into(imageView);
        setContentView(imageView);
        new TimeTask.OverDo(1500, new TimeTask.TimeOverListerner() {
            @Override
            public void onFinished() {
                start();
            }
        });
    }

    private void start() {
        if (!canSkip) { return; }
        Intent intent = new Intent();
//        if (PreferenceHelper.isFirst(PreferenceHelper.FIRST_INSTALLED)) {
//            PreferenceHelper.setNotFirst(PreferenceHelper.FIRST_INSTALLED);
//            intent.setClass(this, GuideActivity.class);
//        } else {
        intent.setClass(this, MainActivity.class);
//        }
        startActivity(intent);
        finish();
    }

    @Override
    public void OnSkip() {
        start();
    }

    @Override
    protected void onRestart() {
        if (!canSkip) {
            canSkip = true;
            start();
        }
        super.onRestart();
    }

    @Override
    public void onResume() {
        canSkip = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        canSkip = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ImeUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

}