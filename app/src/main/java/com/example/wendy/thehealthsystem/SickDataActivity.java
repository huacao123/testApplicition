package com.example.wendy.thehealthsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wendy.adapter.MyDataAdapter;
import com.example.wendy.sqlitebean.BloodGlucoseValue;
import com.example.wendy.sqlitebean.UserInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/3/19.
 */

public class SickDataActivity extends AppCompatActivity {

    private TextView tv_addData;
    private ListView lv_myDataList;
    private MyDataAdapter myDataAdapter;
    private List<BloodGlucoseValue> mBGValueList;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sickdata);

/*        Intent intent = getIntent();
        int bloodGlucoseValue_id = intent.getIntExtra("bloodGlucoseValue_id",0);

        if(bloodGlucoseValue_id > 0){
            BloodGlucoseValue bloodGlucoseValue = DataSupport.find(BloodGlucoseValue.class,bloodGlucoseValue_id);
            userInfo.getmBGValueList().add(bloodGlucoseValue);
            Log.d("wenfang","bloodGlucoseValue_id"+bloodGlucoseValue_id);
           // userInfo.update(UserInfo.user.doctor_id);
            userInfo.save();
        }*/

       /* List<BloodGlucoseValue> allBloodGlucoseValue = DataSupport.findAll(BloodGlucoseValue.class);
        Log.d("wenfang","bloodGlucoseValue_id"+allBloodGlucoseValue.size());
        BloodGlucoseValue bloodGlucoseValue = DataSupport.find(BloodGlucoseValue.class,allBloodGlucoseValue.size());
        userInfo.getmBGValueList().add(bloodGlucoseValue);
        userInfo.update(UserInfo.user.doctor_id);*/


        tv_addData = findViewById(R.id.tv_addData);
        tv_addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SickDataActivity.this,AddDataActivity.class));
            }
        });

        userInfo = DataSupport.find(UserInfo.class,UserInfo.user.doctor_id,true);
        lv_myDataList = findViewById(R.id.lv_myDataList);
        mBGValueList = new ArrayList<>();
        Log.d("wenfang","getmBGValueList"+userInfo.getmBGValueList().size());
        mBGValueList = userInfo.getmBGValueList();
        myDataAdapter = new MyDataAdapter(this,mBGValueList);
        lv_myDataList.setAdapter(myDataAdapter);


    }



}
