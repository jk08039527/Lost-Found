package com.jerry.zhoupro.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.LoginActivity;
import com.jerry.zhoupro.bean.ThingInfoBean;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.databinding.ItemLostFoundBinding;
import com.jerry.zhoupro.util.ShareUtils;
import com.jerry.zhoupro.util.ToastTools;
import com.jerry.zhoupro.widget.NoticeDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public class LostFoundInfoListAdapter extends CommonAdapter<ThingInfoBean> {

    private LayoutInflater inflater;
    private Activity mActivity;
    private NoticeDialog mNoticeDialog;

    public LostFoundInfoListAdapter(final Context context, final List<ThingInfoBean> datas) {
        super(context, datas);
        inflater = LayoutInflater.from(context);
        mActivity = (Activity) context;
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
                if (UserManager.hasLogin()) {
                    switch (v.getId()) {
                        case R.id.iv_call:
                        case R.id.iv_sms:
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
                            break;
                        case R.id.iv_share:
                            final UMShareListener umShareListener = new UMShareListener() {
                                @Override
                                public void onStart(final SHARE_MEDIA share_media) {

                                }

                                @Override
                                public void onResult(SHARE_MEDIA platform) {
                                    Log.d("plat", "platform" + platform);
                                    ToastTools.showShort(mContext, R.string.umeng_share_completed);
                                }

                                @Override
                                public void onError(SHARE_MEDIA platform, Throwable t) {
                                    ToastTools.showShort(mContext, R.string.umeng_share_failed);
                                    if (t != null) {
                                        Log.d("throw", "throw" + t.getMessage());
                                    }
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA platform) {
                                    ToastTools.showShort(mContext, R.string.umeng_share_canceled);
                                }
                            };
                            ShareUtils.share(mActivity, umShareListener, new ShareBoardlistener() {
                                @Override
                                public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                    switch (share_media) {
                                        case SINA:
                                            new ShareAction(mActivity).setPlatform(share_media)
                                                    .withText(mActivity.getString(R.string.select_photo))
                                                    .withMedia(new UMImage(mActivity, R.mipmap.ic_launcher))
                                                    .setCallback(umShareListener)
                                                    .share();
                                            break;
                                        case WEIXIN_CIRCLE:
                                            new ShareAction(mActivity).setPlatform(share_media)
                                                    .withMedia(new UMImage(mActivity, R.mipmap.ic_launcher))
                                                    .setCallback(umShareListener)
                                                    .share();
                                            break;
                                        case WEIXIN:
                                            new ShareAction(mActivity).setPlatform(share_media)
                                                    .withText(mActivity.getString(R.string.share))
                                                    .withMedia(new UMImage(mActivity, R.mipmap.ic_launcher))
                                                    .setCallback(umShareListener)
                                                    .share();
                                            break;
                                    }
                                }
                            });
                            break;
                        default:
                            break;
                    }
                } else {
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                }
            }
        });
        Glide.with(mActivity).load(thing.getReleaser().getPhotoUri()).into(binding.ivReleaser);
        Glide.with(mActivity).load(thing.getPictures().get(0).getFileUrl()).into(binding.ivHappenPic);
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
