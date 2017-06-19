package com.handmark.pulltorefresh.library.view;

import com.handmark.pulltorefresh.library.OnLoadMoreListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.extras.WeakHandler;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by wzl-pc on 16/9/6. 类说明:ListView刷新封装
 */
public class StatePtrListView extends FrameLayout implements AbsListView.OnScrollListener {

    public PullToRefreshListView getRefreshableView() {
        return mListView;
    }

    private PullToRefreshListView mListView;
    private View footerView;
    private WeakHandler mWeakHandler;
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mLastItemVisible;

    public StatePtrListView(Context context) {
        this(context, null);
    }

    public StatePtrListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(0);
    }

    public StatePtrListView(Context context, int layoutId) {
        super(context);
        init(layoutId);
    }

    private void init(int layoutId) {
        if (layoutId == 0) {
            View.inflate(getContext(), R.layout.refresh_listview, this);
        } else {
            View.inflate(getContext(), layoutId, this);
        }
        mListView = (PullToRefreshListView) findViewById(R.id.listView);
        mListView.setOnScrollListener(this);
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void setOnRefreshListener(PullToRefreshListView.OnRefreshListener<ListView> onRefreshListener) {
        mListView.setOnRefreshListener(onRefreshListener);
    }

    public void setAdapterOnLoadMore(ListAdapter adapter, OnLoadMoreListener listener) {
        addFooterView();
        setAdapter(adapter);
        removeFooterView();
        mLoadMoreListener = listener;
    }

    /**
     * 去除下划线
     */
    public void cancelDivider() {
        mListView.getRefreshableView().setDividerHeight(0);
    }

    public void addFooterView() {
        if (getFooterViewsCount() > 0) {
            return;
        }
        if (footerView == null) {
            footerView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        }
        mListView.getRefreshableView().addFooterView(footerView, null, false);
    }

    public void removeFooterView() {
        if (getFooterViewsCount() > 0) {
            mListView.getRefreshableView().removeFooterView(footerView);
        }
    }

    public int getFooterViewsCount() {
        return mListView.getRefreshableView().getFooterViewsCount();
    }

    /**
     * @param isFinish true：没有更多记录
     */
    public void setFooterViewStatus(boolean isFinish) {
        if (footerView == null) {
            return;
        }
        if (isFinish) {
            footerView.findViewById(R.id.loading_view).setVisibility(View.GONE);
            footerView.findViewById(R.id.no_more_view).setVisibility(View.VISIBLE);
        } else {
            footerView.findViewById(R.id.loading_view).setVisibility(View.VISIBLE);
            footerView.findViewById(R.id.no_more_view).setVisibility(View.GONE);
        }
    }

    public void onRefreshComplete() {
        if (mWeakHandler == null) {
            mWeakHandler = new WeakHandler();
        }
        mWeakHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mListView != null) {
                    mListView.onRefreshComplete();
                }
            }
        }, 300);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLoadMoreListener != null && mLastItemVisible) {
            mLoadMoreListener.onLoadMore();
        }

        if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {
            View currentFocus = ((Activity) getContext()).getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mLoadMoreListener != null) {
            mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
        }
    }
}
