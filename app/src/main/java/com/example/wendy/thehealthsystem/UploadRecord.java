package com.example.wendy.thehealthsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.example.wendy.adapter.UploadListViewAdapter;
import com.example.wendy.function.AppConfiguration;
import com.example.wendy.function.NetworkUtils;
import com.example.wendy.function.PatientInfo;
import com.example.wendy.function.ResourceInfo;
import com.example.wendy.function.UsedTools;
import com.example.wendy.sqlitebean.UserInfo;
import com.example.wendy.interFace.AddNewAudioUploadHelp;
import com.example.wendy.interFace.IndexListItemClickHelp;
import com.example.wendy.utils.CircularLoginImage;
import com.example.wendy.utils.GetImagePath;
import com.example.wendy.utils.PullToLoadMoreListView;
import com.example.wendy.utils.UploadDialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import loopj.android.http.AsyncHttpClient;
import loopj.android.http.JsonHttpResponseHandler;
import loopj.android.http.RequestParams;


/**
 * Created by sjaiwl on 15/3/25.
 */
public class UploadRecord extends Activity implements View.OnClickListener, PullToLoadMoreListView.IXListViewListener
        , IndexListItemClickHelp {

    private TextView goBack;
    private TextView patientName;
    private CircularLoginImage patientImage;
    private PullToLoadMoreListView listView;
    private EditText inputBox;
    private TextView addButton;
    private LinearLayout sortBar;
    private LinearLayout typeImage;
    private LinearLayout typePhoto;
    private LinearLayout typeVideo;
    private LinearLayout typeAudio;
    private LinearLayout uploadRecord_selectSort;
    private PatientInfo patientInfo;
    private String category;
    private static Integer type;
    private Intent intent;
    private static final int REQUEST_CODE_TAKE_GALLERY = 1;//图片
    private static final int REQUEST_CODE_TAKE_CAMERA = 2;//拍照
    private static final int REQUEST_CODE_TAKE_VIDEO = 3;//录视频
    private static final int REQUEST_CODE_UPLOAD_GALLERY = 4;//上传图片
    private static final int REQUEST_CODE_UPLOAD_CAMERA = 5;//上传图片
    private static final int REQUEST_CODE_UPLOAD_VIDEO = 6;//上传视频
    private static String dir = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/MedicalApplication/Camera/patientResource/";
    private File file;
    private String imagePrefix = "ImageFile"; //要保存的图片文件的前缀
    private String videoPrefix = "VideoFile"; //要保存的视频文件的前缀
    private String thumbnailPrefix = "ThumbnailFile"; //要保存的缩略图文件的前缀
    private Intent tempIntent;
    private static File uploadGalleryFile, uploadVideoFile, uploadAudioFile;
    private String filePath = null;
    private Uri originalUri = null;
    private AddNewAudio addNewAudio;
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    private static List<ResourceInfo> recordList = new ArrayList<ResourceInfo>();
    private UploadListViewAdapter uploadListViewAdapter;
    private int index = 0; // 请求列表页 ，0为第一条，lastActivityId为上一条
    private int lastActivityId = 0;
    private Dialog pd;
    private ResourceInfo resourceInfo = null;
    private int degree;
    //偏好设置
    private final String PREFERENCE_NAME = "userSetting" + UserInfo.user.getDoctor_id();
    //缓存路径保存
    private final String RESOURCE_CACHE_PREFERENCE_NAME = "videoCachePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upload_record);
        initView();
        initData();
    }

    private void initView() {
        goBack = (TextView) findViewById(R.id.uploadRecord_backButton);
        patientName = (TextView) findViewById(R.id.uploadRecord_patientName);
        patientImage = (CircularLoginImage) findViewById(R.id.uploadRecord_patientImage);
        listView = (PullToLoadMoreListView) findViewById(R.id.uploadRecord_listView);
        inputBox = (EditText) findViewById(R.id.uploadRecord_bottomEditBox);
        addButton = (TextView) findViewById(R.id.uploadRecord_bottomAdd);
        sortBar = (LinearLayout) findViewById(R.id.uploadRecord_selectSort);
        typeImage = (LinearLayout) findViewById(R.id.uploadRecord_typeImage);
        typePhoto = (LinearLayout) findViewById(R.id.uploadRecord_typePhoto);
        typeVideo = (LinearLayout) findViewById(R.id.uploadRecord_typeVideo);
        typeAudio = (LinearLayout) findViewById(R.id.uploadRecord_typeAudio);
        uploadRecord_selectSort = (LinearLayout) findViewById(R.id.uploadRecord_selectSort);
    }

    private void initData() {
        pd = new UploadDialog(this, R.style.UploadDialog, R.string.upload_dialog_textView);
        pd.setCanceledOnTouchOutside(false);
        intent = new Intent();
        category = getIntent().getStringExtra("RecordSort");
        patientInfo = (PatientInfo) getIntent().getSerializableExtra("PatientOnClick");
        patientName.setText(patientInfo.getPatient_name());
        patientImage.setImageUrl(patientInfo.getPatient_url(), 1);

        uploadRecord_selectSort.setOnClickListener(this);
        goBack.setOnClickListener(this);
        patientImage.setOnClickListener(this);
        addButton.setOnClickListener(this);
        typeImage.setOnClickListener(this);
        typePhoto.setOnClickListener(this);
        typeVideo.setOnClickListener(this);
        typeAudio.setOnClickListener(this);

        listView.setPullRefreshEnable(true);
        uploadListViewAdapter = new UploadListViewAdapter(this, recordList, this);
        listView.setAdapter(uploadListViewAdapter);
        listView.setXListViewListener(this);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sortBar.getVisibility() == View.VISIBLE) {
                    hideFaceLayout();
                }
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        inputBox.setOnClickListener(this);
        inputBox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        inputBox.setGravity(Gravity.TOP);
        inputBox.setSingleLine(false);
        //水平滚动设置为False
        inputBox.setHorizontallyScrolling(false);
        inputBox.setMaxLines(3);
        inputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (!v.getText().toString().trim().isEmpty()) {
                        if (checkNetWorkState()) {
                            //上传文字
                            uploadText(v.getText().toString().trim(), 1);
                            inputBox.setText("");
                        }
                    }
                }
                return false;
            }
        });
        //获取记录
        onRefresh();
        //检查网络状态
        checkNetWorkState();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.uploadRecord_selectSort:
                break;
            case R.id.uploadRecord_backButton:
                finish();
                break;
            case R.id.uploadRecord_patientImage:
                //点击病人头像事件
                intent.putExtra("patientInfo", patientInfo);
                intent.setClass(this, PatientDetails.class);
                startActivity(intent);
                break;
            case R.id.uploadRecord_bottomEditBox:
                hideFaceLayout();
                break;
            case R.id.uploadRecord_bottomAdd:
                if (sortBar.getVisibility() == View.VISIBLE) {
                    hideFaceLayout();
                    UsedTools.showKeyboard(this);
                } else {
                    showFaceLayout();
                }
                break;

            case R.id.uploadRecord_typeImage:
                //图库选择图片
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
                galleryIntent.setType("image/*"); //查看类型 String IMAGE_UNSPECIFIED = "image/*" ;
                Intent wrapperGalleryIntent = Intent.createChooser(galleryIntent, null);
                startActivityForResult(wrapperGalleryIntent, REQUEST_CODE_TAKE_GALLERY);
                break;
            case R.id.uploadRecord_typePhoto:
                //系统拍照
                if (isHasSdcard()) {
                    uploadGalleryFile = new File(file, imagePrefix + format.format(new Date()) + ".jpg");
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //"android.media.action.IMAGE_CAPTURE";
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(uploadGalleryFile));
                startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_CAMERA);
                break;
            case R.id.uploadRecord_typeVideo:
                //系统录像
                if (isHasSdcard()) {
                    uploadVideoFile = new File(file, videoPrefix + format.format(new Date()) + ".3gp");
                }
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(uploadVideoFile));
                startActivityForResult(videoIntent, REQUEST_CODE_TAKE_VIDEO);
                break;
            case R.id.uploadRecord_typeAudio:
                //录音
                addNewAudio = new AddNewAudio(this, myListener);
                addNewAudio.show();
                WindowManager windowManager = this.getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = addNewAudio.getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); // 设置宽度
                lp.gravity = Gravity.CENTER;
                addNewAudio.getWindow().setAttributes(lp);
                break;

            default:
                break;
        }
    }

    //判断是否有可以存储
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    //上传音频
    private AddNewAudioUploadHelp myListener = new AddNewAudioUploadHelp() {
        @Override

        public void refreshActivity(String text) {
            type = 4;
            //获取文件路径
            uploadAudioFile = new File(text);
            if (checkNetWorkState()) {
                //上传
                if (pd != null) {
                    pd.show();
                }
                upload(uploadAudioFile, type);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver resolver = getContentResolver();
        if (requestCode == REQUEST_CODE_TAKE_GALLERY && resultCode == RESULT_OK) {
            originalUri = data.getData();
            type = 2;
            //获取文件的路径
            filePath = GetImagePath.getPath(this, originalUri);
            //预览
            tempIntent = new Intent(UploadRecord.this, ViewResourceActivity.class);
            tempIntent.putExtra("uploadResourcePath", filePath);
            tempIntent.putExtra("uploadResourceType", type);
            startActivityForResult(tempIntent, REQUEST_CODE_UPLOAD_GALLERY);
        }
        if (requestCode == REQUEST_CODE_UPLOAD_GALLERY && resultCode == RESULT_OK) {
            if (checkNetWorkState()) {
                //等待提示
                if (pd != null) {
                    pd.show();
                }
                //上传
                degree = data.getIntExtra("degree", 0);
                getRotateBitmapFile(filePath, degree);
            }
        }
        if (requestCode == REQUEST_CODE_TAKE_CAMERA && resultCode == RESULT_OK) {
            originalUri = Uri.fromFile(uploadGalleryFile);
            type = 2;
            //获取文件的路径
            filePath = uploadGalleryFile.getPath();
            //预览
            tempIntent = new Intent(UploadRecord.this, ViewResourceActivity.class);
            tempIntent.putExtra("uploadResourcePath", filePath);
            tempIntent.putExtra("uploadResourceType", type);
            startActivityForResult(tempIntent, REQUEST_CODE_UPLOAD_CAMERA);
        }
        if (requestCode == REQUEST_CODE_UPLOAD_CAMERA && resultCode == RESULT_OK) {
            if (checkNetWorkState()) {
                //等待提示
                if (pd != null) {
                    pd.show();
                }
                //上传
                degree = data.getIntExtra("degree", 0);
                getRotateBitmapFile(filePath, degree);
            }
        }
        if (requestCode == REQUEST_CODE_TAKE_VIDEO && resultCode == RESULT_OK) {
            type = 3;
            filePath = uploadVideoFile.getPath();
            //预览
            tempIntent = new Intent(UploadRecord.this, ViewResourceActivity.class);
            tempIntent.putExtra("uploadResourcePath", filePath);
            tempIntent.putExtra("uploadResourceType", type);
            startActivityForResult(tempIntent, REQUEST_CODE_UPLOAD_VIDEO);
        }
        if (requestCode == REQUEST_CODE_UPLOAD_VIDEO && resultCode == RESULT_OK) {
            if (checkNetWorkState()) {
                //上传
                if (pd != null) {
                    pd.show();
                }
                Bitmap bitmap = UsedTools.getVideoThumbnail(uploadVideoFile.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
                postData(uploadVideoFile, generateFile(bitmap, thumbnailPrefix), type);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    /*
    //    * 上传文件
    //	  */
    @SuppressLint("ShowToast")
    public void upload(final File file, Integer type) {
        RequestParams params = new RequestParams();
        try {
            params.put("doctor_id", UserInfo.user.getDoctor_id().toString());
            params.put("suffer_id", patientInfo.getId().toString());
            params.put("resource_type", type.toString());
            params.put("resource_size", UsedTools.generateFileSize(file));
            params.put("resource_category", category);
            params.put("resource_url", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = AppConfiguration.newResourceUrl;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(JSONObject response) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                Gson gson = new Gson();
                resourceInfo = null;
                resourceInfo = gson.fromJson(response.toString(), ResourceInfo.class);
                if (resourceInfo != null) {
                    recordList.add(resourceInfo);
                    uploadListViewAdapter.notifyDataSetChanged();
                    listView.setSelection(listView.getCount() - 1);
                    //加入到缓存目录
                    SharedPreferences sharedPreferences = getSharedPreferences(RESOURCE_CACHE_PREFERENCE_NAME, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(resourceInfo.getResource_url(), file.getPath());
                    editor.commit();
                } else {
                    Toast.makeText(UploadRecord.this, "上传失败", Toast.LENGTH_LONG).show();
                }
            }

            @SuppressLint("ShowToast")
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(UploadRecord.this, "网络访问异常,请重试", Toast.LENGTH_LONG).show();

            }
        });
    }

    //    /*
    //    * 上传文字
    //	  */
    @SuppressLint("ShowToast")
    public void uploadText(String text, Integer type) {
        RequestParams params = new RequestParams();
        params.put("doctor_id", UserInfo.user.getDoctor_id().toString());
        params.put("suffer_id", patientInfo.getId().toString());
        params.put("resource_type", type.toString());
        params.put("resource_size", "0.01Kb");
        params.put("resource_category", category);
        params.put("resource_description", text);
        String url = AppConfiguration.newResourceUrl;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(JSONObject response) {
                Gson gson = new Gson();
                resourceInfo = null;
                resourceInfo = gson.fromJson(response.toString(), ResourceInfo.class);
                if (resourceInfo != null) {
                    recordList.add(resourceInfo);
                    uploadListViewAdapter.notifyDataSetChanged();
                    listView.setSelection(listView.getCount() - 1);
                } else {
                    Toast.makeText(UploadRecord.this, "发送失败", Toast.LENGTH_LONG).show();
                }
            }

            @SuppressLint("ShowToast")
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Toast.makeText(UploadRecord.this, "网络访问异常,请重试", Toast.LENGTH_LONG).show();

            }
        });
    }

    //上传多个文件
    private void postData(final File file, File thumbnailFile, Integer type) {
        com.lidroid.xutils.http.RequestParams params = new com.lidroid.xutils.http.RequestParams();
        params.addBodyParameter("doctor_id", UserInfo.user.getDoctor_id().toString());
        params.addBodyParameter("suffer_id", patientInfo.getId().toString());
        params.addBodyParameter("resource_type", type.toString());
        params.addBodyParameter("resource_size", UsedTools.generateFileSize(file));
        params.addBodyParameter("resource_category", category);
        params.addBodyParameter("resource_url", file);
        params.addBodyParameter("resource_thumbnailUrl", thumbnailFile);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfiguration.newResourceUrl, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Gson gson = new Gson();
                        resourceInfo = null;
                        resourceInfo = gson.fromJson(responseInfo.result.toString(), ResourceInfo.class);
                        if (resourceInfo != null) {
                            recordList.add(resourceInfo);
                            uploadListViewAdapter.notifyDataSetChanged();
                            listView.setSelection(listView.getCount() - 1);
                            //加入到缓存目录
                            SharedPreferences sharedPreferences = getSharedPreferences(RESOURCE_CACHE_PREFERENCE_NAME, Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(resourceInfo.getResource_url(), file.getPath());
                            editor.commit();
                        } else {
                            Toast.makeText(UploadRecord.this, "上传失败", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.i("tag", error.getExceptionCode() + ":" + msg);
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(UploadRecord.this, "网络访问异常,请重试", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private File generateFile(Bitmap bitmap, String prefix) {
        File picture = null;
        if (isHasSdcard()) {
            picture = new File(file, prefix + format.format(new Date()) + ".jpg");
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picture));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                return picture;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return picture;
    }

    private void getRotateBitmapFile(String imagePath, final int degree) {
        //通过ImageLoader加载后获取bitmap会压缩
        final ImageView imageView = new ImageView(this);
        addContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //初始化ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        //加载本地文件
        imageLoader.displayImage("file:///" + imagePath, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //获取旋转后的bitmap
                loadedImage = UsedTools.rotateBitmapByDegree(loadedImage, degree);
                imageView.setImageBitmap(loadedImage);
                imageView.setVisibility(View.GONE);
                //保存旋转后的文件
                uploadGalleryFile = generateFile(loadedImage, imagePrefix);
                //获取缩略图
                loadedImage = UsedTools.getImageThumbnail(uploadGalleryFile.getPath());
                //生成缩略图文件
                File tempFile = generateFile(loadedImage, thumbnailPrefix);
                //上传数据
                postData(uploadGalleryFile, tempFile, type);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });


//        if (degree == 0) {
//            //生成上传文件
//            uploadGalleryFile = new File(imagePath);
//            //获取缩略图
//            Bitmap tempBitmap = UsedTools.getImageThumbnail(uploadGalleryFile.getPath());
//            //生成缩略图文件
//            File tempFile = generateFile(tempBitmap,thumbnailPrefix);
//            //上传数据
//            postData(uploadGalleryFile, tempFile, type);
//        } else {
//            //转换路径为bitmap
//            Bitmap tempBitmap = UsedTools.convertToBitmap(imagePath, Configuration.convertToBitmapWidth, Configuration.convertToBitmapHeight);
//            //旋转bitmap
//            tempBitmap = UsedTools.rotateBitmapByDegree(tempBitmap, degree);
//            //保存旋转后的文件
//            uploadGalleryFile = generateFile(tempBitmap,imagePrefix);
//            //获取缩略图
//            tempBitmap = UsedTools.getImageThumbnail(uploadGalleryFile.getPath());
//            //生成缩略图文件
//            File tempFile = generateFile(tempBitmap,thumbnailPrefix);
//            //上传数据
//            postData(uploadGalleryFile, tempFile, type);
//        }
    }

    public void showFaceLayout() {
        UsedTools.hideKeyboard(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sortBar.setVisibility(View.VISIBLE);
                addButton.setBackground(getResources().getDrawable(R.mipmap.chat_bottom_keyboard));
            }
        }, 50);
    }

    public void hideFaceLayout() {
        sortBar.setVisibility(View.GONE);
        addButton.setBackground(getResources().getDrawable(R.mipmap.chat_bottom_add));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (sortBar.getVisibility() == View.VISIBLE) {
                hideFaceLayout();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    //创建缓存文件夹
    private boolean isHasSdcard() {
        if (hasSdcard()) {
            file = new File(dir);
            if (!file.exists()) {
                // file不存在
                file.mkdirs();
            }
            return true;
        } else {
            Toast.makeText(UploadRecord.this, "未找到存储卡，无法存储", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //下拉加载更多
    @Override
    public void onRefresh() {
        if (!recordList.isEmpty()) {
            lastActivityId = recordList.get(0).getId();
        }
        index = lastActivityId;
        getData();
    }

    private void getData() {
        String url = AppConfiguration.get_patientResourceUrl + "?index="
                + index + "&&doctor_id=" + UserInfo.user.getDoctor_id() + "&&suffer_id=" + patientInfo.getId();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jar = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<ResourceInfo> list = JSON.parseArray(
                                response.toString(), ResourceInfo.class);
                        for (int i = 0; i < list.size(); i++) {
                            recordList.add(0, list.get(i));
                        }
                        uploadListViewAdapter.notifyDataSetChanged();
                        listView.stopRefresh();
                        listView.setRefreshTime(UsedTools.RefreshTime());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadRecord.this, "上传记录获取失败", Toast.LENGTH_SHORT).show();
                        listView.stopRefresh();
                    }
                });
        mRequestQueue.add(jar);
    }

    @Override
    public void onClick(View item, View widget, int position, int which) {
        ResourceInfo resourceInfo = recordList.get(position);
        intent.putExtra("resource", resourceInfo);
        intent.setClass(UploadRecord.this, ShowResourceActivity.class);
        startActivity(intent);
    }


    //检查网络状态
    private boolean checkNetWorkState() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
        boolean uploadSettingState = preferences.getBoolean("uploadSettingState", false);
        if (NetworkUtils.isConnectInternet(this)) {
            if (!NetworkUtils.isConnectWifi(this)) {
                if (uploadSettingState) {
                    Toast.makeText(this, "当前接入的是移动网络，请在“设置”中修改后上传", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        } else {
            Toast.makeText(this, "无法接入网络，请接入网络", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void finish() {
        InputMethodManager imm = (InputMethodManager) inputBox
                .getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(inputBox.getWindowToken(), 0);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        recordList.clear();
        index = lastActivityId = 0;
        InputMethodManager imm = (InputMethodManager) inputBox
                .getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(inputBox.getWindowToken(), 0);
        }
        super.onDestroy();
    }

}
