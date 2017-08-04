package com.jerry.zhoupro.ui.main;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.TitleBaseActivity;
import com.jerry.zhoupro.model.bean.ThingInfoBean;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.widget.CircleImageView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.bmob.v3.datatype.BmobFile;

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
        Glide.with(this).load(mThingInfo.getReleaser().getPhotoUri())
                .transition(new DrawableTransitionOptions().dontTransition())
                .thumbnail(Glide.with(this).load(R.drawable.ic_img_user_default))
                .into(mIvReleaser);
        List<BmobFile> files = mThingInfo.getPictures();
        if (files != null && files.size() > 0) {
            Glide.with(this).load(files.get(0).getFileUrl()).into(mIvHappenPic);
        } else {
            mIvHappenPic.setVisibility(View.GONE);
        }
        mTvReleaserNickname.setText(mThingInfo.getReleaser().getNickname());
        mTvReleaseTitle.setText(mThingInfo.getTitle());
        mTvReleaseContent.setText(mThingInfo.getContent());
    }
}
