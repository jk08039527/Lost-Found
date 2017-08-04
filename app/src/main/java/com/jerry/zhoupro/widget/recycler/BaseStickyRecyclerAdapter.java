package com.jerry.zhoupro.widget.recycler;

import java.util.List;

import com.jerry.zhoupro.base.BaseRecyclerAdapter;

import android.content.Context;

/**
 * Created by wzl on 2017/6/8
 */
public abstract class BaseStickyRecyclerAdapter<T> extends BaseRecyclerAdapter<T> implements StickyHeaderImpl {

	public BaseStickyRecyclerAdapter(final Context context, final List<T> data, int layoutId) {
		super(context, data, layoutId);
	}

}
