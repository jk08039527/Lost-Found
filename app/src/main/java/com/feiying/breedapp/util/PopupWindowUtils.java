package com.feiying.breedapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feiying.breedapp.R;

import java.util.ArrayList;

/**
 * Created by wzl on 16-6-16.
 */
public class PopupWindowUtils {
    private static PopupWindow popupWindow = null;

    public interface ActionLister {
        void stringAction(int index);
    }

    public interface ShowAndDismissListener {
        void onWindowShow();

        void onWindowDismiss();
    }

    public static class Builder {
        private Context context;
        private String title;//标题
        private ArrayList<String> stringList;//数组
        private ActionLister actionLister;//点击事件
        private ShowAndDismissListener showAndDismissListener;//显示和隐藏事件
        private TextView headTextView;
        private TextView textView;
        private TextView footTextView;
        private ColorStateList headTextColor, textColor, footTextColor;
        private float headTextSize = 16f, textSize = 16f, footTextSize = 16f;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitleSize(float size) {
            this.headTextSize = size;
            return this;
        }

        public Builder setTitleColor(ColorStateList colors) {
            this.headTextColor = colors;
            return this;
        }

        public Builder setStringList(ArrayList<String> stringList) {
            this.stringList = stringList;
            return this;
        }

        public Builder setTextViewSize(float size) {
            this.textSize = size;
            return this;
        }

        public Builder setTextViewColor(ColorStateList colors) {
            this.textColor = colors;
            return this;
        }

        public Builder setFootTextViewSize(float size) {
            this.footTextSize = size;
            return this;
        }

        public Builder setFootTextViewColor(ColorStateList colors) {
            this.footTextColor = colors;
            return this;
        }

        public Builder setActionLister(ActionLister actionLister) {
            this.actionLister = actionLister;
            return this;
        }

        public Builder setShowAndDismissListener(ShowAndDismissListener showAndDismissListener) {
            this.showAndDismissListener = showAndDismissListener;
            return this;
        }

        public void backgroundAlpha(Context context, float bgAlpha) {
            if (context instanceof Activity) {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                        .getAttributes();
                lp.alpha = bgAlpha; // 0.0-1.0
                ((Activity) context).getWindow().setAttributes(lp);
            }
        }

        public void dismiss() {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }

        public boolean isShown() {
            if (popupWindow != null && popupWindow.isShowing()) {
                return true;
            }
            return false;
        }


        public void show() {
            LinearLayout layoutView = setup();
            if (layoutView == null) return;
            View view = ((Activity) context).getWindow().peekDecorView();
            if (view != null) {
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        0);
            }

            if (showAndDismissListener != null)
                showAndDismissListener.onWindowShow();
            else
                backgroundAlpha(context, 0.6f);
            popupWindow = new PopupWindow(layoutView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (showAndDismissListener != null)
                        showAndDismissListener.onWindowDismiss();
                    else
                        backgroundAlpha(context, 1.0f);
                }
            });
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            popupWindow.showAtLocation(layoutView, Gravity.BOTTOM, 0, 0);
        }

        private LinearLayout setup() {
            if (stringList == null || stringList.size() < 1)
                return null;
            LinearLayout layoutView = new LinearLayout(context);
            layoutView.setLayoutParams(new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutView.setOrientation(LinearLayout.VERTICAL);

            if (!TextUtils.isEmpty(title)) {
                headTextView = new TextView(context);
                LinearLayout.LayoutParams headlayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                headlayoutParams.setMargins(50, 1, 50, 1);
                headTextView.setLayoutParams(headlayoutParams);
                headTextView.setBackgroundResource(R.drawable.bg_pop_up);
                headTextView.setGravity(Gravity.CENTER);
                headTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, headTextSize);
                headTextView.setTextColor(headTextColor == null ?
                        context.getResources().getColorStateList(R.color.empty_list_text_color)
                        : headTextColor);
                headTextView.setText(title);
                headTextView.setPadding(
                        PxUtil.dip2px(context, 60),
                        PxUtil.dip2px(context, 15),
                        PxUtil.dip2px(context, 60),
                        PxUtil.dip2px(context, 15));
                layoutView.addView(headTextView);
            }

            for (int i = 0; i < stringList.size(); i++) {
                textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        PxUtil.dip2px(context, 50)
                );
                layoutParams.setMargins(50, 1, 50, 1);
                textView.setLayoutParams(layoutParams);
                final int index = i;
                if (i == stringList.size() - 1) {
                    if (i == 0 && TextUtils.isEmpty(title)) {
                        textView.setBackgroundResource(R.drawable.bg_pop_cancel_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.bg_pop_bottom_selector);
                    }
                } else if (i == 0) {
                    if (!TextUtils.isEmpty(title)) {
                        textView.setBackgroundResource(R.drawable.bg_pop_rect_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.bg_pop_up_selector);
                    }
                } else {
                    textView.setBackgroundResource(R.drawable.bg_pop_rect_selector);
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        actionLister.stringAction(index);
                    }
                });
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                textView.setTextColor(textColor == null ? context.getResources().getColorStateList(R.color.poptext_blue) : textColor);
                textView.setText(stringList.get(i));
                layoutView.addView(textView);
            }

            footTextView = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    PxUtil.dip2px(context, 50)
            );
            layoutParams.setMargins(50, 10, 50, 10);
            footTextView.setLayoutParams(layoutParams);
            footTextView.setBackgroundResource(R.drawable.bg_pop_cancel_selector);
            footTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            footTextView.setGravity(Gravity.CENTER);
            footTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, footTextSize);
            footTextView.setTextColor(footTextColor == null ? context.getResources().getColorStateList(R.color.poptext_red) : footTextColor);
            footTextView.setText(context.getResources().getString(R.string.cancel));
            layoutView.addView(footTextView);
            return layoutView;
        }
    }


}

