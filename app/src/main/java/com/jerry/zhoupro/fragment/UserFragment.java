package com.jerry.zhoupro.fragment;


import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.LoginActivity;
import com.jerry.zhoupro.activity.RegisterActivity;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.User;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.view.UserContentView;
import com.jerry.zhoupro.view.UserHeadView;
import com.jerry.zhoupro.widget.NoticeDialog;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class UserFragment extends TitleBaseFragment implements AdapterView.OnItemClickListener {

    private static final int REGISTER = 0x1;
    private static final int LOGIN = 0x2;
    private UserHeadView headView;

    @BindView(R.id.ptz_user)
    PullToZoomScrollViewEx mPtzUser;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.me);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        setGone(titleBack);
        setGone(titleMore);
        loadViewForCode((ViewGroup) view);
    }

    private void loadViewForCode(ViewGroup view) {
        headView = new UserHeadView(getContext());//头部扩展view
        headView.setHeadClickListener(new UserHeadView.HeadClickListener() {
            @Override
            public void register() {
                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), REGISTER);
            }

            @Override
            public void login() {
                startActivityForResult(new Intent(getContext(), LoginActivity.class), LOGIN);
            }

            @Override
            public void logout() {
                final NoticeDialog noticeDialog = new NoticeDialog(getContext());
                noticeDialog.show();
                noticeDialog.setTitleText(R.string.remind);
                noticeDialog.setMessage(getString(R.string.confirm_logout));
                noticeDialog.setPositiveButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();
                        UserManager.clearLoginInfo();
                        updateHeadView(false);
                    }
                });
            }
        });
        View zoomView = LayoutInflater.from(getContext()).inflate(R.layout.profile_zoom_view, view, false);//拉伸背景view
        UserContentView contentView = new UserContentView(getContext());
        contentView.setOnItemClickListener(this);
        mPtzUser.setHeaderView(headView);
        mPtzUser.setZoomView(zoomView);
        mPtzUser.setScrollContentView(contentView);
    }

    @Override
    protected void initData() {
        super.initData();
        updateHeadView(UserManager.hasLogin());
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) { return; }
        switch (requestCode) {
            case REGISTER:
                User user = new User();
                user.setUsername(data.getStringExtra(Key.phone));
                user.setPassword(data.getStringExtra(Key.password));
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(final User user, final BmobException e) {
                        if (e != null) {
                            Mlog.e(e.toString());
                            toast(R.string.login_fail);
                        }
                        UserManager.getInstance().saveToLocal(user);
                    }
                });
                break;
            case LOGIN:
                break;
        }
        updateHeadView(true);
    }

    private void updateHeadView(boolean hasLogin) {
        headView.setUserText(hasLogin ? UserManager.getInstance().getNickname() : Key.NIL);
        headView.updateUI(hasLogin);
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        //TODO UserMenu点击事件
    }
}
