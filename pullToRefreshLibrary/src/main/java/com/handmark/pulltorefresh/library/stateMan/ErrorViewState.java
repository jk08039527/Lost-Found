package com.handmark.pulltorefresh.library.stateMan;

import android.view.View;

/**
 * Created by wzl-pc on 2017/4/13.
 */

public class ErrorViewState implements ViewState {

    private View.OnClickListener mClickListener;

    public ErrorViewState(final View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public void show(final StateLayout stateLayout) {
        stateLayout.showErrorView(mClickListener);
    }
}
