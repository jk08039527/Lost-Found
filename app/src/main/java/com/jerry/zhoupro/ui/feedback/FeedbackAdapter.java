package com.jerry.zhoupro.ui.feedback;

import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.CommonAdapter;
import com.jerry.zhoupro.base.ViewHolder;
import com.jerry.zhoupro.model.bean.FeedbackBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedbackAdapter extends CommonAdapter<FeedbackBean> {

    public FeedbackAdapter(final Context context, final List<FeedbackBean> datas) {
        super(context, datas);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = ViewHolder.get(mContext, convertView, R.layout.feedback_list_item);
        FeedbackBean info = mDatas.get(position);

        TextView userFeedbackTimeTv = mHolder.getView(R.id.user_feedback_time_tv);
        TextView userFeedbackContentTv = mHolder.getView(R.id.user_feedback_content_tv);
        ImageView iv_agree = mHolder.getView(R.id.iv_agree);

        userFeedbackTimeTv.setText(info.getCreatedAt());
        userFeedbackContentTv.setText(info.getContent());

        if (info.isAgree()) {
            iv_agree.setVisibility(View.VISIBLE);
        }
        if (mDatas.size() - 1 == position) {
            mHolder.getView(R.id.add_view).setVisibility(View.VISIBLE);
        } else {
            mHolder.getView(R.id.add_view).setVisibility(View.GONE);
        }
        return mHolder.getConvertView();
    }

}
