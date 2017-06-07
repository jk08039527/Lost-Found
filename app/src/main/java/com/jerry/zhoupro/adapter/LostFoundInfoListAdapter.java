package com.jerry.zhoupro.adapter;

import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.bean.ThingInfoBean;
import com.jerry.zhoupro.databinding.ItemLostFoundBinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public class LostFoundInfoListAdapter extends CommonAdapter<ThingInfoBean> {

    private LayoutInflater inflater;
    public LostFoundInfoListAdapter(final Context context, final List<ThingInfoBean> datas) {
        super(context, datas);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ItemLostFoundBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.item_lost_found, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemLostFoundBinding) convertView.getTag();
        }
        binding.setThing(mDatas.get(position));
        return convertView;
    }
}
