package com.jerry.zhoupro.ui.test;

import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.RecyclerViewHolder;
import com.jerry.zhoupro.widget.recycler.BaseStickyRecyclerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wzl-pc on 2017/7/14.
 */

public class TextRecyclerAdapter extends BaseStickyRecyclerAdapter<String> {

    public TextRecyclerAdapter(final Context context, final List<String> data) {
        super(context, data, android.R.layout.simple_list_item_1);
    }

    @Override
    public String getHeaderId(final int position) {
        return mData.get(Math.min(position, mData.size() - 1));
    }

    @Override
    public RecyclerViewHolder onCreateHeaderViewHolder(final ViewGroup parent) {
        return new RecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.time_stamp_view, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerViewHolder holder, final int position) {
        TextView dateTv = holder.getView(R.id.date_tv);
        dateTv.setText(mData.get(position));
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final int position, final String bean) {
        TextView textView = holder.getView(android.R.id.text1);
        textView.setText(mData.get(position));
    }
}
