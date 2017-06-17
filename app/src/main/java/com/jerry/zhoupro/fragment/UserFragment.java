package com.jerry.zhoupro.fragment;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.activity.LoginActivity;
import com.jerry.zhoupro.activity.RegisterActivity;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.User;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.pop.ItemPopupWindow;
import com.jerry.zhoupro.util.FileUtils;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.util.PreferenceUtil;
import com.jerry.zhoupro.view.UserContentView;
import com.jerry.zhoupro.view.UserHeadView;
import com.jerry.zhoupro.widget.NoticeDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.jerry.zhoupro.command.Constants.PATH_SETTING_CATCH;
import static com.jerry.zhoupro.command.Key.CUT_PHOTO;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class UserFragment extends TitleBaseFragment implements AdapterView.OnItemClickListener {

    private UserHeadView headView;
    private ItemPopupWindow itemPopupWindow;

    @BindView(R.id.ptz_user)
    PullToZoomScrollViewEx mPtzUser;
    private String uid = Key.NIL;
    private String photoUrl;
    private String PATH_HEAD_PICTURE;

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
        headView = new UserHeadView(getContext());//头部扩展view
        headView.setHeadClickListener(new UserHeadView.HeadClickListener() {
            @Override
            public void changePic() {
                if (UserManager.hasLogin()) {
                    if (itemPopupWindow == null) {
                        List<String> list = new ArrayList<String>();
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
            public void register() {
                startActivityForResult(new Intent(getActivity(), RegisterActivity.class), Key.REGISTER);
            }

            @Override
            public void login() {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), Key.LOGIN);
            }

            @Override
            public void logout() {
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
        UserContentView contentView = new UserContentView(getContext());
        contentView.setOnItemClickListener(this);
        mPtzUser.setHeaderView(headView);
        mPtzUser.setZoomView(zoomView);
        mPtzUser.setScrollContentView(contentView);
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
            case Key.REGISTER:
                User user = new User();
                user.setUsername(data.getStringExtra(Key.phone));
                user.setPassword(data.getStringExtra(Key.password));
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(final User user, final BmobException e) {
                        if (e != null) {
                            Mlog.e(e.toString());
                            toast(R.string.login_fail);
                        }
                        UserManager.getInstance().saveToLocal(user);
                    }
                });
                break;
            case Key.TAKE_PHOTO:
                startZoomActivity(Uri.fromFile(new File(PATH_HEAD_PICTURE)));
                break;
            case Key.PICK_PHOTO:
                if (data != null) {
                    startZoomActivity(data.getData());
                }
                break;
            case CUT_PHOTO:
                //剪裁后的图片
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        final Bitmap bm = extras.getParcelable("data");
                        FileUtils.saveLocalBitmap(bm, PATH_SETTING_CATCH + uid + Key.JPG);
                        uploadPic(bm);
                    }
                }
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
        final BmobFile picture = new BmobFile(new File(PATH_SETTING_CATCH + uid + Key.JPG));
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
        PreferenceUtil.setPreference(Key.USER_PHOTO, picture.getUrl());
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
                        FileUtils.deleteFiles(PATH_SETTING_CATCH);
                        headView.setHeadImg(bm);
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

    /**
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        //TODO UserMenu点击事件
    }

    public void setHeadPicTemp() {
        PATH_HEAD_PICTURE = PATH_SETTING_CATCH + System.currentTimeMillis() + Key.JPG;
    }
}
