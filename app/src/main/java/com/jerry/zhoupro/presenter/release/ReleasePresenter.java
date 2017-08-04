package com.jerry.zhoupro.presenter.release;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.TitleBaseActivity;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.model.bean.ThingInfoBean;
import com.jerry.zhoupro.model.bean.UserManager;
import com.jerry.zhoupro.presenter.baiduMap.MapPresenter;
import com.jerry.zhoupro.util.FileUtils;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.util.WeakHandler;
import com.jerry.zhoupro.widget.CustomDatePickerDialog;
import com.jerry.zhoupro.widget.ItemPopupWindow;
import com.jerry.zhoupro.widget.MeasureGridView;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.jerry.zhoupro.app.Constants.PATH_SETTING_CATCH;

public class ReleasePresenter extends TitleBaseActivity {

    @BindView(R.id.et_release_title)
    EditText mEtReleaseTitle;
    @BindView(R.id.et_release_content)
    EditText mEtReleaseContent;
    @BindView(R.id.gridView)
    MeasureGridView mGridView;
    @BindView(R.id.tv_thing_type_value)
    TextView mTvThingTypeValue;
    @BindView(R.id.tv_thing_date_value)
    TextView mTvThingDateValue;
    @BindView(R.id.tv_thing_place_value)
    TextView mTvThingPlaceValue;
    @BindView(R.id.iv_pic_info)
    ImageView mIvPicInfo;
    private int releaseType;//0:失物，1:招领，2:发现
    private String latlng;
    private String city;
    private LocationClient mLocationClient;
    private WeakHandler mHandler;
    private ItemPopupWindow itemPopupWindow;
    private String PATH_RELEASE_PIC;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_release;
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        releaseType = getIntent().getIntExtra(Key.RELEASE_TYPE, -1);
        mHandler = new WeakHandler();
    }

    @Override
    protected String getTitleText() {
        switch (releaseType) {
            case Key.TAG_RELEASE_LOST:
                return getString(R.string.realese_lost);
            case Key.TAG_RELEASE_FOUND:
                return getString(R.string.realese_found);
            default:
                return getString(R.string.virtue);
        }
    }

    @Override
    public void initView() {
        super.initView();
        setVisible(titleRight);
        titleRight.setText(getString(R.string.realese));
        mTvThingTypeValue.setText(R.string.all);
        switch (releaseType) {
            case Key.TAG_RELEASE_LOST:
                mEtReleaseTitle.setHint(R.string.title_lost);
                break;
            case Key.TAG_RELEASE_FOUND:
                mEtReleaseTitle.setHint(R.string.title_found);
                break;
            default:
                mEtReleaseTitle.setHint(R.string.title);
                break;
        }
    }

    @Override
    protected void initData() {
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation bdLocation) {
                if (isFinishing()) { return; }
                mLocationClient.stop();
                final Address address = bdLocation.getAddress();
                city = getCity(address.province, address.city);
                latlng = bdLocation.getLatitude() + "," + bdLocation.getLongitude();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTvThingPlaceValue.setText(address.address);
                    }
                });
            }

            @Override
            public void onConnectHotSpotMessage(final String s, final int i) {

            }
        });
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    @OnClick({R.id.tv_thing_type, R.id.tv_thing_date, R.id.tv_thing_place, R.id.iv_pic_info, R.id.tv_right})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_thing_type:
                intent = new Intent(this, ThingTypePresenter.class);
                intent.putExtra(Key.THING_TYPE, mTvThingTypeValue.getText().toString());
                startActivityForResult(intent, Key.CODE_101);
                break;
            case R.id.tv_thing_date:
                final CustomDatePickerDialog dialog = new CustomDatePickerDialog(this);
                dialog.show();
                dialog.setPositiveButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        dialog.dismiss();
                        String date = dialog.getDate();
                        mTvThingDateValue.setText(date);
                    }
                });
                break;
            case R.id.tv_thing_place:
                intent = new Intent(this, MapPresenter.class);
                startActivityForResult(intent, Key.CODE_102);
                break;
            case R.id.iv_pic_info:
                if (itemPopupWindow == null) {
                    List<String> list = new ArrayList<String>();
                    list.add(getString(R.string.take_photo));
                    list.add(getString(R.string.select_photo));
                    itemPopupWindow = new ItemPopupWindow(this, list, new ItemPopupWindow.ActionLister() {
                        @Override
                        public void stringAction(final int index) {
                            String sdStatus = Environment.getExternalStorageState();
                            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                                Mlog.i("TestFile",
                                        "SD card is not avaiable/writeable right now.");
                                toast(R.string.no_sdCard);
                                return;
                            }
                            setReleasePicTemp();
                            Intent intent;
                            switch (index) {
                                case 0://拍照
                                    if (!FileUtils.createFile(PATH_RELEASE_PIC)) {
                                        return;
                                    }
                                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(PATH_RELEASE_PIC)));
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
                    itemPopupWindow.show();
                }
                itemPopupWindow.show();
                break;
            case R.id.tv_right:
                final String thingType = mTvThingTypeValue.getText().toString();
                final String date = mTvThingDateValue.getText().toString();
                final String place = mTvThingPlaceValue.getText().toString();
                String title = mEtReleaseTitle.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    title = mEtReleaseTitle.getHint().toString();
                }
                final String content = mEtReleaseContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    toast(R.string.input_content);
                    return;
                }
                if (TextUtils.isEmpty(thingType)) {
                    toast(R.string.input_type);
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    toast(R.string.input_date);
                    return;
                }
                if (TextUtils.isEmpty(place)) {
                    toast(R.string.input_place);
                    return;
                }
                if (releaseType == Key.TAG_RELEASE_FIND && TextUtils.isEmpty(PATH_RELEASE_PIC)) {
                    toast(R.string.realese_find_must_pic);
                    return;
                }
                loadingDialog();

                final ThingInfoBean thingInfo = new ThingInfoBean();
                thingInfo.setReleaseType(releaseType);
                thingInfo.setTitle(title);
                thingInfo.setThingType(thingType);
                thingInfo.setDate(date);
                thingInfo.setPlace(place);
                thingInfo.setContent(content);
                thingInfo.setLatLng(latlng);
                thingInfo.setCity(city);
                thingInfo.setReleaser(UserManager.getInstance().getUser());
                if (TextUtils.isEmpty(PATH_RELEASE_PIC)) {
                    saveThingInfo(thingInfo);
                } else {
                    final BmobFile bmobFile = new BmobFile(new File(PATH_RELEASE_PIC));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(final BmobException e) {
                            if (e != null) {
                                Mlog.e(e.toString());
                                toast(R.string.error);
                                return;
                            }
                            List<BmobFile> pictures = new ArrayList<>();
                            pictures.add(bmobFile);
                            thingInfo.setPictures(pictures);
                            saveThingInfo(thingInfo);
                        }
                    });
                }

                break;
        }
    }

    private void saveThingInfo(final ThingInfoBean thingInfo) {
        thingInfo.save(new SaveListener<String>() {
            @Override
            public void done(final String thingInfo, final BmobException e) {
                if (e != null) {
                    Mlog.e(e.toString());
                    toast(R.string.error);
                    return;
                }
                Mlog.d(thingInfo);
                toast(R.string.release_success);
                closeLoadingDialog();
                FileUtils.deleteFiles(new File(PATH_SETTING_CATCH));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { return; }
        switch (requestCode) {
            case Key.CODE_101:
                String thingType = data.getStringExtra(Key.THING_TYPE);
                mTvThingTypeValue.setText(thingType);
                break;
            case Key.CODE_102:
                String place = data.getStringExtra(Key.ADDRESS);
                latlng = data.getStringExtra(Key.LOCATION);
                city = data.getStringExtra(Key.USER_CITY);
                mTvThingPlaceValue.setText(place);
                break;
            case Key.TAKE_PHOTO:
                Glide.with(this).load(PATH_RELEASE_PIC).into(mIvPicInfo);
                break;
            case Key.PICK_PHOTO:
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    FileUtils.saveFile(inputStream, PATH_RELEASE_PIC);
                    Glide.with(this).load(uri).into(mIvPicInfo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private String getCity(String province, String city) {
        if (province.equals(city)) {
            return city;
        }
        return province + city;
    }

    public void setReleasePicTemp() {
        PATH_RELEASE_PIC = PATH_SETTING_CATCH + System.currentTimeMillis() + Key.JPG;
    }
}
