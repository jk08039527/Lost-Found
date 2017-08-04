package com.jerry.zhoupro.ui.about;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.TitleBaseActivity;
import com.jerry.zhoupro.util.AndroidUtils;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends TitleBaseActivity {

    @BindView(R.id.version_tv)
    TextView mVersionTv;
    @BindView(R.id.tv_website_wzl)
    TextView mTvWebsiteWzl;
    @BindView(R.id.tv_website_zb)
    TextView mTvWebsiteZb;

    @Override
    protected String getTitleText() {
        return getString(R.string.about_us);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {
        mVersionTv.setText(getString(R.string.version_info, AndroidUtils.getVersionName()));
        mTvWebsiteWzl.setText("微博：@小Li子不会玩儿");
        mTvWebsiteZb.setText("微博：@最后一颗子弹留给庄周");
    }

    @Override
    @OnClick({R.id.tv_website_wzl, R.id.tv_website_zb})
    public void onClick(final View v) {
        Uri uri;
        Intent it;
        switch (v.getId()) {
            case R.id.tv_website_wzl:
                uri = Uri.parse("http://weibo.com/zaijianniyiyan?refer_flag=1001030101");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
            case R.id.tv_website_zb:
                uri = Uri.parse("http://weibo.com/wangyouwoaini?refer_flag=1001030101");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
            default:
                super.onClick(v);
                break;
        }
    }
}
