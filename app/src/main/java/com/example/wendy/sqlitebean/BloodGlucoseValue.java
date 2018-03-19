package com.example.wendy.sqlitebean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/3/19.
 */

public class BloodGlucoseValue extends DataSupport{

    String data;
    String time;
    String bgvalue;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBgvalue() {
        return bgvalue;
    }

    public void setBgvalue(String bgvalue) {
        this.bgvalue = bgvalue;
    }
}
