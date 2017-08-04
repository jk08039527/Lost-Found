package com.jerry.zhoupro.widget;

import com.jerry.zhoupro.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReleasePopWindow extends PopupWindow {

    private PopMenuClickListener mPopMenuClickListener;
    private View mBackground;

    public ReleasePopWindow(Activity activity, PopMenuClickListener popMenuClickListener) {
        super(activity);
        init(activity);
        initBackground(activity);
        mPopMenuClickListener = popMenuClickListener;
    }

    private void init(Activity activity) {
        final View view = activity.getLayoutInflater().inflate(R.layout.llayout_release, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        setContentView(view);
        this.setAnimationStyle(R.style.popwin_anim_style);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                if (!view.dispatchTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
    }

    public void initBackground(Activity activity) {
        FrameLayout frameLayout = (FrameLayout) (activity).findViewById(android.R.id.content);
        mBackground = new View(activity);
        mBackground.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBackground.setBackgroundColor(ContextCompat.getColor(activity, R.color.black_60alpha));
        frameLayout.addView(mBackground);
    }

    public void show() {
        mBackground.setVisibility(View.VISIBLE);
        super.showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void dismiss() {
        mBackground.setVisibility(View.GONE);
        super.dismiss();
    }

    @OnClick({R.id.tv_release_lost, R.id.tv_release_found, R.id.tv_release_find})
    public void onClick(View view) {
        dismiss();
        if (mPopMenuClickListener != null) {
            dismiss();
            mPopMenuClickListener.onPopMenuClick(view);
        }
    }

    public void setPopMenuClickListener(final PopMenuClickListener popMenuClickListener) {
        mPopMenuClickListener = popMenuClickListener;
    }

    public interface PopMenuClickListener {

        void onPopMenuClick(View view);
    }
}
