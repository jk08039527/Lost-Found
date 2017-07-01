package com.jerry.zhoupro.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.LoginActivity;
import com.jerry.zhoupro.activity.PictureActivity;
import com.jerry.zhoupro.bean.ThingInfoBean;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.databinding.ItemLostFoundBinding;
import com.jerry.zhoupro.util.ShareUtils;
import com.jerry.zhoupro.widget.NoticeDialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public class LostFoundInfoListAdapter extends CommonAdapter<ThingInfoBean> {

    private LayoutInflater inflater;
    private Activity mActivity;
    private int mType;
    private NoticeDialog mNoticeDialog;

    public LostFoundInfoListAdapter(final Context context, int type, final List<ThingInfoBean> datas) {
        super(context, datas);
        inflater = LayoutInflater.from(context);
        mActivity = (Activity) context;
        mType = type;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ItemLostFoundBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.item_lost_found, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemLostFoundBinding) convertView.getTag();
        }
        final ThingInfoBean thing = mDatas.get(position);
        binding.setThing(thing);
        binding.setMyClick(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.iv_call:
                    case R.id.iv_sms:
                        if (UserManager.hasLogin()) {
                            final String phone = thing.getReleaser().getMobilePhoneNumber();
                            if (mNoticeDialog == null) {
                                mNoticeDialog = new NoticeDialog(mActivity);
                            }
                            mNoticeDialog.show();
                            mNoticeDialog.setTitleText(R.string.remind);
                            boolean isCall = v.getId() == R.id.iv_call;
                            mNoticeDialog.setMessage(isCall
                                    ? mActivity.getString(R.string.call_to_phone, phone)
                                    : mActivity.getString(R.string.sms_to_phone, phone));
                            mNoticeDialog.setPositiveButtonListener(new CallOrSmsClickListener(phone, isCall ? 0 : 1));
                        } else {
                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                        }
                        break;
                    case R.id.iv_share:
                        if (UserManager.hasLogin()) {
                            ShareUtils.share(mActivity,
                                    "http:www.baidu.com/",
                                    mType == 0 ? mActivity.getString(R.string.share_lost) : mActivity.getString(R.string.share_found),
                                    thing.getTitle(),
                                    thing.getContent(),
                                    thing.getPictures().get(0).getUrl());
                        } else {
                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                        }
                        break;
                    case R.id.iv_happen_pic:
                        Intent intent = PictureActivity.newIntent(mActivity, thing.getPictures().get(0).getUrl(), thing.getTitle());
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                                v, PictureActivity.TRANSIT_PIC);
                        ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
                        break;
                    default:
                        break;
                }
            }
        });
        String photoUrl = thing.getReleaser().getPhotoUri();
        if (!TextUtils.isEmpty(photoUrl)) {
            Glide.with(mActivity).load(photoUrl).into(binding.ivReleaser);
        }
        List<BmobFile> files = thing.getPictures();
        if (files != null) {
            String picUrl = files.get(0).getFileUrl();
            Glide.with(mActivity).load(picUrl).into(binding.ivHappenPic);
        }
        return convertView;
    }

    private class CallOrSmsClickListener implements View.OnClickListener {

        private String phone;
        private int type;//0：电话，1：短信

        CallOrSmsClickListener(final String phone, final int type) {
            this.phone = phone;
            this.type = type;
        }

        @Override
        public void onClick(final View v) {
            if (mNoticeDialog.isShowing()) { mNoticeDialog.dismiss(); }
            Intent intent;
            if (type == 0) {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            } else {
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                intent.putExtra("sms_body", "Nice to Meet you！");
            }
            mActivity.startActivity(intent);
        }
    }
}
