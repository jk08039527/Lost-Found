package com.jerry.zhoupro.model.bean;

import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.model.prefs.PreferenceHelper;

import android.text.TextUtils;

/**
 * Created by wzl-pc on 2017/5/16.
 */

public class UserManager {

    private static UserManager instance;
    private static User user;

    private UserManager() {}

    public static UserManager getInstance() {
        if (null == instance) {
            synchronized (UserManager.class) {
                if (null == instance) {
                    instance = new UserManager();
                    if (null == user) {
                        user = new User();
                        user.setObjectId(PreferenceHelper.getPreference(Key.UID));
                        user.setMobilePhoneNumber(PreferenceHelper.getPreference(Key.USER_MOBLIE));
                        user.setNickname(PreferenceHelper.getPreference(Key.USER_NICKNAME));
                        user.setPhotoUri(PreferenceHelper.getPreference(Key.USER_PHOTO));
                        user.setSessionToken(PreferenceHelper.getPreference(Key.USER_SESSIONTOKEN));
                    }
                }
            }
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public String getMobilePhoneNumber() {
        return user.getMobilePhoneNumber();
    }

    public void setMobilePhoneNumber(String phone) {
        user.setMobilePhoneNumber(phone);
    }

    public String getNickname() {
        return user.getNickname();
    }

    public void setNickname(String nickname) {
        user.setNickname(nickname);
    }

    public String getPhotoUrl() {
        return user.getPhotoUri();
    }

    public void setPhotoUrl(String photoUri) {
        user.setPhotoUri(photoUri);
    }

    public String getSessionToken() {
        return user.getSessionToken();
    }

    public void setSessionToken(String sessionToken) {
        user.setSessionToken(sessionToken);
    }

    public void saveToLocal(User user) {
        UserManager.user = user;
        PreferenceHelper.setPreference(Key.UID, user.getObjectId());
        PreferenceHelper.setPreference(Key.USER_MOBLIE, user.getMobilePhoneNumber());
        PreferenceHelper.setPreference(Key.USER_NICKNAME, user.getNickname());
        PreferenceHelper.setPreference(Key.USER_PHOTO, user.getPhotoUri());
        PreferenceHelper.setPreference(Key.USER_SESSIONTOKEN, user.getSessionToken());
    }

    public static boolean hasLogin() {
        return !TextUtils.isEmpty(PreferenceHelper.getPreference(Key.UID));
    }

    public static void clearLoginInfo() {
        user.clear();
        PreferenceHelper.setPreference(Key.UID, Key.NIL);
        PreferenceHelper.setPreference(Key.USER_MOBLIE, Key.NIL);
        PreferenceHelper.setPreference(Key.USER_NICKNAME, Key.NIL);
        PreferenceHelper.setPreference(Key.USER_PHOTO, Key.NIL);
        PreferenceHelper.setPreference(Key.USER_SESSIONTOKEN, Key.NIL);
    }
}
