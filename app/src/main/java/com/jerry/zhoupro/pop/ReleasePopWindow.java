package com.jerry.zhoupro.pop;

import java.lang.ref.WeakReference;

import com.jerry.zhoupro.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReleasePopWindow extends PopupWindow {

    private WeakReference<Activity> activity;

    public ReleasePopWindow(Activity activity) {
        super(activity);
        this.activity = new WeakReference<>(activity);
        init(activity);
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

    public void show() {
        super.showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.tv_release_lost, R.id.tv_release_found, R.id.iv_cancel/*, R.id.out_side_view*/})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_release_lost:
                break;
            case R.id.tv_release_found:
                break;
            case R.id.iv_cancel:
//            case R.id.out_side_view:
                dismiss();
                break;
        }
    }
}
