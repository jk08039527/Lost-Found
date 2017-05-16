package com.jerry.zhoupro.data;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * Created by drakeet on 8/11/15.
 */
public class User extends BmobUser implements Parcelable {

    private String nickname;
    private String city;
    private String college;
    private BmobFile photo;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

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

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(final BmobFile photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(nickname);
        dest.writeString(city);
        dest.writeString(college);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            User user = new User();
            user.nickname = in.readString();
            user.city = in.readString();
            user.college = in.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public <T> Subscription login(final SaveListener<T> listener) {
        return super.login(listener);
    }
}