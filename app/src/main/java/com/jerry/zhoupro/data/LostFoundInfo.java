package com.jerry.zhoupro.data;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wzl-pc on 2017/5/20.
 */

public class LostFoundInfo extends BmobObject {

    String title;
    int type;
    String date;
    String place;
    String content;
    List<BmobFile> files;

    public LostFoundInfo() {
       files = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(final int type) {
        this.type = type;
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

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public List<BmobFile> getFiles() {
        return files;
    }

    public void setFiles(final List<BmobFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "LostFoundInfo{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", date='" + date + '\'' +
                ", place='" + place + '\'' +
                ", content='" + content + '\'' +
                ", files=" + files +
                '}';
    }
}
