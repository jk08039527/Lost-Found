package com.jerry.zhoupro.util;

import com.jerry.zhoupro.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import android.app.Activity;
import android.util.Log;

/**
 * Created by wzl on 16-9-26.
 */

public class ShareUtils {

    public static void share(final Activity activity, final String text, final String picUri) {
        final UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(final SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                ToastTools.showShort(activity, R.string.umeng_share_completed);
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

//        ShareBoardConfig boardConfig = new ShareBoardConfig();
//        boardConfig.setIndicatorVisibility(false);
//        boardConfig.setMenuItemBackgroundColor(Color.TRANSPARENT);
        UMWeb web = new UMWeb("http://www.baidu.com/");
        web.setTitle("This is music title");//标题
        web.setThumb(new UMImage(activity, picUri));  //缩略图
        web.setDescription("my description");//描述
        new ShareAction(activity)
                .withText(text)
                .withMedia(web)
                .setCallback(umShareListener)
                .setDisplayList(SHARE_MEDIA.SMS, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.SINA)
                .open();

    }
}
