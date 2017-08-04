package com.jerry.zhoupro.ui.feedback;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.view.StatePtrListView;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.CommonAdapter;
import com.jerry.zhoupro.base.SimpleStateBaseActivity;
import com.jerry.zhoupro.model.bean.FeedbackBean;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.model.bean.UserManager;
import com.jerry.zhoupro.util.Mlog;

import android.widget.ListView;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wzl-pc on 2017/6/19.
 */

public class UserFeedbackActivity extends SimpleStateBaseActivity {

    @BindView(R.id.lv_feedback)
    StatePtrListView mLvFeedback;
    private List<FeedbackBean> mFeedbacks = new ArrayList<>();
    private CommonAdapter<FeedbackBean> mAdapter;

    @Override
    protected String getTitleText() {
        return getString(R.string.user_feedback);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_feedback;
    }

    @Override
    public void initView() {
        super.initView();
        mAdapter = new FeedbackAdapter(this, mFeedbacks);
        mLvFeedback.cancelDivider();
        mLvFeedback.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(final PullToRefreshBase refreshView) {
                setCurrentState(mNormalState);
                initData();
            }
        });
        mLvFeedback.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        BmobQuery<FeedbackBean> query = new BmobQuery<>();
        query.addWhereEqualTo(Key.USER, UserManager.getInstance().getUser());
        query.setLimit(10);
        query.order("-" + Key.CREATEDAT);
        query.findObjects(new FindListener<FeedbackBean>() {

            @Override
            public void done(final List<FeedbackBean> list, final BmobException e) {
                closeLoadingDialog();
                mLvFeedback.onRefreshComplete();
                if (e != null) {
                    Mlog.e(e.toString());
                    toast(R.string.error);
                    setCurrentState(mErrorState);
                    return;
                }
                if (list.size() == 0) {
                    setCurrentState(mEmptyState);
                } else {
                    mFeedbacks.clear();
                    mFeedbacks.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}