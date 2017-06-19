package com.jerry.zhoupro.util;

import com.jerry.zhoupro.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by wzl-pc on 16-9-26.
 */

public class ShareUtils {

    public static void share(final Activity activity, final String url, final String title, final String description, final String text, final int picUri) {
        new UMImage(activity, picUri);
        UMWeb web = new UMWeb(TextUtils.isEmpty(url) ? "http://www.baidu.com/" : url);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, picUri));  //缩略图
        web.setDescription(description);//描述
        share(activity, web, text);
    }

    public static void share(final Activity activity, final String url, final String title, final String description, final String text, final String picUri) {
        new UMImage(activity, picUri);
        UMWeb web = new UMWeb(TextUtils.isEmpty(url) ? "http://www.baidu.com/" : url);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(activity, picUri));  //缩略图
        web.setDescription(description);//描述
        share(activity, web, text);
    }

    private static void share(final Activity activity, final UMWeb web, final String text) {
        final UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(final SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                if (platform != SHARE_MEDIA.SMS) {
                    ToastTools.showShort(activity, R.string.umeng_share_completed);
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                ToastTools.showShort(activity, R.string.umeng_share_failed);
                if (t != null) {
                    Log.d("throw", "throw" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                ToastTools.showShort(activity, R.string.umeng_share_canceled);
            }
        };
        new ShareAction(activity)
                .withText(text)
                .withMedia(web)
                .setCallback(umShareListener)
                .setDisplayList(SHARE_MEDIA.SMS, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.SINA)
                .open();

    }
}
