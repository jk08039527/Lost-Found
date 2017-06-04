package com.jerry.zhoupro.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.listener.MyGetGeoCoderResultListener;
import com.jerry.zhoupro.listener.MyLocationListener;
import com.jerry.zhoupro.listener.MyMapStatusChangeListener;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;

public class MapActivity extends TitleBaseActivity {

    @BindView(R.id.bmapView)
    MapView mBmapView;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    private BaiduMap map;

    private String mAddress;
    private String city;
    private String mCurrentLocation;
    private LocationClient mLocationClient;

    /**
     * 初始化定位
     */
    public BDLocationListener mLocationListener = new MyLocationListener() {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            MyLocationData.Builder builder = new MyLocationData.Builder();
            builder.latitude(latitude);
            builder.longitude(longitude);
            builder.accuracy(location.getRadius());
            builder.direction(location.getDirection());
            MyLocationData locationData = builder.build();
            map.setMyLocationData(locationData);
            map.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
            map.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng, 16));

            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        }
    };
    private GeoCoder mSearch;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.get_address);
    }

    @Override
    public void initView() {
        super.initView();
        setVisible(titleRight);
        titleRight.setText(getString(R.string.ok));
        map = mBmapView.getMap();
        map.setMyLocationEnabled(true);
        map.setOnMapStatusChangeListener(new MyMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeFinish(final MapStatus mapStatus) {
                LatLng center = mapStatus.target;
                String location = center.longitude + "," + center.latitude;
                System.out.println(location);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(center));
            }
        });
    }

    @Override
    protected void initData() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);
        initLocation();
        initStatusChange();
    }

    @OnClick({R.id.tv_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                Intent data = new Intent();
                data.putExtra(Key.ADDRESS, mAddress);
                data.putExtra(Key.LOCATION, mCurrentLocation);
                data.putExtra(Key.USER_CITY, city);
                setResult(RESULT_OK, data);
                finish();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBmapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(mLocationListener);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setOpenGps(true);// 打开gps
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        mLocationClient.setLocOption(option);

    }

    /**
     * 中心发生变化
     */
    private void initStatusChange() {
        mSearch = GeoCoder.newInstance();
        MyGetGeoCoderResultListener resultListener = new MyGetGeoCoderResultListener() {
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || isFinishing() || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    toast(R.string.web_not_well);
                    return;
                }
                //获取反向地理编码结果
                LatLng latLng = result.getLocation();
                mCurrentLocation = latLng.latitude + "," + latLng.longitude;
                mAddress = result.getAddress();
                mEtAddress.setSelection(mEtAddress.getText().length());
                mEtAddress.setText(mAddress);
                city = getCity(result.getAddressDetail().province, result.getAddressDetail().city);
            }
        };
        mSearch.setOnGetGeoCodeResultListener(resultListener);
    }

    private String getCity(String province, String city) {
        if (province.equals(city)) {
            return city;
        }
        return province + city;
    }
}
