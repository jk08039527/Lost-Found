package com.jerry.zhoupro.view;

import java.util.ArrayList;
import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.adapter.UserContentAdapter;
import com.jerry.zhoupro.bean.UserMenuBean;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

/**
 * Created by wzl-pc on 2017/5/17.
 */

public class UserContentView extends MeasureGridView {

    private int[] imgRes = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };


    public UserContentView(final Context context) {
        this(context, null);
    }

    public UserContentView(final Context paramContext, final AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_bg));
        setNumColumns(2);
        List<UserMenuBean> userMenus = new ArrayList<>();
        String[] userMenuStrs = getContext().getResources().getStringArray(R.array.user_menu_title);
        for (int i = 0; i < userMenuStrs.length; i++) {
            UserMenuBean userMenu = new UserMenuBean();
            userMenu.setText(userMenuStrs[i]);
            userMenu.setInco(imgRes[i]);
            userMenus.add(userMenu);
        }
        setAdapter(new UserContentAdapter(getContext(), userMenus));
    }
}
