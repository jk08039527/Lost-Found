package com.jerry.zhoupro.base;

/**
 * Created by wzl-pc on 2017/8/4
 *
 * @Description
 */

public interface BaseView {

    void showErrorMsg(String error);

    //=======state======
    void stateNormal();

    void stateError();

    void stateEmpty();

    void stateLoading();
}
