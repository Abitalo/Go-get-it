package com.abitalo.www.gogetit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;

import java.util.Map;

/**
 * Created by Lancelot on 2016/3/16.
 */
public class LocationThread extends Thread implements AMapLocationListener {
    private Context context = null;
    private Handler handler = null;


    private static TextView mainInfo= null;
    private static TextView debugInfo=null;
    private static AMap mAMap=null;

    private static LatLng latLngNow=null;

    private static AMapLocationClient mLocationClient = null;
    private static AMapLocationClientOption mLocationOption = null;

    String GEOFENCE_BROADCAST_ACTION="com.abitalo.www.gogetit";
    PendingIntent pendingIntent = null;

    private GeoFenceReceiver mGeoFenceReceiver = new GeoFenceReceiver();

    public LocationThread(AMapLocationClient client,Context context, Handler handler, Map params){
        this.context = context;
        this.handler = handler;
        this.mLocationClient=client;

        this.mainInfo = (TextView) params.get("mainInfo");
        this.debugInfo = (TextView) params.get("debugInfo");
        this.mAMap= (AMap) params.get("map");
    }

    @Override
    public void run() {
        super.run();
//      register the receiver
//      and startLocation the pendingIntent for the later GeoFence's register.
        Intent intent=new Intent(GEOFENCE_BROADCAST_ACTION);
        pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(GEOFENCE_BROADCAST_ACTION);

        context.registerReceiver(mGeoFenceReceiver,intentFilter);
        //      start location service
        initLocationClient();
    }

    //      update camera's central point
    private void updateCamera(){
        //      startLocation camera
        CameraUpdate cuZoom=CameraUpdateFactory.zoomTo(20);
        mAMap.moveCamera(cuZoom);
        mAMap=HomeFragment.mMapView.getMap();
        CameraUpdate cuMove= CameraUpdateFactory.changeLatLng(latLngNow);
        mAMap.animateCamera(cuMove);
    }
    //      startLocation the GenFence area
    private void initGenFence(){
//        mLocationClient.addGeoFenceAlert("gym", 31.048412,121.212389, 50, -1, pendingIntent);
//        mAMap.addCircle(new CircleOptions().center(new LatLng(31.048412,121.212389)).radius(50));
//
//        mLocationClient.addGeoFenceAlert("college", 31.055944, 121.21115, 50, -1, pendingIntent);
//        mAMap.addCircle(new CircleOptions().center(new LatLng(31.055944, 121.21115)).radius(50));

        mLocationClient.addGeoFenceAlert("lab", 31.054262, 121.212046, 50, -1, pendingIntent);
        mAMap.addCircle(new CircleOptions().center(new LatLng(31.054262, 121.212046)).radius(50));
    }
    //      startLocation the locationService and start it with some configuration
    private void initLocationClient(){
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        initGenFence();

        //启动定位
        mLocationClient.startLocation();

    }
    //      listener when location update
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                latLngNow=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                updateCamera();
                Log.e("gogetit",aMapLocation.getAddress());
                mainInfo.setText(aMapLocation.getAddress());
            } else {

                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
