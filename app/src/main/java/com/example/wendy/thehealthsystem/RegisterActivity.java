package com.example.wendy.thehealthsystem;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy.sqlitebean.UserInfo;
import com.example.wendy.sqlitebean.MyDBHelper;

import cn.bmob.v3.Bmob;

/**
 * Created by sjaiwl on 15/3/28.
 */
public class RegisterActivity extends Activity {

    private MyDBHelper myDBHelper;
    private SQLiteDatabase database;
    private String classes = "doctor";

    private TextView cancelButton;
    private TextView userSexManImage;
    private TextView userSexWomanImage;
    private LinearLayout manButton;
    private LinearLayout womanButton;
    private EditText userName;
    private EditText userPass;
    private EditText userConfirmPass;
    private RelativeLayout registerButton;
    //private String gender = null;
    private String username;
    private String userPassword;

    private final String PREFERENCE_NAME = "userInfo";
    private String successResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_page);
        initView();
        initData();
        Bmob.initialize(this,"Your Application ID");
    }

    private void initView() {
        cancelButton = (TextView) findViewById(R.id.registerPage_cancelButton);
        userSexManImage = (TextView) findViewById(R.id.registerPage_userSexManImage);
        userSexWomanImage = (TextView) findViewById(R.id.registerPage_userSexWomanImage);
        manButton = (LinearLayout) findViewById(R.id.registerPage_userSexMan);
        womanButton = (LinearLayout) findViewById(R.id.registerPage_userSexWoman);
        userName = (EditText) findViewById(R.id.registerPage_userName);
        userPass = (EditText) findViewById(R.id.registerPage_userPassword);
        userConfirmPass = (EditText) findViewById(R.id.registerPage_userConfirmPassword);
        registerButton = (RelativeLayout) findViewById(R.id.registerPage_registerButton);

    }

    private void initData() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSexManImage.setBackground(getResources().getDrawable(R.mipmap.r_man_after));
                userSexWomanImage.setBackground(getResources().getDrawable(R.mipmap.r_woman_before));
                classes = "doctor";
            }
        });

        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSexManImage.setBackground(getResources().getDrawable(R.mipmap.r_man_before));
                userSexWomanImage.setBackground(getResources().getDrawable(R.mipmap.r_woman_after));
                classes = "sick";
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    //postData();
                    insertData();
                }
            }
        });
    }

    public void insertData() {
/*
        myDBHelper = new MyDBHelper(this, "APP_Login.db", null, 1);
        database = myDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("classes", classes);
        contentValues.put("username", userName.getText().toString());
        contentValues.put("password", userPass.getText().toString());
        database.insert("person", null, contentValues);
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
*/

        UserInfo userInfo = new UserInfo();
        userInfo.setDoctor_name(userName.getText().toString());
        userInfo.setDoctor_password(userPass.getText().toString());
        userInfo.setDoctor_url(classes);
        userInfo.save();
        if(userInfo.save()){
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }else {
            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
        }

        /*BloodGlucoseValue bloodGlucoseValue = new BloodGlucoseValue();
        bloodGlucoseValue.setBgvalue("100");
        bloodGlucoseValue.save();
        userInfo.setBloodGlucoseValue(bloodGlucoseValue);
        userInfo.save();*/

    }

    private boolean checkData() {
        if (userName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入姓名！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (classes == null) {
            Toast.makeText(this, "请选择类别！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userPass.getText().toString().trim().isEmpty() || userConfirmPass.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(userPass.getText().toString().trim().equals(userConfirmPass.getText().toString().trim()))) {
            Toast.makeText(this, "输入密码不一致！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*private void postData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("doctor_name", userName.getText().toString().trim());
        //map.put("doctor_gender", gender);
        map.put("doctor_password", userPass.getText().toString().trim());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject(map);
        String url = AppConfiguration.registerUrl;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            successResponse = response.get("success").toString();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (successResponse.equals("1")) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            username = userName.getText().toString().trim();
                            userPassword = userPass.getText().toString().trim();
                            editor.putString("UserName", username);
                            editor.putString("PassWord", userPassword);
                            editor.commit();
                            finish();
                        }
                        if (successResponse.equals("-1")) {
                            Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        } else if (successResponse.equals("0")) {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "网络访问异常", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonRequest);
    }*/
}
