package com.jerry.zhoupro.base;

import com.handmark.pulltorefresh.library.stateMan.EmptyViewState;
import com.handmark.pulltorefresh.library.stateMan.ErrorViewState;
import com.handmark.pulltorefresh.library.stateMan.NormalViewState;
import com.handmark.pulltorefresh.library.stateMan.StateLayout;
import com.handmark.pulltorefresh.library.stateMan.ViewState;
import com.jerry.zhoupro.R;

import android.view.View;

public abstract class SimpleStateBaseActivity extends TitleBaseActivity {

    protected StateLayout mStateLayout;
    protected ViewState mCurrentState;
    protected ViewState mNormalState;
    protected ViewState mErrorState;
    protected EmptyViewState mEmptyState;

    @Override
    public void initView() {
        super.initView();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setCurrentState(mNormalState);
                loadingDialog();
                initData();
            }
        };
        mNormalState = new NormalViewState();
        mErrorState = new ErrorViewState(onClickListener);
        mEmptyState = new EmptyViewState(onClickListener);
        mCurrentState = mNormalState;
        mStateLayout = (StateLayout) findViewById(R.id.state_layout);
    }

    public void setCurrentState(final ViewState currentState) {
        if (mStateLayout != null) {
            mCurrentState = currentState;
            mCurrentState.show(mStateLayout);
        }
    }
}
