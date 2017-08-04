package com.jerry.zhoupro.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wzl on 2017/4/21. 类说明:RecyclerView公共adapter
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected Context mContext;
    protected List<T> mData;
    private int mLayoutId;
    protected OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this(context, data, 0);
    }

    public BaseRecyclerAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
        this.mLayoutId = layoutId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return RecyclerViewHolder.createViewHolder(mHeaderViews.get(viewType));

        } else if (mFooterViews.get(viewType) != null) {
            return RecyclerViewHolder.createViewHolder(mFooterViews.get(viewType));
        }
        final RecyclerViewHolder holder = new RecyclerViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, null));
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition() - getHeadersCount());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition() - getHeadersCount());
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return super.getItemViewType(position - getHeadersCount());
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            return;
        }
        if (position - getHeadersCount() >= getItemCount()) {
            return;
        }
        convert(holder, position, mData.get(position - getHeadersCount()));
    }

    public abstract void convert(RecyclerViewHolder holder, int position, T bean);

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    public int getRealItemCount() {
        return mData.size();
    }

    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void delete(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(T item) {
        int position = mData.indexOf(item);
        delete(position);
    }

    public void swap(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public T getItemAtPosition(int position) {
        return (mData == null || position < 0 || position >= mData.size()) ? null : mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(View itemView, int position);
    }

    public interface OnItemLongClickListener {

        void onItemLongClick(View itemView, int position);
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public void removeFooterView() {
        mFooterViews.remove(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderViewPos(position) || isFooterViewPos(position)) ? manager.getSpanCount() : 1;
                }
            });
            manager.setSpanCount(manager.getSpanCount());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

}
