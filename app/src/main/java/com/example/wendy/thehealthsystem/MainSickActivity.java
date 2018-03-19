package com.example.wendy.thehealthsystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wendy.utils.ExitApplication;

/**
 * Created by Administrator on 2018/3/15.
 */

public class MainSickActivity extends FragmentActivity {

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
	private LinearLayout bottomHome;
    private LinearLayout bottomFile;
    private LinearLayout bottomMine;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_sick);
        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_home);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_file);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_mine);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        fragmentTransaction.show(mFragments[0]).commit();
        setFragmentIndicator();
    }

    private void setFragmentIndicator() {
        bottomHome = (LinearLayout) this.findViewById(R.id.bottom_home);
        bottomFile = (LinearLayout) this.findViewById(R.id.bottom_file);
        bottomMine = (LinearLayout) this.findViewById(R.id.bottom_mine);
        bottomHome.setOnClickListener(mOnClickListener);
        bottomFile.setOnClickListener(mOnClickListener);
        bottomMine.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fragmentTransaction = fragmentManager.beginTransaction()
                    .hide(mFragments[0]).hide(mFragments[1])
                    .hide(mFragments[2]);
            switch (v.getId()) {
                case R.id.bottom_home:
                    bottomHome.setBackgroundResource(R.color.fragment_bottom_pressed_color);
                    bottomFile.setBackgroundResource(R.color.fragment_bottom_normal_color);
                    bottomMine.setBackgroundResource(R.color.fragment_bottom_normal_color);
                    fragmentTransaction.show(mFragments[0]).commit();
                    break;
                case R.id.bottom_file:
                    bottomHome.setBackgroundResource(R.color.fragment_bottom_normal_color);
                    bottomFile.setBackgroundResource(R.color.fragment_bottom_pressed_color);
                    bottomMine.setBackgroundResource(R.color.fragment_bottom_normal_color);
                    fragmentTransaction.show(mFragments[1]).commit();
                    break;
                case R.id.bottom_mine:
                    bottomHome.setBackgroundResource(R.color.fragment_bottom_normal_color);
                    bottomFile.setBackgroundResource(R.color.fragment_bottom_normal_color);
                    bottomMine.setBackgroundResource(R.color.fragment_bottom_pressed_color);
                    fragmentTransaction.show(mFragments[2]).commit();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainSickActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT)
                    .show();
            exitTime = System.currentTimeMillis();
            return;
        }
        ExitApplication.getInstance().exit();
    }
}
