package com.jerry.zhoupro.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.bean.ThingInfoBean;
import com.jerry.zhoupro.databinding.ItemFindBinding;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wzl-pc on 2017/7/1.
 */

public class FindAdapter extends CommonAdapter<ThingInfoBean> {

    private LayoutInflater inflater;
    private Activity mActivity;

    public FindAdapter(final Context context, final List<ThingInfoBean> datas) {
        super(context, datas);
        inflater = LayoutInflater.from(context);
        mActivity = (Activity) context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ItemFindBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.item_find, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemFindBinding) convertView.getTag();
        }
        final ThingInfoBean thing = mDatas.get(position);
        binding.setThing(thing);
        String photoUrl = thing.getReleaser().getPhotoUri();
        if (!TextUtils.isEmpty(photoUrl)) {
            Glide.with(mActivity).load(photoUrl).into(binding.ivReleaser);
        }
        List<BmobFile> files = thing.getPictures();
        if (files != null) {
            String picUrl = files.get(0).getFileUrl();
            Glide.with(mActivity).load(picUrl).into(binding.ivHappenPic);
        }
        return convertView;
    }
}
