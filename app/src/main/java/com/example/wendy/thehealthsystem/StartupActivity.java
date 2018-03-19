package com.example.wendy.thehealthsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Administrator on 2018/3/13.
 * 启动页
 */

public class StartupActivity extends AppCompatActivity {

    private int[] images = {R.drawable.start0, R.drawable.start1, R.drawable.start2, R.drawable.start3};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_startup);
        initImage();

    }

    private void initImage() {
        ImageView startupImage = findViewById(R.id.startupImage);
        startupImage.setImageResource(images[new Random().nextInt(images.length)]);
        //进行缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.1f, 1.4f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //finish();
                startActivity(new Intent(StartupActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startupImage.startAnimation(scaleAnimation);
    }

    @Override
    public void onBackPressed() {

    }
}

