package com.jerry.zhoupro.fragment;


import com.jerry.zhoupro.util.ToastTools;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/3/26.
 */
abstract class BaseFragment extends Fragment {

    private static final String LOG_TAG = BaseFragment.class.getSimpleName();
    private Context mContext;
    private Unbinder unbinder;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentLayout(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 设置布局文件
     */
    public abstract int getContentLayout();

    /**
     * 控件初始化
     */
    public void initView(View view) {}

    /**
     * 数据处理
     */
    protected void initData() {}


    protected void toast(int resId) {
        ToastTools.showShort(getContext(), resId);
    }

    protected void toast(String msg) {
        ToastTools.showShort(getContext(), msg);
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
}
