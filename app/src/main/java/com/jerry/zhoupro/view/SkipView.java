package com.jerry.zhoupro.view;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.util.DisplayUtil;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SkipView extends LinearLayout {

    private SkipListener mSkipListener;

    public SkipView(Context context) {
        super(context);
    }

    public SkipView(Context context, SkipListener skipListener) {
        this(context);
        TextView skipText = new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams
                (DisplayUtil.dip2px(60), DisplayUtil.dip2px(32));
        skipText.setLayoutParams(params);
        skipText.setBackgroundResource(R.drawable.skip_shape_bg);
        skipText.setText(R.string.skip);
        skipText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        skipText.setTextColor(ContextCompat.getColor(context, R.color.skip_text_color));
        addView(skipText);

        this.mSkipListener = skipListener;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != mSkipListener) {
                    mSkipListener.OnSkip();
                }
            }
        }, 3000);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mSkipListener) {
                    mSkipListener.OnSkip();
                }
                mSkipListener = null;
            }
        });
    }

    public interface SkipListener {

        void OnSkip();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mSkipListener = null;
    }
}