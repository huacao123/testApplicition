package com.sjaiwl.app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjaiwl.app.function.UserInfo;
import com.sjaiwl.app.medicalapplicition.LoginActivity;
import com.sjaiwl.app.medicalapplicition.MineInformation;
import com.sjaiwl.app.medicalapplicition.MineSetting;
import com.sjaiwl.app.medicalapplicition.R;
import com.sjaiwl.app.tools.CircularLoginImage;


/**
 * Created by Administrator on 2018/3/16.
 */

public class FragmentHome extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page, container, false);
    }

}
