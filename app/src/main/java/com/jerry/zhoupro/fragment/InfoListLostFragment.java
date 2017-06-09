package com.jerry.zhoupro.fragment;

import com.jerry.zhoupro.bean.ThingInfoBean;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public class InfoListLostFragment{

    protected void initData() {
        BmobQuery<ThingInfoBean> bmobQuery = new BmobQuery<>();
        bmobQuery.doSQLQuery(new SQLQueryListener<ThingInfoBean>() {
            @Override
            public void done(final BmobQueryResult<ThingInfoBean> bmobQueryResult, final BmobException e) {

            }
        });
    }
}
