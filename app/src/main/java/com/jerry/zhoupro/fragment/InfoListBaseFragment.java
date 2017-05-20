package com.jerry.zhoupro.fragment;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.adapter.CommonAdapter;
import com.jerry.zhoupro.adapter.LostFoundInfoListAdapter;
import com.jerry.zhoupro.command.Constants;
import com.jerry.zhoupro.data.LostFoundInfo;

import android.view.View;

import butterknife.BindView;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public abstract class InfoListBaseFragment extends BaseFragment {

    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshList;
    protected CommonAdapter<LostFoundInfo> mAdapter;
    protected ArrayList<LostFoundInfo> mLostFoundInfos = new ArrayList<>();
    private int type;// Lost 0；Found 1；

    public static InfoListBaseFragment newInstance(int type) {
        InfoListBaseFragment fragment;
        if (type == Constants.LOST) {
            fragment = new InfoListLostFragment();
        } else {
            fragment = new InfoListFoundFragment();
        }
        fragment.type = type;
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_info_list;
    }

    @Override
    public void initView(final View view) {
        super.initView(view);
        mAdapter = new LostFoundInfoListAdapter(getContext(), mLostFoundInfos);
        mPullRefreshList.setAdapter(mAdapter);
    }
}
