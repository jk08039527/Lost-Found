package com.jerry.zhoupro.adapter;

import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.bean.UserMenuBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wzl-pc on 2017/5/17.
 */

public class UserContentAdapter extends CommonAdapter<UserMenuBean> {

    public UserContentAdapter(final Context context, List<UserMenuBean> userContents) {
        super(context, userContents);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, R.layout.item_user_menu);
        ImageView iv_menu = holder.getView(R.id.iv_menu);
        TextView tv_menu = holder.getView(R.id.tv_menu);
        UserMenuBean menu = mDatas.get(position);
        iv_menu.setImageResource(menu.getInco());
        tv_menu.setText(menu.getText());
        return holder.getConvertView();
    }
}
