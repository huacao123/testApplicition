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
    private int year ;
    private int mouth;
    private int day;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMouth() {
        return mouth;
    }

    public void setMouth(int mouth) {
        this.mouth = mouth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public String getTimeSelect() {
        return timeSelect;
    }

    public void setTimeSelect(String timeSelect) {
        this.timeSelect = timeSelect;
    }

    public String getBoldGlucoseLevelValue() {
        return boldGlucoseLevelValue;
    }

    public void setBoldGlucoseLevelValue(String bgvalue) {
        this.boldGlucoseLevelValue = bgvalue;
    }


}
