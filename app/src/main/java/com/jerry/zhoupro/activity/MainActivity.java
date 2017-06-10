package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Constants;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.fragment.FindFragment;
import com.jerry.zhoupro.fragment.HomeFragment;
import com.jerry.zhoupro.fragment.MsgFragment;
import com.jerry.zhoupro.fragment.UserFragment;
import com.jerry.zhoupro.pop.ReleasePopWindow;
import com.jerry.zhoupro.util.TimeTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static int selectTag = Constants.TAB_HOME;

    @BindView(R.id.content)
    FrameLayout mContent;
    @BindView(R.id.tab_bar)
    LinearLayout mTabBar;
    @BindView(R.id.tab_home)
    TextView mTabHome;
    @BindView(R.id.tab_find)
    TextView mTabFind;
    @BindView(R.id.tab_msg)
    TextView mTabMsg;
    @BindView(R.id.tab_me)
    TextView mTabMe;

    private FragmentManager fragmentManager;
    private Fragment fragmentHome, fragmentFind, fragmentMsg, fragmentMe;
    private ReleasePopWindow mReleasePopWindow;
    private boolean isExit;
    private boolean hasTask;

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

    @OnClick({R.id.tab_home, R.id.tab_find, R.id.iv_release, R.id.tab_msg, R.id.tab_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home:
                setContentFragment(Constants.TAB_HOME);
                break;
            case R.id.tab_find:
                setContentFragment(Constants.TAB_FIND);
                break;
            case R.id.iv_release:
                if (mReleasePopWindow == null) {
                    mReleasePopWindow = new ReleasePopWindow(this, new ReleasePopWindow.PopMenuClickListener() {
                        @Override
                        public void onPopMenuClick(final View view) {
                            int requestCode = view.getId() == R.id.tv_release_lost ? Key.TAG_RELEASE_LOST : Key.TAG_RELEASE_FOUND;
                            Intent intent;
                            if (UserManager.hasLogin()) {
                                intent = new Intent(MainActivity.this, ReleaseActivity.class);
                                intent.putExtra(Key.RELEASE_TYPE, requestCode);
                                startActivity(intent);
                            } else {
                                intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivityForResult(intent, requestCode);
                            }
                        }
                    });
                }
                mReleasePopWindow.show();
                break;
            case R.id.tab_msg:
                setContentFragment(Constants.TAB_MSG);
                break;
            case R.id.tab_me:
                setContentFragment(Constants.TAB_ME);
                break;
            default:
                break;
        }
    }

    private void changeSelect(int index) {
        mTabHome.setSelected(index == Constants.TAB_HOME);
        mTabFind.setSelected(index == Constants.TAB_FIND);
        mTabMsg.setSelected(index == Constants.TAB_MSG);
        mTabMe.setSelected(index == Constants.TAB_ME);
    }

    private void setContentFragment(int selectTag) {
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
        changeSelect(selectTag);
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
        int tabTag = 0;
        if (extras != null) {
            tabTag = extras.getInt(Key.tabTag, -1);
        }
        if (tabTag == -1) {
            setContentFragment(Constants.TAB_HOME);
            return;
        }
        setContentFragment(tabTag);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK && requestCode == Key.TAG_RELEASE_LOST || requestCode == Key.TAG_RELEASE_FOUND) {
            Intent intent = new Intent(MainActivity.this, ReleaseActivity.class);
            intent.putExtra(Key.RELEASE_TYPE, requestCode);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            toast(R.string.press_again_exit);
            if (!hasTask) {
                hasTask = true;
                new TimeTask(2000, new TimeTask.TimeOverListerner() {
                    @Override
                    public void onFinished() {
                        isExit = false;
                        hasTask = false;
                    }
                }).start();
            }
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }
}
