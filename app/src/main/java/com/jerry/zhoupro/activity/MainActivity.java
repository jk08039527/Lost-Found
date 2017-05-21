package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Constants;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.fragment.FindFragment;
import com.jerry.zhoupro.fragment.HomeFragment;
import com.jerry.zhoupro.fragment.MsgFragment;
import com.jerry.zhoupro.fragment.UserFragment;
import com.jerry.zhoupro.pop.ReleasePopWindow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static String selectTag = Constants.TAB_HOME;

    @BindView(R.id.content)
    FrameLayout mContent;
    @BindView(R.id.tab_bar)
    LinearLayout mTabBar;
    @BindView(R.id.tab_home)
    Button mTabHome;
    @BindView(R.id.tab_find)
    Button mTabFind;
    @BindView(R.id.tab_add)
    Button mTabAdd;
    @BindView(R.id.tab_msg)
    Button mTabMsg;
    @BindView(R.id.tab_me)
    Button mTabMe;

    private FragmentManager fragmentManager;
    private Fragment fragmentHome, fragmentFind, fragmentMsg, fragmentMe;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fragmentManager = getSupportFragmentManager();
        setSelectTab(getIntent().getExtras());
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tab_home, R.id.tab_find, R.id.tab_add, R.id.tab_msg, R.id.tab_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home:
                setContentFragment(Constants.TAB_HOME);
                break;
            case R.id.tab_find:
                setContentFragment(Constants.TAB_FIND);
                break;
            case R.id.tab_add:
                new ReleasePopWindow(this, new ReleasePopWindow.PopMenuClickListener() {
                    @Override
                    public void onPopMenuClick(final View view) {
                        switch (view.getId()){
                            case R.id.tv_release_lost:

                                break;
                            case R.id.tv_release_found:
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.tab_msg:
                setContentFragment(Constants.TAB_MSG);
                break;
            case R.id.tab_me:
                setContentFragment(Constants.TAB_ME);
                break;
        }
    }

    private void changeSelect(int id) {
        View v;
        for (int i = 0; i < mTabBar.getChildCount(); i++) {
            v = mTabBar.getChildAt(i);
            v.setSelected(v.getId() == id);
        }
    }

    private void setContentFragment(String selectTag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (selectTag) {
            case Constants.TAB_HOME:
                if (null == fragmentHome) {
                    fragmentHome = new HomeFragment();
                    transaction.add(R.id.content, fragmentHome);
                } else {
                    transaction.show(fragmentHome);
                }
                break;
            case Constants.TAB_FIND:
                if (null == fragmentFind) {
                    fragmentFind = new FindFragment();
                    transaction.add(R.id.content, fragmentFind);
                } else {
                    transaction.show(fragmentFind);
                }
                break;
            case Constants.TAB_MSG:
                if (null == fragmentMsg) {
                    fragmentMsg = new MsgFragment();
                    transaction.add(R.id.content, fragmentMsg);
                } else {
                    transaction.show(fragmentMsg);
                }
                break;
            case Constants.TAB_ME:
                if (null == fragmentMe) {
                    fragmentMe = new UserFragment();
                    transaction.add(R.id.content, fragmentMe);
                } else {
                    transaction.show(fragmentMe);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != fragmentHome) {
            transaction.hide(fragmentHome);
        }
        if (null != fragmentFind) {
            transaction.hide(fragmentFind);
        }
        if (null != fragmentMsg) {
            transaction.hide(fragmentMsg);
        }
        if (null != fragmentMe) {
            transaction.hide(fragmentMe);
        }
    }
    private void setSelectTab(Bundle extras) {
        String tabTag = Key.NIL;
        if (extras != null) {
            tabTag = extras.getString(Key.tabTag);
        }
        if (TextUtils.isEmpty(tabTag)) {
            setContentFragment(Constants.TAB_HOME);
            return;
        }
        setContentFragment(tabTag);
    }
}
