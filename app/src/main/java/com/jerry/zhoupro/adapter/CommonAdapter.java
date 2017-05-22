package com.jerry.zhoupro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

    Context mContext;
    List<T> mDatas;

    public CommonAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
        if (null == mDatas) {
            mDatas = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return null == mDatas ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}