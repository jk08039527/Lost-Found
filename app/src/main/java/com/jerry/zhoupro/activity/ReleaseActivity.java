package com.jerry.zhoupro.activity;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.bean.ThingInfoBean;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.data.UserManager;
import com.jerry.zhoupro.pop.CustomDatePickerDialog;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.util.WeakHandler;
import com.jerry.zhoupro.view.MeasureGridView;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ReleaseActivity extends TitleBaseActivity {

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
    private int releaseType;
    private String thingType;
    private String date;
    private String place;
    private String latlng;
    private String city;
    private LocationClient mLocationClient;
    private WeakHandler mHandler;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_release;
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        releaseType = getIntent().getIntExtra(Key.TAG_RELEASE_TYPE, -1);
        mHandler = new WeakHandler();
    }

    @Override
    protected String getTitleText() {
        return releaseType == Key.TAG_RELEASE_LOST
                ? getString(R.string.realese_lost)
                : getString(R.string.realese_found);
    }

    @Override
    public void initView() {
        super.initView();
        setVisible(titleRight);
        titleRight.setText(getString(R.string.realese));
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
                Address address = bdLocation.getAddress();
                city = getCity(address.province, address.city);
                place = address.address;
                latlng = bdLocation.getLatitude() + "," + bdLocation.getLongitude();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTvThingPlaceValue.setText(place);
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

    @OnClick({R.id.tv_thing_type, R.id.tv_thing_date, R.id.tv_thing_place, R.id.tv_right})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_title:
                intent = new Intent();
                break;
            case R.id.tv_thing_type:
                intent = new Intent(this, ThingTypeActivity.class);
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
                        date = dialog.getDate();
                        mTvThingDateValue.setText(date);
                    }
                });
                break;
            case R.id.tv_thing_place:
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, Key.CODE_102);
                break;
            case R.id.tv_right:
                String thingType = mTvThingTypeValue.getText().toString();
                String date = mTvThingDateValue.getText().toString();
                String place = mTvThingPlaceValue.getText().toString();
                String content = mEtReleaseContent.getText().toString();
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
                ThingInfoBean thingInfo = new ThingInfoBean();
                thingInfo.setReleaseType(Key.TAG_RELEASE_LOST);
                thingInfo.setTitle(titleText.getText().toString());
                thingInfo.setThingType(thingType);
                thingInfo.setDate(date);
                thingInfo.setPlace(place);
                thingInfo.setContent(content);
                thingInfo.setLatLng(latlng);
                thingInfo.setCity(city);
                thingInfo.setReleaser(UserManager.getInstance().getUser().getObjectId());
                thingInfo.save(new SaveListener<String>() {
                    @Override
                    public void done(final String thingInfoBean, final BmobException e) {
                        if (e != null) {
                            toast(R.string.error);
                            return;
                        }
                        Mlog.d(thingInfoBean);

                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) { return; }
        switch (requestCode) {
            case Key.CODE_101:
                thingType = data.getStringExtra(Key.THING_TYPE);
                mTvThingTypeValue.setText(thingType);
                break;
            case Key.CODE_102:
                place = data.getStringExtra(Key.ADDRESS);
                latlng = data.getStringExtra(Key.LOCATION);
                city = data.getStringExtra(Key.USER_CITY);
                mTvThingPlaceValue.setText(place);
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
}
