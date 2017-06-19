package com.handmark.pulltorefresh.library.stateMan;

import com.handmark.pulltorefresh.library.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wzl-pc on 2017/4/13  类说明：需要状态切换页面的顶层布局
 */

public class StateLayout extends FrameLayout {

    private String mEmptyText;
    private LinearLayout mEmptyView;
    private View mErrorView;

    private int mLayoutRes = R.layout.empty_default_layout;

    public StateLayout(final Context context) {
        this(context, null);
    }

    public StateLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateLayout);

            mEmptyText = typedArray.getString(R.styleable.StateLayout_empty_text);
            typedArray.recycle();
        }

        if (TextUtils.isEmpty(mEmptyText)) {
            mEmptyText = getResources().getString(R.string.null_data);
        }
    }

    void showErrorView(final OnClickListener clickListener) {
        if (mErrorView == null) {
            initErrorView(clickListener);
        }
        hideEmptyView();
        mErrorView.setVisibility(VISIBLE);
    }

    private void initErrorView(final OnClickListener clickListener) {
        ViewStub stub = (ViewStub) findViewById(R.id.error_view_stub);
        mErrorView = stub.inflate();
        mErrorView.findViewById(R.id.refresh).setOnClickListener(clickListener);
    }

    void showEmptyView(final OnClickListener listener) {
        if (mEmptyView == null) {
            initEmptyView();
        }
        if (mLayoutRes == R.layout.empty_default_layout) {
            TextView emptyText = (TextView) mEmptyView.findViewById(R.id.empty_text_view);
            emptyText.setText(mEmptyText);
            emptyText.setOnClickListener(listener);
        }
        hideErrorView();
        mEmptyView.setVisibility(VISIBLE);
    }

    private void initEmptyView() {
        ViewStub stub = (ViewStub) findViewById(R.id.empty_view_stub);
        mEmptyView = (LinearLayout) stub.inflate();
        inflate(getContext(), mLayoutRes, mEmptyView);
    }

    void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
        }
    }

    void hideErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }
    }

    public void showNormalView() {
        hideEmptyView();
        hideErrorView();
    }

    public void setEmptyText(final String emptyText) {
        mEmptyText = emptyText;
    }

    public void setEmptyLayoutRes(final int layoutRes) {
        mLayoutRes = layoutRes;
    }
}
