package com.jerry.zhoupro.ui.release;


import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jerry.zhoupro.R;
import com.jerry.zhoupro.model.bean.ThingInfoBean;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.base.TitleBaseFragment;
import com.jerry.zhoupro.presenter.listener.MyGetGeoCoderResultListener;
import com.jerry.zhoupro.presenter.listener.MyLocationListener;
import com.jerry.zhoupro.presenter.listener.MyMapStatusChangeListener;
import com.jerry.zhoupro.util.Mlog;

import android.text.TextUtils;
import android.view.View;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by wzl-pc on 2017/5/9.
 */
public class ReleasePlaceFragment extends TitleBaseFragment {

    @BindView(R.id.bmapView)
    MapView mBmapView;
    private BaiduMap map;
    private GeoCoder mSearch;
    private String city;
    private LocationClient mLocationClient;

    /**
     * 初始化定位
     */
    private BDLocationListener mLocationListener = new MyLocationListener() {
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

    @Override
    public int getContentLayout() {
        return R.layout.fragment_place;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.try_find);
    }

    @Override
    public void initView(final View view) {
        super.initView(view);
        setGone(titleBack);
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
        super.initData();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();
        initStatusChange();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.registerLocationListener(mLocationListener);
        mLocationClient.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBmapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBmapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stop();
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
            public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult result) {
                if (result == null || getActivity().isFinishing() || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    toast(R.string.web_not_well);
                    return;
                }
                city = getCity(result.getAddressDetail().province, result.getAddressDetail().city);

                BmobQuery<ThingInfoBean> query = new BmobQuery<>(ThingInfoBean.class.getSimpleName());
                query.include(Key.RELEASER);// 希望在查询帖子信息的同时也把发布人的信息查询出来
                query.addWhereEqualTo(Key.USER_CITY, city);
                query.findObjects(new FindListener<ThingInfoBean>() {
                    @Override
                    public void done(final List<ThingInfoBean> list, final BmobException e) {
                        if (e != null) {
                            Mlog.e(e.toString());
                            toast(R.string.error);
                            return;
                        }
                        map.clear();
                        // 通过marker的icons设置一组图片，再通过period设置多少帧刷新一次图片资源
                        for (int i = 0, size = list.size(); i < size; i++) {
                            ThingInfoBean thingInfo = list.get(i);
                            String latLngStr = thingInfo.getLatLng();
                            int type = thingInfo.getReleaseType();
                            if (!TextUtils.isEmpty(latLngStr)) {
                                String[] latLng = latLngStr.split(",");
                                LatLng point = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                                //构建Marker图标
                                BitmapDescriptor bitmap = BitmapDescriptorFactory
                                        .fromResource(type == Key.TAG_RELEASE_LOST ? R.drawable.icon_lost_place : R.drawable.icon_found_place);
                                OverlayOptions ooD = new MarkerOptions()
                                        .position(point)
                                        .icon(bitmap)
                                        .zIndex(0).period(10);
                                map.addOverlay(ooD);
                            }
                        }
                    }
                });
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
