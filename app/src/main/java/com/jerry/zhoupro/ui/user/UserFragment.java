package com.jerry.zhoupro.ui.user;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ecloud.pulltozoomview.PullToZoomBase;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.TitleBaseFragment;
import com.jerry.zhoupro.ui.about.AboutUsActivity;
import com.jerry.zhoupro.ui.feedback.FeedbackActivity;
import com.jerry.zhoupro.app.Constants;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.model.bean.User;
import com.jerry.zhoupro.model.bean.UserManager;
import com.jerry.zhoupro.widget.ItemPopupWindow;
import com.jerry.zhoupro.util.DataCleanUtils;
import com.jerry.zhoupro.util.FileUtils;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.model.prefs.PreferenceHelper;
import com.jerry.zhoupro.util.RxBus;
import com.jerry.zhoupro.util.ShareUtils;
import com.jerry.zhoupro.util.TimeTask;
import com.jerry.zhoupro.widget.UserContentView;
import com.jerry.zhoupro.widget.UserHeadView;
import com.jerry.zhoupro.widget.NoticeDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class UserFragment extends TitleBaseFragment {

    @BindView(R.id.ptz_user)
    PullToZoomScrollViewEx mPtzUser;
    private UserHeadView headView;
    private UserContentView contentView;
    private ItemPopupWindow itemPopupWindow;
    private String uid = Key.NIL;
    private String photoUrl;
    private String PATH_HEAD_PICTURE;
    private Subscription rxSubscription;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.me);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        setGone(titleBack);
        setGone(titleMore);
        loadViewForCode((ViewGroup) view);
    }

    private void loadViewForCode(ViewGroup view) {
        headView = new UserHeadView(getContext(), new UserHeadView.HeadClickListener() {
            @Override
            public void changePicClick() {
                if (UserManager.hasLogin()) {
                    if (itemPopupWindow == null) {
                        List<String> list = new ArrayList<>();
                        list.add(getString(R.string.take_photo));
                        list.add(getString(R.string.select_photo));
                        itemPopupWindow = new ItemPopupWindow(getActivity(), list, new ItemPopupWindow.ActionLister() {
                            @Override
                            public void stringAction(final int index) {
                                String sdStatus = Environment.getExternalStorageState();
                                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                                    Mlog.i("TestFile",
                                            "SD card is not avaiable/writeable right now.");
                                    toast(R.string.no_sdCard);
                                    return;
                                }
                                setHeadPicTemp();
                                Intent intent;
                                switch (index) {
                                    case 0://拍照
                                        if (!FileUtils.createFile(PATH_HEAD_PICTURE)) {
                                            return;
                                        }
                                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra("return-data", false);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                Uri.fromFile(new File(PATH_HEAD_PICTURE)));
                                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                                        intent.putExtra("noFaceDetection", true);
                                        startActivityForResult(intent, Key.TAKE_PHOTO);
                                        break;
                                    case 1:  //相册
                                        intent = new Intent(Intent.ACTION_PICK);
                                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                        startActivityForResult(intent, Key.PICK_PHOTO);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    }
                    itemPopupWindow.show();
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Key.LOGIN);
                }
            }

            @Override
            public void registerClick() {
                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), Key.REGISTER);
            }

            @Override
            public void loginClick() {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), Key.LOGIN);
            }

            @Override
            public void logoutClick() {
                final NoticeDialog noticeDialog = new NoticeDialog(getContext());
                noticeDialog.show();
                noticeDialog.setTitleText(R.string.remind);
                noticeDialog.setMessage(getString(R.string.confirm_logout));
                noticeDialog.setPositiveButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();
                        UserManager.clearLoginInfo();
                        initData();
                    }
                });
            }
        });
        View zoomView = LayoutInflater.from(getContext()).inflate(R.layout.profile_zoom_view, view, false);//拉伸背景view
        contentView = new UserContentView(getActivity(), new UserContentView.ContentClickListener() {
            @Override
            public void appShareClick() {
                ShareUtils.share(getActivity(),
                        "http://www.baidu.com/",
                        getString(R.string.app_share),
                        getString(R.string.app_name),
                        getString(R.string.word_app_share),
                        R.mipmap.ic_launcher);
            }

            @Override
            public void feedbackClick() {
                if (UserManager.hasLogin()) {
                    startActivity(new Intent(getActivity(), FeedbackActivity.class));
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Key.TAG_RELEASE_FEEDBACK);
                }
            }

            @Override
            public void checkUpdateClick() {
                toast(R.string.checking_update);
                new TimeTask.OverDo(1000, new TimeTask.TimeOverListerner() {
                    @Override
                    public void onFinished() {
                        toast(R.string.last_version_now);
                    }
                });
            }

            @Override
            public void clearCacheClick() {
                DataCleanUtils.getInstance(getContext()).cleanCatch();
                new TimeTask.OverDo(1000, new TimeTask.TimeOverListerner() {
                    @Override
                    public void onFinished() {
                        contentView.updateCatchText();
                    }
                });
            }

            @Override
            public void aboutUsClick() {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });
        mPtzUser.setHeaderView(headView);
        mPtzUser.setZoomView(zoomView);
        mPtzUser.setScrollContentView(contentView);
        mPtzUser.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(final int newScrollValue) {

            }

            @Override
            public void onPullZoomEnd() {
                contentView.updateCatchText();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        uid = UserManager.getInstance().getUser().getObjectId();
        photoUrl = UserManager.getInstance().getPhotoUrl();
        updateHeadView(UserManager.hasLogin());
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK) { return; }
        switch (requestCode) {
            case Key.TAKE_PHOTO:
                startZoomActivity(Uri.fromFile(new File(PATH_HEAD_PICTURE)));
                break;
            case Key.PICK_PHOTO:
                if (data != null) {
                    startZoomActivity(data.getData());
                }
                break;
            case Key.CUT_PHOTO:
                //剪裁后的图片
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        final Bitmap bm = extras.getParcelable("data");
                        FileUtils.saveLocalBitmap(bm, Constants.PATH_SETTING_CATCH + uid + Key.JPG);
                        uploadPic(bm);
                    }
                }
                break;
            case Key.TAG_RELEASE_FEEDBACK:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            default:
                break;
        }
        initData();
    }

    private void uploadPic(final Bitmap bm) {
        BmobFile file = new BmobFile();
        if (!TextUtils.isEmpty(photoUrl)) {
            file.setUrl(photoUrl);
            file.delete(new UpdateListener() {
                @Override
                public void done(final BmobException e) {
                    doUpdate(bm);
                }
            });
        } else {
            doUpdate(bm);
        }
    }

    private void doUpdate(final Bitmap bm) {
        final BmobFile picture = new BmobFile(new File(Constants.PATH_SETTING_CATCH + uid + Key.JPG));
        picture.upload(new UploadFileListener() {
            @Override
            public void done(final BmobException e) {
                if (e != null) {
                    toast(e.getMessage());
                    return;
                }
                updatePhotoData(picture, bm);
            }
        });
    }

    private void updatePhotoData(final BmobFile picture, final Bitmap bm) {
        PreferenceHelper.setPreference(Key.USER_PHOTO, picture.getUrl());
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(uid, new QueryListener<User>() {
            @Override
            public void done(final User user, final BmobException e) {
                user.setPhoto(picture);
                user.setPhotoUri(picture.getUrl());
                user.update(new UpdateListener() {
                    @Override
                    public void done(final BmobException e) {
                        if (e != null) {
                            Mlog.e(e.toString());
                            toast(R.string.error);
                            return;
                        }
                        FileUtils.deleteFiles(new File(Constants.PATH_SETTING_CATCH));
                        headView.setHeadImg(bm);
                        UserManager.getInstance().setPhotoUrl(picture.getUrl());
                    }
                });
            }
        });
    }

    private void updateHeadView(boolean hasLogin) {
        headView.setUserText(UserManager.getInstance().getNickname());
        headView.setHeadImg(photoUrl);
        headView.updateUI(hasLogin);
    }

    private void startZoomActivity(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, Key.CUT_PHOTO);
    }

    public void setHeadPicTemp() {
        PATH_HEAD_PICTURE = Constants.PATH_SETTING_CATCH + System.currentTimeMillis() + Key.JPG;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxSubscription = RxBus.getDefault().toObservable(User.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(final User user) {
                        initData();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }
}
