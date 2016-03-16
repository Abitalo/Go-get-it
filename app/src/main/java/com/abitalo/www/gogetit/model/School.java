package com.abitalo.www.gogetit.model;

import com.abitalo.www.gogetit.util.Address;

/**
 * Created by Lancelot on 2016/3/16.
 */
public class School {
    private String name = null;
    private Address address = null;

    public String getName() {
        return name;
    }

    public Address getAddress() { return address;}

    public School(Address address ,String name){
        this.address=address;
        this.name=name;
    }

}
