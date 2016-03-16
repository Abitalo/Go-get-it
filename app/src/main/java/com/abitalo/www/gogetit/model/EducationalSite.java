package com.abitalo.www.gogetit.model;

/**
 * Created by Lancelot on 2016/3/16.
 */
public interface EducationalSite extends Site {

    School getSchool();
    Type getType();

//    the type of this place
    enum Type{
        LIBRARY,
        TEACHING_BUILDING,
        ADMINISTRATIVE_BUILDING,
        RESEARCH_BUILDING;
//    NOTICE!this is ordered! When a place meets multiple description, match the first one.
    }
}
