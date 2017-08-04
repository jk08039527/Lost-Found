package com.jerry.zhoupro.widget.recycler;

import com.jerry.zhoupro.base.RecyclerViewHolder;

import android.view.ViewGroup;

/**
 * Created by wzl on 2017/6/8. 类说明:StickyHeaderAdapter,根据ID判断header,header通过inflate加载
 */
public interface StickyHeaderImpl<T extends RecyclerViewHolder> {

    /**
     * Returns the header id for the item at the given position.
     */
    String getHeaderId(int position);

    /**
     * Creates a new header ViewHolder.
     */
    T onCreateHeaderViewHolder(ViewGroup parent);

    /**
     * Updates the header view to reflect the header data for the given position
     */
    void onBindHeaderViewHolder(T holder, int position);
}
