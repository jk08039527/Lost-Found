package com.jerry.zhoupro.activity;

import java.util.ArrayList;
import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.fragment.BaseFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

/**
 * Created by Administrator on 2016/3/28.
 */
public abstract class FragmentActivity extends BaseActivity {
    protected List<BaseFragment> listKeyDownNotify = new ArrayList<BaseFragment>();
    private boolean isFragmentBack;

    @Override
    protected void onCreate(Bundle savedactivityState) {
        super.onCreate(savedactivityState);
        // 取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getContentLayout() != 0) {
            setContentView(getContentLayout());
        }
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {

                    @Override
                    public void onBackStackChanged() {
                        if (isFragmentBack) {
                        }
                    }
                });
        initView();
        initData();
    }

    protected abstract int getContentLayout();

    protected abstract void initView();

    protected abstract void initData();

    /**
     * 打开等待层
     */
    protected void showLoadingView() {
    }

    /**
     * 关闭等待层
     */
    protected void hideLoadingView() {
    }

    @Override
    public void onBackPressed() {
        if (fragmentCount() <= 0) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * 需要保留，"我的最爱"增加菜单，返回时需要走fragment生命周期，重新加载菜单
     *
     * @param fragment
     * @param backStackFlag
     */
    public void changeFragment(BaseFragment fragment, boolean backStackFlag) {
        changeFragment(fragment, backStackFlag, true);
    }

    public void changeFragment(BaseFragment fragment, boolean backStackFlag,
                               boolean isAnimated) {
        changeFragment(R.id.fragments_contain, fragment, backStackFlag,
                isAnimated);
    }

    public void changeFragment(int containId, BaseFragment fragment,
                               boolean backStackFlag, boolean isAnimated) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (isAnimated) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        ft.replace(containId, fragment);
        if (backStackFlag) {
            ft.addToBackStack(null);
        } else {
            clearPopBackStack();
        }
        ft.commitAllowingStateLoss();
    }

    public void addFragment(BaseFragment fragment, boolean backStackFlag) {
        isFragmentBack = false;
        addFragment(R.id.fragments_contain, fragment, backStackFlag);
    }

    public void addFragment(int containId, BaseFragment fragment,
                            boolean backStackFlag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(containId, fragment);
        if (backStackFlag) {
            ft.addToBackStack(null);
        } else {
            clearPopBackStack();
        }
        ft.commitAllowingStateLoss();
    }

    public int fragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    public void popBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            isFragmentBack = true;
            getSupportFragmentManager().popBackStack();
        }
    }

    public void clearPopBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (listKeyDownNotify != null) {
            boolean isHook = false;
            for (int i = 0; i < listKeyDownNotify.size(); i++) {
                isHook = listKeyDownNotify.get(i).onKeyDown(keyCode, event);
            }
            if (isHook) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registerKeyDownNotify(BaseFragment fragment) {
        if (fragment != null) {
            listKeyDownNotify.add(fragment);
        }
    }

    public void removeKeyDownNotify(BaseFragment fragment) {
        if (listKeyDownNotify.contains(fragment)) {
            listKeyDownNotify.remove(fragment);
        }
    }

    public void onEvent(Object object) {

    }

    public Fragment getCurrentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments == null) {
            return null;
        }
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (null != fragment && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}