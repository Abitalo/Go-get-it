package com.abitalo.www.gogetit.Site;

import com.abitalo.www.gogetit.model.EducationalSite;
import com.abitalo.www.gogetit.model.School;

/**
 * Created by Lancelot on 2016/3/16.
 */
public class College implements EducationalSite {
    private static final Type type=Type.ADMINISTRATIVE_BUILDING;

    private School school;
    private String name;
    private MyLatLng latLng;

    private String profileURL;

    public College(String name, School school, MyLatLng latLng, String profileURL){
        this(name,school,latLng);
        this.profileURL=profileURL;
    }

    public College(String name, School school, MyLatLng latLng){
        this(name,school);
        this.latLng=latLng;
    }

    public College(String name, School school) {
        this.name=name;
        this.school=school;
    }

    @Override
    public School getSchool() {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean setName() {
        return false;
    }

    @Override
    public boolean updateInfo() {
        return false;
    }

    @Override
    public MyLatLng getLatLng() {
        return null;
    }

    @Override
    public boolean setLatLng() {
        return false;
    }
}
