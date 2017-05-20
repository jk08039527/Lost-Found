package com.jerry.zhoupro.fragment;

import com.jerry.zhoupro.data.LostFoundInfo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public class InfoListLostFragment extends InfoListBaseFragment {

    @Override
    protected void initData() {
        super.initData();
        BmobQuery<LostFoundInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.doSQLQuery(new SQLQueryListener<LostFoundInfo>() {
            @Override
            public void done(final BmobQueryResult<LostFoundInfo> bmobQueryResult, final BmobException e) {

            }
        });
    }
}
