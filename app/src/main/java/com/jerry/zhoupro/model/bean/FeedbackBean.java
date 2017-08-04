package com.jerry.zhoupro.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wzl-pc on 2017/6/19.
 */

public class FeedbackBean extends BmobObject {
    private User user;
    private String content;
    private boolean agree;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(final boolean agree) {
        this.agree = agree;
    }
}
