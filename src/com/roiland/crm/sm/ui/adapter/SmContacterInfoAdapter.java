package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * 
 * <pre>
 * 联系人详细信息Adapter
 * </pre>
 * @extends BaseAdapter
 * @author liuyu
 * @version $Id: SmContacterInfoAdapter.java, v 0.1 2013-5-20 上午10:09:37 liuyu Exp $
 */
public class SmContacterInfoAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<BasicInfoListAdapter.Info> contacterList;
    public SmContacterInfoAdapter(Context context){
        this.context = context;
        if(contacterList == null){
            contacterList = new ArrayList<BasicInfoListAdapter.Info>();
        }
    }
    @Override
    public int getCount() {
        return contacterList != null ? contacterList.size() :0;
    }

    @Override
    public Object getItem(int position) {
        return contacterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sm_contacter_info_item, null);
        }
        fillView(convertView,contacterList.get(position));
        return convertView;
    }
    
    public void addView(String key,String value){
        BasicInfoListAdapter.Info info = new Info();
        info.key = key;
        info.value = value;
        contacterList.add(info);
    }
    
    private void fillView(View convertView, Info info) {
        TextView key = (TextView) convertView.findViewById(R.id.lb_name);
        TextView value = (TextView) convertView.findViewById(R.id.cont_value);
        CheckBox ck = (CheckBox) convertView.findViewById(R.id.ck_value);
        if((context.getString(R.string.prim_contanter)).equals(info.key)){
            key.setText(info.key+":");
            value.setVisibility(View.INVISIBLE);
            ck.setVisibility(View.VISIBLE);
            ck.setChecked(Boolean.parseBoolean(info.value));
        }else{
            key.setText(info.key+":");
            value.setText(info.value);
        }
       
    }

}
