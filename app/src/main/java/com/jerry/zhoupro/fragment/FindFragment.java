package com.jerry.zhoupro.fragment;


import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.FindDetailActivity;
import com.jerry.zhoupro.adapter.CommonAdapter;
import com.jerry.zhoupro.adapter.FindAdapter;
import com.jerry.zhoupro.bean.ThingInfoBean;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.util.Mlog;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class FindFragment extends TitleBaseFragment implements AdapterView.OnItemClickListener {

    private CommonAdapter<ThingInfoBean> mAdapter;
    private List<ThingInfoBean> mFindInfos = new ArrayList<>();
    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshList;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_find;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.discover);
    }

    @Override
    public void initView(final View view) {
        super.initView(view);
        setGone(titleBack);
        mAdapter = new FindAdapter(getActivity(), mFindInfos);
        mPullRefreshList.setAdapter(mAdapter);
        mPullRefreshList.setOnItemClickListener(this);
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
