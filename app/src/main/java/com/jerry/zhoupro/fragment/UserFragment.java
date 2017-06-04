package com.jerry.zhoupro.fragment;


import java.util.ArrayList;
import java.util.List;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.LoginActivity;
import com.jerry.zhoupro.activity.RegisterActivity;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.User;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.pop.ItemPopupWindow;
import com.jerry.zhoupro.util.Mlog;
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

    private UserHeadView headView;
    private ItemPopupWindow itemPopupWindow;

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
            public void changePic() {
                if (itemPopupWindow == null) {
                    List<String> list = new ArrayList<String>();
                    list.add(getString(R.string.take_photo));
                    list.add(getString(R.string.select_photo));
                    itemPopupWindow = new ItemPopupWindow(getActivity(), list, new ItemPopupWindow.ActionLister() {
                        @Override
                        public void stringAction(final int index) {

                        }
                    });
                }
                itemPopupWindow.show();
            }

            @Override
            public void register() {
                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), Key.REGISTER);
            }

            @Override
            public void login() {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), Key.LOGIN);
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
        if (resultCode != Activity.RESULT_OK) { return; }
        switch (requestCode) {
            case Key.REGISTER:
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
            case Key.LOGIN:
                break;
        }
        updateHeadView(true);
    }

    private void updateHeadView(boolean hasLogin) {
        String  fds = UserManager.getInstance().getNickname();
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
