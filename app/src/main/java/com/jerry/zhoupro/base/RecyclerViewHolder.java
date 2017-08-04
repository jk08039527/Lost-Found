package com.jerry.zhoupro.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by wzl on 2017/4/21. 类说明:RecyclerViewHolder封装
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

	private SparseArray<View> views;// 默认大小是10

	public RecyclerViewHolder(View itemView) {
		super(itemView);
		views = new SparseArray<>();
	}

	public static RecyclerViewHolder createViewHolder(View itemView) {
		return new RecyclerViewHolder(itemView);
	}

	/**
	 * 获取view
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = itemView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

}
