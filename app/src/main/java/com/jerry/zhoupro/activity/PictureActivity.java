package com.jerry.zhoupro.activity;

import com.bumptech.glide.Glide;
import com.jerry.zhoupro.R;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends TitleBaseActivity {

    public static final String DATA_URL = "picture_url";
    public static final String DATA_DESC = "picture_desc";
    public static final String TRANSIT_PIC = "picture";
    @BindView(R.id.picture)
    ImageView picture;
    PhotoViewAttacher attacher;

    private String dataUrl, dataDesc;

    @Override
    protected String getTitleText() {
        return dataDesc;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_picture;
    }

    @Override
    protected void beforeViews() {
        dataUrl = getIntent().getStringExtra(DATA_URL);
        dataDesc = getIntent().getStringExtra(DATA_DESC);
    }

    @Override
    protected void initData() {
        ViewCompat.setTransitionName(picture, TRANSIT_PIC);
        Glide.with(this).load(dataUrl).into(picture);
        attacher = new PhotoViewAttacher(picture);
    }

    public static Intent newIntent(Activity activity, String url, String desc) {
        Intent intent = new Intent(activity, PictureActivity.class);
        intent.putExtra(DATA_URL, url);
        intent.putExtra(DATA_DESC, desc);
        return intent;
    }
}
