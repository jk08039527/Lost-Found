package com.jerry.zhoupro.widget.recycler;

import com.jerry.zhoupro.base.RecyclerViewHolder;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wzl on 2017/6/26. 类说明:时间轴效果(投注记录、账户记录)
 */
public class TimeStickyDecoration extends RecyclerView.ItemDecoration {

	private BaseStickyRecyclerAdapter mAdapter;

	public TimeStickyDecoration(BaseStickyRecyclerAdapter adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		final int childCount = parent.getChildCount();
		for (int layoutPos = 0; layoutPos < childCount; layoutPos++) {
			final View child = parent.getChildAt(layoutPos);
			final int adapterPos = parent.getChildAdapterPosition(child);
			if (adapterPos == RecyclerView.NO_POSITION) {
				return;
			}
			// 只有在最上面一个item或者有header的item才绘制ItemDecoration
			if (layoutPos == 0 || hasHeader(adapterPos)) {
				View header = getHeader(parent, adapterPos).itemView;
				c.save();
				int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
				c.translate(0, top);
				header.draw(c);
				c.restore();
			}

		}
	}

	/**
	 * 计算距离顶部的高度
	 */
	private int getHeaderTop(RecyclerView parent, View child, View header, int adapterPos, int layoutPos) {
		int headerHeight = header.getHeight();
		int top = child.getTop();
		// 处理最上面两个ItemDecoration切换时
		if (layoutPos == 0) {
			final int count = parent.getChildCount();
			final String currentId = mAdapter.getHeaderId(adapterPos);
			for (int i = 1; i < count; i++) {
				int adapterPosNext = parent.getChildAdapterPosition(parent.getChildAt(i));
				if (adapterPosNext == RecyclerView.NO_POSITION) {
					continue;
				}
				String nextId = mAdapter.getHeaderId(adapterPosNext);
				if (!nextId.equals(currentId)) {
					View next = parent.getChildAt(i);
					int offset = next.getTop() - headerHeight;
					if (offset < 0) {// sticky效果，
						return offset;
					} else {
						break;
					}
				}
			}
			// top不能小于0，否则最上面的ItemDecoration不会一直存在
			top = Math.max(0, top);
		}
		return top;
	}

	/**
	 * 判断是否有header
	 */
	private boolean hasHeader(int position) {
		if (position == 0) {// 第一个位置必然有
			return true;
		}
		// 判断和上一个的id不同则有header
		int previous = position - 1;
		return !mAdapter.getHeaderId(position).equals(mAdapter.getHeaderId(previous));
	}

	/**
	 * 获得自定义的Header
	 */
	@SuppressWarnings("unchecked")
	private RecyclerView.ViewHolder getHeader(RecyclerView parent, int position) {
		// 创建HeaderViewHolder
		RecyclerViewHolder holder = mAdapter.onCreateHeaderViewHolder(parent);
		final View header = holder.itemView;
		// 绑定数据
		mAdapter.onBindHeaderViewHolder(holder, position);
		// 测量View并且layout
		int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
		int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);
		// 根据父View的MeasureSpec和子view自身的LayoutParams以及padding来获取子View的MeasureSpec
		int childWidth = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), header.getLayoutParams().width);
		int childHeight = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), header.getLayoutParams().height);
		// 进行测量
		header.measure(childWidth, childHeight);
		// 根据测量后的宽高放置位置
		header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
		return holder;
	}

}
