package com.handmark.pulltorefresh.library.stateMan;

/**
 * Created by wzl-pc on 2017/4/13
 */

public class NormalViewState implements ViewState {

    @Override
    public void show(final StateLayout stateLayout) {
        stateLayout.showNormalView();
    }
}
