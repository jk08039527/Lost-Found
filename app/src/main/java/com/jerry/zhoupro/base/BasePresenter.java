package com.jerry.zhoupro.base;

import android.view.View;

/**
 * Created by wzl-pc on 2017/8/4
 *
 * @Description
 */

public interface BasePresenter {

    void attachView(View view);

    void detachView();
}
