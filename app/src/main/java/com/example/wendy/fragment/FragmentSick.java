package com.example.wendy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wendy.thehealthsystem.R;

/**
 * Created by Administrator on 2018/3/19.
 */

public class FragmentSick extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sick_page, container, false);
    }

}