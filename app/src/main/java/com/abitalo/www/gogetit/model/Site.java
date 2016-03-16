package com.abitalo.www.gogetit.model;

/**
 * Created by Lancelot on 2016/3/16.
 */
public interface Site {
    //get or set name of this site
    String getName();
    boolean setName();

    //update the information related to the site
    boolean updateInfo();

    //get or set longtitude and latitude of the site;
    MyLatLng getLatLng();
    boolean setLatLng();

    //the longtitude and latitude
    class MyLatLng {
        double longtitude;
        double latitude;
    }
}
