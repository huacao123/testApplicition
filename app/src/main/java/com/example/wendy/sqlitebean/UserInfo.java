package com.example.wendy.sqlitebean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjaiwl on 15/3/19.
 */
public class UserInfo extends DataSupport{
    private List<BloodGlucoseValue> mBGValueList = new ArrayList<>();

    public static UserInfo user = null;

    private int id;

    public Integer doctor_id;
    private String doctor_name;
    private String doctor_url;
    private String doctor_password;
    private String doctor_gender;
    private String doctor_birthday;
    private String doctor_job;
    private String doctor_department;
    private String doctor_telephone;

    public UserInfo(){
        super();
    }

    public UserInfo(Integer id,String name,String password,String url){
        super();
        this.doctor_id = id;
        this.doctor_name = name;
        this.doctor_password = password;
        this.doctor_url = url;
    }

    public List<BloodGlucoseValue> getmBGValueList() {
        return mBGValueList;
    }

    public void setmBGValueList(List<BloodGlucoseValue> mBGlucoseValue) {
        this.mBGValueList = mBGlucoseValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void setUserInfo(UserInfo u) {
        UserInfo.user = u;
    }

    public static void setEmpty() {
        user = null;
    }

    public static boolean isEmpty() {
        if (user == null) {
            return true;
        }
        return false;
    }

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer id) {
        this.doctor_id = id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoctor_department() {
        return doctor_department;
    }

    public void setDoctor_department(String doctor_department) {
        this.doctor_department = doctor_department;
    }

    public String getDoctor_job() {

        return doctor_job;
    }

    public void setDoctor_job(String doctor_job) {
        this.doctor_job = doctor_job;
    }

    public String getDoctor_birthday() {

        return doctor_birthday;
    }

    public void setDoctor_birthday(String doctor_birthday) {
        this.doctor_birthday = doctor_birthday;
    }

    public String getDoctor_gender() {
        return doctor_gender;
    }

    public void setDoctor_gender(String doctor_gender) {
        this.doctor_gender = doctor_gender;
    }

    public String getDoctor_password() {

        return doctor_password;
    }

    public void setDoctor_password(String doctor_password) {
        this.doctor_password = doctor_password;
    }

    public String getDoctor_url() {

        return doctor_url;
    }

    public void setDoctor_url(String doctor_url) {
        this.doctor_url = doctor_url;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_telephone() {
        return doctor_telephone;
    }

    public void setDoctor_telephone(String doctor_telephone) {
        this.doctor_telephone = doctor_telephone;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "doctor_id=" + doctor_id +
                ", doctor_name='" + doctor_name + '\'' +
                ", doctor_url='" + doctor_url + '\'' +
                ", doctor_password='" + doctor_password + '\'' +
                ", doctor_gender='" + doctor_gender + '\'' +
                ", doctor_birthday='" + doctor_birthday + '\'' +
                ", doctor_job='" + doctor_job + '\'' +
                ", doctor_department='" + doctor_department + '\'' +
                ", doctor_telephone='" + doctor_telephone + '\'' +
                '}';
    }
}
