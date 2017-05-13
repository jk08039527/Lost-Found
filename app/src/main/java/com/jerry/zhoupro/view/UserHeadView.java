package com.jerry.zhoupro.view;

import com.jerry.zhoupro.R;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wzl-pc on 2017/5/13.
 */

public class UserHeadView extends LinearLayout {

    protected Context mContext;

    public UserHeadView(final Context context) {
        super(context);
        mContext = context;
        View.inflate(mContext, R.layout.profile_head_view, this);

    }

}
