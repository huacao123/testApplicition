package com.example.wendy.thehealthsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.example.wendy.function.AppConfiguration;
import com.example.wendy.sqlitebean.UserInfo;
import com.example.wendy.smart.WebImageCache;
import com.example.wendy.sqlitebean.MyDBHelper;
import com.example.wendy.utils.ExitApplication;
import com.example.wendy.utils.UploadDialog;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;


/**
 * Created by sjaiwl on 15/3/19.
 */
public class LoginActivity extends Activity {

    private MyDBHelper myDBHelper;
    private SQLiteDatabase database;
    private UserInfo userInfo = null;
    private String classes = "doctor";

    private EditText username;
    private EditText password;
    private RelativeLayout loginButton;
    private TextView registerButton;
    private TextView forgetPass;

    private final String PREFERENCE_NAME = "userInfo";
    private String userName, passWord;

    private static final int REQUEST_CODE_FOR_LOGIN = 1;//登录
    private Dialog dialog;
    private static boolean isShowNetWorkState;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_page);

        //startActivity(new Intent(this, StartupActivity.class));
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        initView();
        initData();
        ExitApplication.getInstance().addActivity(this);


    }


    private void initView() {
        username = (EditText) this.findViewById(R.id.login_usr);
        password = (EditText) this.findViewById(R.id.login_pass);
        loginButton = (RelativeLayout) this.findViewById(R.id.login_button);
        registerButton = (TextView) this.findViewById(R.id.login_register);
        forgetPass = (TextView) this.findViewById(R.id.login_forget);
        loginButton.setOnClickListener(onClickListener);
        registerButton.setOnClickListener(onClickListener);
        forgetPass.setOnClickListener(onClickListener);
        isShowNetWorkState = true;
    }

    private void initData() {
        dialog = new UploadDialog(this, R.style.UploadDialog, R.string.login_dialog_textView);
        dialog.setCanceledOnTouchOutside(false);

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        username.setText(preferences.getString("UserName", null));
        password.setText(preferences.getString("PassWord", null));
/*
        if (isShowNetWorkState) {
            //判断网络接入状态
            if (NetworkUtils.isConnectInternet(this)) {
                if (NetworkUtils.isConnectWifi(this)) {
                    Toast.makeText(this, "当前接入的是wifi网络，请放心使用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "当前接入的是移动网络，请注意流量", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "无法接入网络，请接入网络后重试", Toast.LENGTH_SHORT).show();
            }
            isShowNetWorkState = false;
        }*/
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_button:
                    doLogin();
                    break;
                case R.id.login_forget:
                    //doForget();
                    break;
                case R.id.login_register:
                    doRegister();
                    break;
                default:
                    break;

            }
        }
    };

    private void doLogin() {
        if (checkData()) {
            if (dialog != null) {
                dialog.show();
            }

            if (isCorrect()){
                Intent intent;
                if (classes.equals("doctor")){
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(LoginActivity.this, MainSickActivity.class);
                }

                startActivityForResult(intent, REQUEST_CODE_FOR_LOGIN);
            }
            //postData();
        }
    }

    private void doRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void doForget() {
        ForgetPasswordActivity forgetPasswordActivity = new ForgetPasswordActivity();
        forgetPasswordActivity.show(LoginActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userName = username.getText().toString();
        passWord = password.getText().toString();
        editor.putString("UserName", userName);
        editor.putString("PassWord", passWord);
        editor.commit();

    }

    public boolean isCorrect() {
        List<UserInfo> allUserInfo = DataSupport.findAll(UserInfo.class);
        for(int i=0 ; i<allUserInfo.size();i++){
            if (allUserInfo.get(i).getDoctor_name().equals(username.getText().toString())
                    && allUserInfo.get(i).getDoctor_password().equals(password.getText().toString())){
                userInfo = new UserInfo(allUserInfo.get(i).getDoctor_name(),allUserInfo.get(i).getDoctor_password(),allUserInfo.get(i).getDoctor_url());
                UserInfo.setUserInfo(userInfo);
                classes = allUserInfo.get(i).getDoctor_url();
                return true;
            }
        }
        Toast.makeText(this, "账号或密码错误，请重新输入", Toast.LENGTH_LONG).show();
        return false;

        /*String dbUserName;
        String dbPassword;
        String dbClasses;
        Integer dbPersonid;
        myDBHelper = new MyDBHelper(this, "APP_Login.db", null, 1);
        database = myDBHelper.getWritableDatabase();
        //SQLiteDatabase db = Connector.getDatabase();

        Cursor cursor = database.query("person", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                dbUserName = cursor.getString(cursor.getColumnIndex("username"));
                dbPassword = cursor.getString(cursor.getColumnIndex("password"));
                dbClasses = cursor.getString(cursor.getColumnIndex("classes"));
                dbPersonid = cursor.getInt(cursor.getColumnIndex("personid"));


                if (dbUserName.equals(username.getText().toString()) &&
                        dbPassword.equals(password.getText().toString())) {
               //     userInfo = new UserInfo(dbUserName,dbPassword,dbClasses,dbPersonid);
               //     UserInfo.setUserInfo(userInfo);
                    classes = dbClasses;
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        Toast.makeText(this, "账号或密码错误，请重新输入", Toast.LENGTH_LONG).show();
        return false;*/
    }

    private boolean checkData() {
        if (username.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_LOGIN && resultCode == RESULT_OK) {
            WebImageCache webImageCache = new WebImageCache(getApplicationContext());
            webImageCache.clear();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(LoginActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT)
                    .show();
            exitTime = System.currentTimeMillis();
            return;
        }
        ExitApplication.getInstance().exit();
    }
}
