package com.example.wendy.thehealthsystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wendy.function.UsedTools;
import com.example.wendy.function.UserInfo;
import com.example.wendy.sqlite.MyDBHelper;

import java.util.Calendar;

/**
 * Created by sjaiwl on 15/4/3.
 */
public class UpdateInformation extends Activity {
    private String updateType;
    private TextView cancelButton;
    private TextView topText;
    private ImageView updateSexMan;
    private ImageView updateSexWoman;
    private EditText inputText;
    private LinearLayout updateName;
    private LinearLayout updateWomanLayout;
    private LinearLayout updateManLayout;
    private LinearLayout updateSex;
    private DatePicker updateBirthday;
    private RelativeLayout updateButton;

    private final String PREFERENCE_NAME = "userInfo";
    private String gender = null;
    private String birthday = "";
    private String successResponse = null;

    private String personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.updateinformation);
        initView();
        getUpdateType();
        initData();
        personId = UserInfo.user.doctor_id.toString();
    }

    private void initView() {
        cancelButton = (TextView) findViewById(R.id.updateInformation_cancelButton);
        topText = (TextView) findViewById(R.id.updateInformation_topText);
        updateSexMan = (ImageView) findViewById(R.id.updateInformation_updateSex_man);
        updateSexWoman = (ImageView) findViewById(R.id.updateInformation_updateSex_woman);
        inputText = (EditText) findViewById(R.id.updateInformation_updateName);
        updateName = (LinearLayout) findViewById(R.id.updateInformation_updateNameLayout);
        updateSex = (LinearLayout) findViewById(R.id.updateInformation_updateSexLayout);
        updateManLayout = (LinearLayout) findViewById(R.id.updateInformation_updateSexManLayout);
        updateWomanLayout = (LinearLayout) findViewById(R.id.updateInformation_updateSexWomanLayout);
        updateBirthday = (DatePicker) findViewById(R.id.updateInformation_updateBirthday);
        updateButton = (RelativeLayout) findViewById(R.id.updateInformation_updateButton);
    }

    private void initData() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

    }

    private void getUpdateType() {
        updateType = getIntent().getStringExtra("index");
        switch (updateType) {
            case "updateName":
                topText.setText("姓名");
                updateName.setVisibility(View.VISIBLE);
                if (UserInfo.user.getDoctor_name() != null) {
                    inputText.setText(UserInfo.user.getDoctor_name());
                    inputText.requestFocus(UserInfo.user.getDoctor_name().length());
                }
                break;
            case "updateSex":
                topText.setText("性别");
                updateSex.setVisibility(View.VISIBLE);
                initViewForSex();
                break;
            case "updateBirthday":
                topText.setText("出生日期");
                updateBirthday.setVisibility(View.VISIBLE);
                initViewForBirthday();
                break;
            case "updateDepartment":
                topText.setText("科室");
                updateName.setVisibility(View.VISIBLE);
                if (UserInfo.user.getDoctor_department() != null) {
                    inputText.setText(UserInfo.user.getDoctor_department());
                    inputText.requestFocus(UserInfo.user.getDoctor_department().length());
                }
                break;
            case "updateJob":
                topText.setText("职务");
                updateName.setVisibility(View.VISIBLE);
                if (UserInfo.user.getDoctor_job() != null) {
                    inputText.setText(UserInfo.user.getDoctor_job());
                    inputText.requestFocus(UserInfo.user.getDoctor_job().length());
                }
                break;
            case "updateTelephone":
                topText.setText("电话");
                updateName.setVisibility(View.VISIBLE);
                if (UserInfo.user.getDoctor_telephone() != null) {
                    inputText.setText(UserInfo.user.getDoctor_telephone());
                    inputText.requestFocus(UserInfo.user.getDoctor_telephone().length());
                }
                break;
            default:
                break;
        }
    }

    private void initViewForSex() {
        if (UserInfo.user.getDoctor_url() != null) {
            if (UserInfo.user.getDoctor_url().equals("doctor")) {
                gender = "doctor";
                updateSexMan.setBackground(getResources().getDrawable(R.mipmap.r_man_after));
            }
            if (UserInfo.user.getDoctor_url().equals("sick")) {
                gender = "sick";
                updateSexWoman.setBackground(getResources().getDrawable(R.mipmap.r_woman_after));
            }
        }
        updateSexMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSexMan.setBackground(getResources().getDrawable(R.mipmap.r_man_after));
                updateSexWoman.setBackground(getResources().getDrawable(R.mipmap.r_woman_before));
                gender = "doctor";
            }
        });

        updateSexWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSexMan.setBackground(getResources().getDrawable(R.mipmap.r_man_before));
                updateSexWoman.setBackground(getResources().getDrawable(R.mipmap.r_woman_after));
                gender = "sick";
            }
        });
    }

    private void initViewForBirthday() {
        Calendar ca = Calendar.getInstance();
        int nowYear = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String nowMonth = UsedTools.formatTime(ca.get(Calendar.MONTH) + 1);
        String nowDay = UsedTools.formatTime(ca.get(Calendar.DAY_OF_MONTH));
        birthday = nowYear + "-" + nowMonth + "-" + nowDay;
        updateBirthday.init(nowYear, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthday = year + "-" + UsedTools.formatTime((monthOfYear + 1)) + "-" + UsedTools.formatTime(dayOfMonth);
            }
        });
    }

    private void updateData() {
        switch (updateType) {
            case "updateName":
                if (inputText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "请输入姓名！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (UserInfo.user.getDoctor_name() == null || !(inputText.getText().toString().trim().equals(UserInfo.user.getDoctor_name()))) {
                    updatePersonData("username", inputText.getText().toString().trim());
                }
                break;
            case "updateSex":
                if (gender == null) {
                    Toast.makeText(this, "请选择类别！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if ((UserInfo.user.getDoctor_url() == null || !(UserInfo.user.getDoctor_url().equals(gender)))) {
                    updatePersonData("classes", gender);

                }
                break;
            case "updateBirthday":
                if (UserInfo.user.getDoctor_birthday() == null || !(UserInfo.user.getDoctor_birthday().equals(birthday))) {
                    updatePersonData("birthday", birthday);
                }
                break;
            case "updateDepartment":
                if (inputText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "请输入科室！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (UserInfo.user.getDoctor_department() == null || !(inputText.getText().toString().trim().equals(UserInfo.user.getDoctor_department()))) {
                    updatePersonData("department", inputText.getText().toString().trim());
                }

                break;
            case "updateJob":
                if (inputText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "请输入职务！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (UserInfo.user.getDoctor_job() == null || !(inputText.getText().toString().trim().equals(UserInfo.user.getDoctor_job()))) {
                    updatePersonData("job", inputText.getText().toString().trim());
                }

                break;
            case "updateTelephone":
                if (inputText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "请输入电话！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (inputText.getText().toString().trim().length() != 11) {
                    Toast.makeText(this, "电话输入有误！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (UserInfo.user.getDoctor_telephone() == null || !(inputText.getText().toString().trim().equals(UserInfo.user.getDoctor_telephone()))) {
                    updatePersonData("telephone", inputText.getText().toString().trim());
                }
                break;
            default:
                break;
        }
    }


    public void updatePersonData(final String type,final String value) {

        MyDBHelper myDBHelper = new MyDBHelper(this, "APP_Login.db", null, 1);
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(type,value);
        Log.d("wenfang","personId:"+personId);
        database.update("person", values,"personid=?", new String[]{personId});
        database.close();
        afterPostData(type,value);
        Toast.makeText(UpdateInformation.this, "修改成功", Toast.LENGTH_LONG).show();

    }


/*    private void postData(final String type, final String value) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(type, value);
        map.put("doctor_id", UserInfo.user.getDoctor_id());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject(map);
        String url = AppConfiguration.updateUserUrl;
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
                            Toast.makeText(UpdateInformation.this, "修改成功", Toast.LENGTH_SHORT).show();
                            afterPostData(type, value);
                        } else {
                            Toast.makeText(UpdateInformation.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateInformation.this, "网络访问异常", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonRequest);
    }*/

    private void afterPostData(String type, String value) {
        switch (type) {
            case "username":
                UserInfo.user.setDoctor_name(value);
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserName", value);
                editor.commit();
                finish();
                break;
            case "classes":
                UserInfo.user.setDoctor_gender(value);
                finish();
                break;
            case "birthday":
                UserInfo.user.setDoctor_birthday(value);
                finish();
                break;
            case "department":
                UserInfo.user.setDoctor_department(value);
                finish();
                break;
            case "job":
                UserInfo.user.setDoctor_job(value);
                finish();
                break;
            case "telephone":
                UserInfo.user.setDoctor_telephone(value);
                finish();
                break;
            default:
                break;
        }
    }
}
