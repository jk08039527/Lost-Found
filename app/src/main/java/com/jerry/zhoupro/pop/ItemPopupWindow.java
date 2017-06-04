package com.jerry.zhoupro.pop;

import java.util.List;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.util.DisplayUtil;
import com.jerry.zhoupro.util.ImeUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by wzl on 16-6-16.
 */
public class ItemPopupWindow extends PopupWindow {

    private Context mContext;
    private String mTitle;//标题
    private List<String> mStringList;//数组
    private ActionLister mActionLister;//点击事件
    private View mBackground;
    private ColorStateList headTextColor, textColor, footTextColor;
    private float headTextSize = 16f, textSize = 16f, footTextSize = 16f;

    public ItemPopupWindow(Context context, List<String> stringList, ActionLister actionLister) {
        this.mContext = context;
        this.mStringList = stringList;
        this.mActionLister = actionLister;
        init();
        initBackground();
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setTitleSize(float size) {
        this.headTextSize = size;
    }

    public void setTitleColor(ColorStateList colors) {
        this.headTextColor = colors;
    }

    public void setTextViewSize(float size) {
        this.textSize = size;
    }

    public void setTextViewColor(ColorStateList colors) {
        this.textColor = colors;
    }

    public void setFootTextViewSize(float size) {
        this.footTextSize = size;
    }

    public void setFootTextViewColor(ColorStateList colors) {
        this.footTextColor = colors;
    }

    private void init() {
        LinearLayout layoutView = new LinearLayout(mContext);
        layoutView.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutView.setOrientation(LinearLayout.VERTICAL);

        if (!TextUtils.isEmpty(mTitle)) {
            TextView mHeadText = new TextView(mContext);
            LinearLayout.LayoutParams headlayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            headlayoutParams.setMargins(50, 1, 50, 1);
            mHeadText.setLayoutParams(headlayoutParams);
            mHeadText.setBackgroundResource(R.drawable.bg_pop_up);
            mHeadText.setGravity(Gravity.CENTER);
            mHeadText.setTextSize(TypedValue.COMPLEX_UNIT_SP, headTextSize);
            mHeadText.setTextColor(headTextColor == null ?
                    mContext.getResources().getColorStateList(R.color.empty_list_text_color)
                    : headTextColor);
            mHeadText.setText(mTitle);
            mHeadText.setPadding(
                    DisplayUtil.dip2px(60),
                    DisplayUtil.dip2px(15),
                    DisplayUtil.dip2px(60),
                    DisplayUtil.dip2px(15));
            layoutView.addView(mHeadText);
        }

        for (int i = 0; i < mStringList.size(); i++) {
            TextView mTextItem = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    DisplayUtil.dip2px(50)
            );
            layoutParams.setMargins(50, 1, 50, 1);
            mTextItem.setLayoutParams(layoutParams);
            final int index = i;
            if (i == mStringList.size() - 1) {
                if (i == 0 && TextUtils.isEmpty(mTitle)) {
                    mTextItem.setBackgroundResource(R.drawable.bg_pop_cancel_selector);
                } else {
                    mTextItem.setBackgroundResource(R.drawable.bg_pop_bottom_selector);
                }
            } else if (i == 0) {
                if (!TextUtils.isEmpty(mTitle)) {
                    mTextItem.setBackgroundResource(R.drawable.bg_pop_rect_selector);
                } else {
                    mTextItem.setBackgroundResource(R.drawable.bg_pop_up_selector);
                }
            } else {
                mTextItem.setBackgroundResource(R.drawable.bg_pop_rect_selector);
            }
            mTextItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    mActionLister.stringAction(index);
                }
            });
            mTextItem.setGravity(Gravity.CENTER);
            mTextItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            mTextItem.setTextColor(textColor == null ? mContext.getResources().getColorStateList(R.color.poptext_blue) : textColor);
            mTextItem.setText(mStringList.get(i));
            layoutView.addView(mTextItem);
        }

        TextView mFootText = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dip2px(50)
        );
        layoutParams.setMargins(50, 10, 50, 10);
        mFootText.setLayoutParams(layoutParams);
        mFootText.setBackgroundResource(R.drawable.bg_pop_cancel_selector);
        mFootText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mFootText.setGravity(Gravity.CENTER);
        mFootText.setTextSize(TypedValue.COMPLEX_UNIT_SP, footTextSize);
        mFootText.setTextColor(footTextColor == null ? mContext.getResources().getColorStateList(R.color.poptext_red) : footTextColor);
        mFootText.setText(mContext.getResources().getString(R.string.cancel));
        layoutView.addView(mFootText);
        this.setContentView(layoutView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popwin_anim_style);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void initBackground() {
        if (mContext instanceof Activity) {
            FrameLayout frameLayout = (FrameLayout) ((Activity) mContext).findViewById(android.R.id.content);
            mBackground = new View(mContext);
            mBackground.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black_60alpha));
            frameLayout.addView(mBackground);
        }
    }

    public void show() {
        ImeUtils.hideIme((Activity) mContext);
        mBackground.setVisibility(View.VISIBLE);
        super.showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void dismiss() {
        mBackground.setVisibility(View.GONE);
        super.dismiss();
    }

    public interface ActionLister {

        void stringAction(int index);
    }
}

