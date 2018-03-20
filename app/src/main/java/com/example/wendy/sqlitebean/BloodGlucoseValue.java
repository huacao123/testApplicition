package com.example.wendy.sqlitebean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class BloodGlucoseValue extends DataSupport{

    private UserInfo userInfo;
    private int id;
    private String timeSelect;
    private String boldGlucoseLevelValue;
    Integer[] data  = new Integer[3];


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer[] getData() {
        return data;
    }

    public void setData(Integer[] data) {
        this.data[0] =data[0];
        this.data[1] =data[1];
        this.data[2] =data[2];
    }

    public String getTime() {
        return timeSelect;
    }

    public void setTime(String time) {
        this.timeSelect = time;
    }

    public String getBoldGlucoseLevelValue() {
        return boldGlucoseLevelValue;
    }

    public void setBoldGlucoseLevelValue(String bgvalue) {
        this.boldGlucoseLevelValue = bgvalue;
    }


}
