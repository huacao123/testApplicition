package com.example.wendy.thehealthsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wendy.adapter.MyDataAdapter;
import com.example.wendy.sqlitebean.BloodGlucoseValue;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sickdata);

        tv_addData = findViewById(R.id.tv_addData);
        tv_addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SickDataActivity.this,AddDataActivity.class));
            }
        });

        lv_myDataList = findViewById(R.id.lv_myDataList);
        mBGValueList = new ArrayList<>();
        myDataAdapter = new MyDataAdapter();
        lv_myDataList.setAdapter(myDataAdapter);



    }



}
