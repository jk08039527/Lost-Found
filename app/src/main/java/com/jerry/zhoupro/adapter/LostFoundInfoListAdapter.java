package com.jerry.zhoupro.adapter;

import java.util.List;

import com.jerry.zhoupro.bean.ThingInfoBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public class LostFoundInfoListAdapter extends CommonAdapter<ThingInfoBean> {

    public LostFoundInfoListAdapter(final Context context, final List<ThingInfoBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        return null;
    }
}
