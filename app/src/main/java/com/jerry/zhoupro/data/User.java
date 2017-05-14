package com.jerry.zhoupro.data;

import cn.bmob.v3.BmobUser;

/**
 * Created by drakeet on 8/11/15.
 */
public class User extends BmobUser {

    public String city;
    public String college;

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(final String college) {
        this.college = college;
    }
}