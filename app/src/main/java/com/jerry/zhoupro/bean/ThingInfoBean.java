package com.jerry.zhoupro.bean;

import java.util.List;

import com.jerry.zhoupro.data.User;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wzl-pc on 2017/6/3.
 */

public class ThingInfoBean extends BmobObject {
    private int releaseType;//失物0，招领1
    private String title;
    private String date;
    private String place;
    private List<BmobFile> pictures;
    private String content;
    private User releaser;//发布者，这里储存id
    private String thingType;
    private String city;
    private String latLng;

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(final int releaseType) {
        this.releaseType = releaseType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(final String place) {
        this.place = place;
    }

    public List<BmobFile> getPictures() {
        return pictures;
    }

    public void setPictures(final List<BmobFile> pictures) {
        this.pictures = pictures;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public User getReleaser() {
        return releaser;
    }

    public void setReleaser(final User releaser) {
        this.releaser = releaser;
    }

    public String getThingType() {
        return thingType;
    }

    public void setThingType(final String thingType) {
        this.thingType = thingType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(final String latLng) {
        this.latLng = latLng;
    }
}
