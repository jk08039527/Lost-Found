package com.jerry.zhoupro.widget;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.util.DataCleanUtils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzl-pc on 2017/5/17.
 */

public class UserContentView extends LinearLayout {

    public static final int MENU_APP_SHARE = 0;
    public static final int MENU_FEEDBACK = 1;
    public static final int MENU_UPDATE = 2;
    public static final int MENU_CLEAR_CATCH = 3;
    public static final int MENU_ABOUT_US = 4;
    @BindView(R.id.tv_cache_size)
    TextView mTvCacheSize;
    private ContentClickListener mContentClickListener;

    public UserContentView(final Context context, final ContentClickListener contentClickListener) {
        super(context, null);
        mContentClickListener = contentClickListener;
        View.inflate(context, R.layout.profile_content_view, this);
        ButterKnife.bind(this);
        updateCatchText();
    }

    public void updateCatchText() {
        mTvCacheSize.setText(DataCleanUtils.getInstance(getContext()).getCatchSize());
    }

    @OnClick({R.id.tv_app_share, R.id.tv_feedback, R.id.tv_check_update, R.id.ll_clear_cache, R.id.tv_about_us})
    public void onViewClicked(View view) {
        if (mContentClickListener == null) { return; }
        switch (view.getId()) {
            case R.id.tv_app_share:
                mContentClickListener.appShareClick();
                break;
            case R.id.tv_feedback:
                mContentClickListener.feedbackClick();
                break;
            case R.id.tv_check_update:
                mContentClickListener.checkUpdateClick();
                break;
            case R.id.ll_clear_cache:
                mContentClickListener.clearCacheClick();
                break;
            case R.id.tv_about_us:
                mContentClickListener.aboutUsClick();
                break;
            default:
                break;
        }
    }

    public interface ContentClickListener {

        void appShareClick();

        void feedbackClick();

        void checkUpdateClick();

        void clearCacheClick();

        void aboutUsClick();
    }
}
