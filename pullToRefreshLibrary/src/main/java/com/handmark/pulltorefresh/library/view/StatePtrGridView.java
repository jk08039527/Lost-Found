package com.handmark.pulltorefresh.library.view;


import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.extras.WeakHandler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

/**
 * Created by wzl-pc on 16/9/6. 类说明:PtrGridView刷新封装
 */
public class StatePtrGridView extends FrameLayout {

    private PullToRefreshGridView mGridView;
    private WeakHandler mWeakHandler;

    public StatePtrGridView(Context context) {
        this(context, null);
    }

    public StatePtrGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.refresh_gridview, this);
        mGridView = (PullToRefreshGridView) findViewById(R.id.gridView);
    }

    public PullToRefreshGridView getRefreshableView() {
        return mGridView;
    }

    public void setAdapter(ListAdapter adapter) {
        mGridView.setAdapter(adapter);
    }

    public void onRefreshComplete() {
        if (mWeakHandler == null) {
            mWeakHandler = new WeakHandler();
        }
        mWeakHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGridView.onRefreshComplete();
            }
        }, 300);
    }
}
