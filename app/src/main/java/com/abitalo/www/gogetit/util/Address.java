package com.abitalo.www.gogetit.util;

/**
 * Created by Lancelot on 2016/3/16.
 */
public class Address {
    private String province = null;
    private String city = null;
    private String district = null;

    public Address(String province, String city, String district ){
        this.province=province;
        this.city=city;
        this.district=district;
    }
}
