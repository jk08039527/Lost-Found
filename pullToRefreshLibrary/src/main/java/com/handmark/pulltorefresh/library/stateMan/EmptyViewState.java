package com.handmark.pulltorefresh.library.stateMan;

import android.view.View;

public class EmptyViewState implements ViewState {
    private View.OnClickListener mOnClickListener;

    public EmptyViewState(final View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void show(final StateLayout stateLayout) {
        stateLayout.showEmptyView(mOnClickListener);
    }

    public void setOnClickListener(final View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
