package com.jerry.zhoupro.base;

import com.jerry.zhoupro.util.ToastUtils;
import com.jerry.zhoupro.widget.RefreshDialog;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BasePresenter> extends FragmentActivity implements BaseView{

    private Unbinder mUnbinder;
    private RefreshDialog progressDialog;
    private T mPresenter;

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

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

    protected void setGone(View view) {
        view.setVisibility(View.GONE);
    }

    protected void setVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    protected void setInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    protected void toast(int resId) {
        ToastUtils.showShort(this,resId);
    }

    @Override
    public void showErrorMsg(final String error) {

    }

    @Override
    public void stateNormal() {

    }

    @Override
    public void stateError() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateLoading() {

    }
}
