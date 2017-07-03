package com.jerry.zhoupro.activity;

import com.bumptech.glide.Glide;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.bean.ThingInfoBean;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.widget.CircleImageView;

import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by wzl-pc on 2017/7/2.
 */

public class FindDetailActivity extends TitleBaseActivity {

    @BindView(R.id.iv_releaser)
    CircleImageView mIvReleaser;
    @BindView(R.id.tv_releaser_nickname)
    TextView mTvReleaserNickname;
    @BindView(R.id.iv_happen_pic)
    ImageView mIvHappenPic;
    @BindView(R.id.tv_release_title)
    TextView mTvReleaseTitle;
    @BindView(R.id.tv_content)
    TextView mTvReleaseContent;
    private ThingInfoBean mThingInfo;

    @Override
    protected String getTitleText() {
        return getString(R.string.find_detail);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_find_detail;
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        mThingInfo = (ThingInfoBean) getIntent().getSerializableExtra(Key.THING_INFO);
    }

    @Override
    protected void initData() {
        Glide.with(this).load(mThingInfo.getReleaser().getPhotoUri()).into(mIvReleaser);
        Glide.with(this).load(mThingInfo.getPictures().get(0).getFileUrl()).into(mIvHappenPic);
        mTvReleaserNickname.setText(mThingInfo.getReleaser().getNickname());
        mTvReleaseTitle.setText(mThingInfo.getTitle());
        mTvReleaseContent.setText(mThingInfo.getContent());
    }
}
