package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.util.AndroidUtils;
import com.jerry.zhoupro.util.ImeUtils;
import com.jerry.zhoupro.util.PreferenceUtil;
import com.jerry.zhoupro.view.SkipView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements SkipView.SkipListener {

    private boolean canSkip = true;// 是否能跳到首页

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferenceUtil.isFirst(PreferenceUtil.FIRST_OPEN_FOR_VERSION, AndroidUtils.getVersionCode())) {// v2.0更新icon
            PreferenceUtil.setNotFirst(PreferenceUtil.FIRST_OPEN_FOR_VERSION, AndroidUtils.getVersionCode());
//            AndroidUtils.addAppShortcutToDesktop(this);
        }

//        Bitmap bitmap = ImageLoader.getInstance().getBitmapFromSDCard(LocalDB.getPreloadAdResponse());
//        if (bitmap != null && !bitmap.isRecycled()) {
//            RelativeLayout mRootView = new RelativeLayout(this);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
//            ImageView imageView = new ImageView(this);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setImageBitmap(bitmap);
//            final InitBean initBean = LocalDB.getInitAppResponse();
//            if (initBean != null) {
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TJUtils.onEvent(MyUmengEvent.splashAdDetail);
//                        int lid = initBean.getLid();
//                        String preloadUrl = initBean.getPreloadUrl();
//                        if (!LotteryId.unknown.equals(LotteryId.find(lid, LotteryId.unknown))) {
//                            IntentUtil.goIntentLottery(SplashActivity.this, lid);
//                        } else if (URLUtil.isNetworkUrl(preloadUrl)) {
//                            IntentUtil.goWebActivity(SplashActivity.this, preloadUrl);
//                        }
//                    }
//                });
//            }
//            mRootView.addView(imageView, params);
//
//            AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1);
//            alphaAnimation.setDuration(1000);
//            imageView.startAnimation(alphaAnimation);
//
//            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(-2, -2);
//            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            params2.topMargin = getResources().getDimensionPixelSize(R.dimen.status_bar_height);
//            mRootView.addView(new SkipView(this, this), params2);
//
//            mRootView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen._110dp));
//            try {
//                setContentView(mRootView);
//            } catch (OutOfMemoryError e) {
//                e.printStackTrace();
//                start();
//            }
//
//        } else {
            goIntent();
//        }
    }

    private void goIntent() {
        if (isFinishing()) {
            return;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        start();
    }

    private void start() {
        if (!canSkip) { return; }
        Intent intent = new Intent();
//        if (PreferenceHelp.isFirst(PreferenceHelp.FIRST_INSTALLED)) {
//            PreferenceHelp.setNotFirst(PreferenceHelp.FIRST_INSTALLED);
//            intent.setClass(this, GuideActivity.class);
//        } else {
        intent.setClass(this, MainActivity.class);
//        }
        startActivity(intent);
        finish();
    }

    @Override
    public void OnSkip() {
        start();
    }

    @Override
    protected void onRestart() {
        if (!canSkip) {
            canSkip = true;
            start();
        }
        super.onRestart();
    }

    @Override
    public void onResume() {
        canSkip = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        canSkip = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ImeUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

}