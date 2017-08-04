package com.jerry.zhoupro.ui.main;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.BaseActivity;
import com.jerry.zhoupro.app.Constants;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.model.bean.UserManager;
import com.jerry.zhoupro.model.prefs.PreferenceHelper;
import com.jerry.zhoupro.ui.release.ReleaseActivity;
import com.jerry.zhoupro.ui.release.ReleasePlaceFragment;
import com.jerry.zhoupro.ui.user.LoginActivity;
import com.jerry.zhoupro.ui.user.UserFragment;
import com.jerry.zhoupro.util.TimeTask;
import com.jerry.zhoupro.widget.ReleasePopWindow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
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
    }

    @Override
    protected void initData() {
        setSelectTab(getIntent().getExtras());
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
//                toast(R.string.data_load_failed);
                if (mReleasePopWindow == null) {
                    mReleasePopWindow = new ReleasePopWindow(this, new ReleasePopWindow.PopMenuClickListener() {
                        @Override
                        public void onPopMenuClick(final View view) {
                            int requestCode;
                            switch (view.getId()) {
                                case R.id.tv_release_lost:
                                    requestCode = Key.TAG_RELEASE_LOST;
                                    break;
                                case R.id.tv_release_found:
                                    requestCode = Key.TAG_RELEASE_FOUND;
                                    break;
                                case R.id.tv_release_find:
                                    requestCode = Key.TAG_RELEASE_FIND;
                                    break;
                                default:
                                    requestCode = Key.TAG_RELEASE_FIND;
                                    break;
                            }
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
                    fragmentFind = new VirtueFragment();
                    transaction.add(R.id.content, fragmentFind);
                } else {
                    transaction.show(fragmentFind);
                }
                break;
            case Constants.TAB_MSG:
                if (null == fragmentMsg) {
                    fragmentMsg = new ReleasePlaceFragment();
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
        PreferenceHelper.setPreference(Key.TABTAG, selectTag);
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
        int tabTag;
        if (extras != null) {
            tabTag = extras.getInt(Key.TABTAG, 0);
        } else {
            tabTag = PreferenceHelper.getPreference(Key.TABTAG, 0);
        }
        setContentFragment(tabTag);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {return;}
        if (requestCode == Key.TAG_RELEASE_LOST || requestCode == Key.TAG_RELEASE_FOUND
                || requestCode == Key.TAG_RELEASE_FIND) {
            Intent intent = new Intent(MainActivity.this, ReleaseActivity.class);
            intent.putExtra(Key.RELEASE_TYPE, requestCode);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            toast(R.string.press_again_exit);
            if (!hasTask) {
                hasTask = true;
                new TimeTask.OverDo(2000, new TimeTask.TimeOverListerner() {
                    @Override
                    public void onFinished() {
                        isExit = false;
                        hasTask = false;
                    }
                });
            }
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ActivityCompat.finishAffinity(this);
            return true;
        }
        return false;
    }
}
