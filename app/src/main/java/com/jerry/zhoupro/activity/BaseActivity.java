package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.pop.RefreshDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    private RefreshDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViews();
        setContentView(getContentLayout());
        mUnbinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    protected void beforeViews() {}

    protected abstract int getContentLayout() ;

    protected void initView() {}

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void loadingDialog() {
        if (null == progressDialog) {
            progressDialog = new RefreshDialog(this);
        }
        if (progressDialog.isShowing()) {
            return;
        }
        progressDialog.show();
    }

    public void closeLoadingDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
