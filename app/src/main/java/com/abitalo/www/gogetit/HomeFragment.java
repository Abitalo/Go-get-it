package com.abitalo.www.gogetit;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lancelot on 2016/3/17.
 */
public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private static AMapLocationClient mLocationClient = null;

    private static View view=null;
    private static Handler handler=new Handler();

    private static CheckBox location_btn=null;
    private static TextView mainInfo= null;
    public static TextView debugInfo=null;//test

    public static MapView mMapView= null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home,container,false);
        //      startLocation view
        mMapView=(MapView)view.findViewById(R.id.mapview);

        debugInfo=(TextView)view.findViewById(R.id.debug_info);// only for debug
        mainInfo=(TextView)view.findViewById(R.id.main_info);
        location_btn = (CheckBox)view.findViewById(R.id.location_btn);
        mMapView.onCreate(savedInstanceState);

        location_btn.setOnCheckedChangeListener(this);
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());

        mMapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.054805,121.215742),16));

        Marker marker=mMapView.getMap().addMarker(new MarkerOptions().position(new LatLng(31.055944,121.211332)));
        mMapView.getMap().setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), "hahaha", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;

    }

    public void startLocation(){

        Map params = new HashMap();
        params.put("map",mMapView.getMap());
        params.put("mainInfo",mainInfo);
        params.put("debugInfo",debugInfo);

        new LocationThread(mLocationClient,getActivity().getApplicationContext(),handler,params);
    }
    @Override
    public void onResume() {

        super.onResume();
        mMapView.onResume();
        mMapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.054805,121.215742),16));

    }

    @Override
    public void onPause() {

        super.onPause();
        mMapView.onPause();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }



    @Override
    public void onDestroy() {

        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == true)
            startLocation();
        else{
            mLocationClient.stopLocation();
        }
    }
}
