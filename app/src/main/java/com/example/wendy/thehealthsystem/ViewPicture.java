package com.example.wendy.thehealthsystem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.wendy.sqlitebean.UserInfo;
import com.example.wendy.smart.WebImageCache;
import com.example.wendy.utils.BitmapUtil;
import com.example.wendy.utils.DragImageView;

public class ViewPicture extends Activity {
    private int window_width, window_height;// 控件宽度
    private DragImageView dragImageView;// 自定义控件
    private int state_height;// 状态栏的高度
    private ViewTreeObserver viewTreeObserver;
    private RelativeLayout layout;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_picture);
        layout = (RelativeLayout) findViewById(R.id.viewPage_layout);
        dragImageView = (DragImageView) findViewById(R.id.view_picture);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /** 获取可見区域高度 **/
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        Bitmap bitmap = new WebImageCache(this).get(UserInfo.user.getDoctor_url());
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.userphoto);
        }
        bitmap = BitmapUtil.getBitmap(bitmap, window_width, window_height);
        // 设置图片
        dragImageView.setImageBitmap(bitmap);
        dragImageView.setmActivity(this, true);// 注入Activity
        /** 测量状态栏高度 **/
        viewTreeObserver = dragImageView.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (state_height == 0) {
                            // 获取状况栏高度
                            Rect frame = new Rect();
                            getWindow().getDecorView()
                                    .getWindowVisibleDisplayFrame(frame);
                            state_height = frame.top;
                            dragImageView.setScreen_H(window_height
                                    - state_height);
                            dragImageView.setScreen_W(window_width);
                        }
                    }
                });
    }
}
