package com.suiyueshentou.databasebean;

import cn.bmob.v3.BmobObject;

/**
 * Created by jishu055 on 2016/6/28.
 */
public class Person extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
