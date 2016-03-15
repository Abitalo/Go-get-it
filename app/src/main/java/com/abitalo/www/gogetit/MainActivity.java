package com.abitalo.www.gogetit;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
public class MainActivity extends AppCompatActivity implements AMapLocationListener{

    private static LatLng latLngNow=null;

    private AccountHeader headerResult = null;//test
    private static final int PROFILE_SETTING = 100000;//test

    public static TextView debugInfo=null;

    private static TextView mainInfo= null;
    private static MapView mMapView= null;
    private static AMap mAMap=null;

    private static AMapLocationClient mLocationClient = null;
    private static AMapLocationClientOption mLocationOption = null;

    String GEOFENCE_BROADCAST_ACTION="com.abitalo.www.gogetit";
    PendingIntent pendingIntent = null;

    private GeoFenceReceiver mGeoFenceReceiver = new GeoFenceReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);
        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460")).withIdentifier(101);
        final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2).withIdentifier(102);
        final IProfile profile4 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3).withIdentifier(103);
        final IProfile profile5 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4).withIdentifier(104);
        final IProfile profile6 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5).withIdentifier(105);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(true)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5,
                        profile6
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
//                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
//                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile5).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        new DrawerBuilder().withActivity(this).withToolbar(toolbar).withAccountHeader(headerResult).build();
//      init view
        mMapView=(MapView)findViewById(R.id.mapview);
        debugInfo=(TextView)findViewById(R.id.debug_info);// only for debug
        mainInfo=(TextView)findViewById(R.id.main_info);
        mMapView.onCreate(savedInstanceState);
        if(mAMap == null){
            mAMap=mMapView.getMap();
        }
//      init camera
        CameraUpdate cuZoom=CameraUpdateFactory.zoomTo(20);
        mAMap.moveCamera(cuZoom);
//      register the receiver
//      and init the pendingIntent for the later GeoFence's register.
        Intent intent=new Intent(GEOFENCE_BROADCAST_ACTION);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(GEOFENCE_BROADCAST_ACTION);

        this.registerReceiver(mGeoFenceReceiver,intentFilter);
//      start location service
        initLocationClient();
    }
//      update camera's central point
    private void updateCamera(){
        CameraUpdate cuMove= CameraUpdateFactory.changeLatLng(latLngNow);
        mAMap.animateCamera(cuMove);
    }
//      init the GenFence area
    private void initGenFence(){
        mLocationClient.addGeoFenceAlert("gym", 31.048412,121.212389, 50, -1, pendingIntent);
        mAMap.addCircle(new CircleOptions().center(new LatLng(31.048412,121.212389)).radius(50));

        mLocationClient.addGeoFenceAlert("college", 31.055944, 121.21115, 50, -1, pendingIntent);
        mAMap.addCircle(new CircleOptions().center(new LatLng(31.055944, 121.21115)).radius(50));

        mLocationClient.addGeoFenceAlert("lab", 31.057585, 121.217335, 50, -1, pendingIntent);
        mAMap.addCircle(new CircleOptions().center(new LatLng(31.057585, 121.217335)).radius(50));
    }
//      init the locationService and start it with some configuration
    private void initLocationClient(){
        mLocationClient=new AMapLocationClient(getApplicationContext());
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

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
//      listener when location update
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                latLngNow=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                updateCamera();
                if (!mainInfo.getText().equals(aMapLocation.getAddress()))
                    mainInfo.setText(aMapLocation.getAddress());
            } else {

                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}