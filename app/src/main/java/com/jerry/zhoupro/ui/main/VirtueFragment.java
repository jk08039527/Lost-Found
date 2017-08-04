package com.jerry.zhoupro.ui.main;


import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.CommonAdapter;
import com.jerry.zhoupro.base.TitleBaseFragment;
import com.jerry.zhoupro.model.bean.ThingInfoBean;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.util.Mlog;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class VirtueFragment extends TitleBaseFragment implements AdapterView.OnItemClickListener {

    private CommonAdapter<ThingInfoBean> mAdapter;
    private List<ThingInfoBean> mFindInfos = new ArrayList<>();
    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshList;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_virtue;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.virtue);
    }

    @Override
    public void initView(final View view) {
        super.initView(view);
        setGone(titleBack);
        mAdapter = new VirtueAdapter(getActivity(), mFindInfos);
        mPullRefreshList.setAdapter(mAdapter);
        mPullRefreshList.setOnItemClickListener(this);
        mPullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        BmobQuery<ThingInfoBean> query = new BmobQuery<>(ThingInfoBean.class.getSimpleName());
        query.addWhereEqualTo(Key.RELEASE_TYPE, Key.TAG_RELEASE_FIND);
        query.setLimit(10);
        query.order("-" + Key.CREATEDAT);
        query.include(Key.RELEASER);// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<ThingInfoBean>() {
            @Override
            public void done(final List<ThingInfoBean> list, final BmobException e) {
                if (e != null) {
                    Mlog.e(e.toString());
                    toast(R.string.error);
                    return;
                }
                mFindInfos.clear();
                mFindInfos.addAll(list);
                mAdapter.notifyDataSetChanged();
                mPullRefreshList.onRefreshComplete();
            }
        });
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        Intent intent = new Intent(getActivity(), FindDetailActivity.class);
        intent.putExtra(Key.THING_INFO, mFindInfos.get(position - 1));
        startActivity(intent);
    }
}
