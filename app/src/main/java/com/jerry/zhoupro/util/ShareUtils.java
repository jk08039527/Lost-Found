package com.jerry.zhoupro.util;

import com.jerry.zhoupro.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.utils.ShareBoardlistener;

import android.app.Activity;
import android.graphics.Color;

/**
 * Created by wzl on 16-9-26.
 */

public class ShareUtils {
    public static void share(final Activity activity, UMShareListener umShareListener, ShareBoardlistener shareBoardlistener) {
        ShareBoardConfig boardConfig = new ShareBoardConfig();
        boardConfig.setIndicatorVisibility(false);
        boardConfig.setMenuItemBackgroundColor(Color.TRANSPARENT);
        new ShareAction(activity)
                .withText(activity.getString(R.string.share))
                .withMedia(new UMImage(activity, R.mipmap.ic_launcher))
                .setShareboardclickCallback(shareBoardlistener)
                .setDisplayList(SHARE_MEDIA.SMS, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA)
                .setCallback(umShareListener).open(boardConfig);

    }
}
