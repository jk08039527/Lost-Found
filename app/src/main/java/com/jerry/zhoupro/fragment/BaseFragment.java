package com.jerry.zhoupro.fragment;


import com.jerry.zhoupro.FYLog.Mlog;
import com.jerry.zhoupro.activity.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/3/26.
 */
public abstract class BaseFragment extends Fragment {
    private static final String LOG_TAG = BaseFragment.class.getSimpleName();
    protected View contentView = null;
    protected FragmentActivity activity = null;
    protected boolean isFirstLoad = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(getContentLayout(), container, false);
            isFirstLoad = true;
        } else {
            isFirstLoad = false;
        }
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirstLoad) {
            initView();
            initAction();
            initData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Mlog.d(LOG_TAG, this.toString() + "registerKeyDownNotify");
        activity.registerKeyDownNotify(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Mlog.d(LOG_TAG, this.toString() + "removeKeyDownNotify");
        activity.removeKeyDownNotify(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup vp = (ViewGroup) contentView.getParent();
        if (null != vp) {
            vp.removeView(contentView);
        }
    }

    /**
     * 设置布局文件
     */
    public abstract int getContentLayout();

    /**
     * 控件初始化
     */
    public abstract void initView();

    ;

    /**
     * 事件监听
     */
    public abstract void initAction();

    ;

    /**
     * 数据处理
     */
    protected void initData() {
    }

    ;

    /**
     * 处理硬键点击
     * 返回false，事件继续传递
     * 返回true，事件终止
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 后退
     */
    public void finish() {
        activity.popBack();
    }

    /**
     * 查找控件
     */
    public View findViewById(int id) {
        View v = null;
        if (contentView != null) {
            v = contentView.findViewById(id);
        }
        return v;
    }

    /**
     * 启动Fragment
     *
     * @param cls    需要启动Fragment的Class
     * @param params 需要向启动Fragment传递的参数
     */
    public boolean startFragment(Class<?> cls, Bundle params) {
        boolean isSuccess = false;
        try {
            BaseFragment fragment = (BaseFragment) cls.newInstance();
            if (params != null) {
                fragment.setArguments(params);
            }
            activity.changeFragment(fragment, true);
            isSuccess = true;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
            isSuccess = false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 启动Activity
     */
    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    /**
     * 启动Activity，接收返回结果
     */
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * EventBus监听事件，子类根据需要进行重写
     *
     * @param object
     */
    public void onEvent(Object object) {
    }
}
