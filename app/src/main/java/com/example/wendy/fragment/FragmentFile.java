package com.example.wendy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.wendy.thehealthsystem.R;
import com.example.wendy.thehealthsystem.SickAddDataActivity;
import com.example.wendy.thehealthsystem.SickChartActivity;
import com.example.wendy.thehealthsystem.SickMedicalRecordActivity;


/**
 * Created by sjaiwl on 15/3/19.
 */
public class FragmentFile extends Fragment implements View.OnClickListener{
    private Button btn_addData;
    private Button btn_chart;
    private Button btn_medicalRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        btn_addData = (Button)getActivity().findViewById(R.id.btn_addData);
        btn_chart = (Button)getActivity().findViewById(R.id.btn_chart);
        btn_medicalRecord = (Button)getActivity().findViewById(R.id.btn_medicalRecord);

        btn_addData.setOnClickListener(this);
        btn_chart.setOnClickListener(this);
        btn_medicalRecord.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addData:
                startActivity(new Intent(getActivity(), SickAddDataActivity.class));
                break;

            case R.id.btn_chart:
                startActivity(new Intent(getActivity(), SickChartActivity.class));
                break;

            case R.id.btn_medicalRecord:
                startActivity(new Intent(getActivity(), SickMedicalRecordActivity.class));
                break;
        }
    }



}
