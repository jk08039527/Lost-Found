package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.util.DateUtils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        ButterKnife.bind(this);
        initView();
        initAction();
    }

    protected abstract void initAction();

    protected abstract void initView();

    protected abstract int getContentLayout() ;

    public void myClick(View view) {
        int df = DateUtils.compareTo("19911027", "19910803");
        Toast.makeText(this, df, Toast.LENGTH_SHORT).show();

    }
}
