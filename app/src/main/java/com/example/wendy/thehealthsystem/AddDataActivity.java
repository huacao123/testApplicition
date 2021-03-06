package com.example.wendy.thehealthsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy.sqlitebean.BloodGlucoseValue;
import com.example.wendy.sqlitebean.UserInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_date;
    private RadioGroup rg_timeSelect;
    private EditText et_boldGlucoseLevel;
    private TextView tv_ensure;
    private TextView tv_cancel;
    private String timeSelect = "午餐前";
    private int[] data = new int[3] ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);

        et_date = findViewById(R.id.et_data);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDataActivity.this);
                builder.setTitle("请选择时间").setIcon(R.mipmap.ic_launcher);
                final LinearLayout layout_alert = (LinearLayout)getLayoutInflater().inflate(R.layout.datepicker_dialog,null);
                builder.setView(layout_alert);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker = layout_alert.findViewById(R.id.date_picker);
                        data[0] = datePicker.getYear();
                        data[1] = datePicker.getMonth()+1;
                        data[2] = datePicker.getDayOfMonth();
                        et_date.setText(data[0]+"-"+data[1]+"-"+data[2]);

                    }
                }).create().show();
            }
        });

        rg_timeSelect = findViewById(R.id.rg_timeSelect);
        rg_timeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_time1:
                        timeSelect = "午餐前";
                        break;
                    case R.id.rb_time2:
                        timeSelect = "午餐后";
                        break;
                }
            }
        });

        et_boldGlucoseLevel=findViewById(R.id.et_boldGlucoseLevel);

        tv_ensure = findViewById(R.id.tv_ensure);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        tv_ensure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ensure:
                BloodGlucoseValue bloodGlucoseValue = new BloodGlucoseValue();

                bloodGlucoseValue.setBoldGlucoseLevelValue(
                        et_boldGlucoseLevel.getText().toString());
                bloodGlucoseValue.setTimeSelect(timeSelect);
                bloodGlucoseValue.setYear(data[0]);
                bloodGlucoseValue.setMouth(data[1]);
                bloodGlucoseValue.setDay(data[2]);
                bloodGlucoseValue.save();

                UserInfo userInfo = DataSupport.find(UserInfo.class,UserInfo.user.doctor_id,true);
                userInfo.getmBGValueList().add(bloodGlucoseValue);
                userInfo.save();

                startActivity(new Intent(this,SickDataActivity.class));
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }

    }
}
