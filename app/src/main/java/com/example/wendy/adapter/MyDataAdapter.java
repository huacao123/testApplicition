package com.example.wendy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wendy.sqlitebean.BloodGlucoseValue;
import com.example.wendy.thehealthsystem.R;
import com.example.wendy.utils.CircularLoginImage;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MyDataAdapter extends BaseAdapter {

    private List<BloodGlucoseValue> mBGValueList;
    private LayoutInflater layoutInflater;
    private Context context;
    private int[] data = new int[3];

    public MyDataAdapter(Context context, List<BloodGlucoseValue> mBGValueList){
        this.context = context;
        this.mBGValueList = mBGValueList;
    }
    public static class ViewHolder {
        public TextView dataSelect;
        public TextView timeSelect;
        public TextView boldGlucoseLevelValue;
    }

    @Override
    public int getCount() {
        return mBGValueList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBGValueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获得组件，实例化组件
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final MyDataAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new MyDataAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.data_item,
                    null);
            holder.dataSelect = (TextView) convertView
                    .findViewById(R.id.tv_data);
            holder.timeSelect = (TextView) convertView
                    .findViewById(R.id.tv_timeSelect);
            holder.boldGlucoseLevelValue = (TextView) convertView
                    .findViewById(R.id.tv_boldGlucoseLevelValue);
            convertView.setTag(holder);
        } else {
            holder = (MyDataAdapter.ViewHolder) convertView.getTag();
        }
        if (!mBGValueList.isEmpty()) {
            data[0] = mBGValueList.get(position).getYear();
            data[1] = mBGValueList.get(position).getMouth();
            data[2] = mBGValueList.get(position).getDay();
          //  Log.d("wenfang","data = "+data[0]+"-"+data[1]+"-"+data[2]);
            holder.dataSelect.setText(data[0]+"-"+data[1]+"-"+data[2]+"    ");
            holder.timeSelect.setText(mBGValueList.get(position).getTimeSelect()+"    ");
            holder.boldGlucoseLevelValue.setText(mBGValueList.get(position).getBoldGlucoseLevelValue());
        }
        return convertView;
    }
}
