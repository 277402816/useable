package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info;

/**
 * 
 * <pre>
 * 试乘试驾adapter
 * </pre>
 * @extends BaseAdapter
 * @author liuyu
 * @version $Id: SmTestDriveInfoAdapter.java, v 0.1 2013-5-20 下午2:13:05 liuyu Exp $
 */
public class ScTestDriveInfoAdapter extends BaseAdapter{

    ArrayList<BasicInfoListAdapter.Info> testInfoList;
    Context context;
    public  ScTestDriveInfoAdapter(Context context){
        this.context = context;
        if(testInfoList == null){
            testInfoList = new ArrayList<BasicInfoListAdapter.Info>();
        }
    }
    
    @Override
    public int getCount() {
        return testInfoList == null?0:testInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return testInfoList == null ? null:testInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sm_test_drive_info_item, null);
        }
        fillView(convertView,testInfoList.get(position));
        return convertView;
    }

    private void fillView(View convertView, Info info) {
        
        TextView key = (TextView)convertView.findViewById(R.id.key);
        TextView value = (TextView)convertView.findViewById(R.id.value);
        key.setText(info.key+":");
        value.setText(info.value);
        
    }

    public void addView (String key,String value){
        BasicInfoListAdapter.Info info = new Info();
        info.key = key;
        info.value = value;
        testInfoList.add(info);
        this.notifyDataSetChanged();
    }
}
