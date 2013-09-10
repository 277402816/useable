package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.utils.StringUtils;
/**
 * 客户订单详细信息adapter
 * @extends BaseAdapter 
 * @author liuyu
 * @version $Id: ScCustomerOrderInfoAdapter.java, v 0.1 2013-5-14 下午3:04:18 liuyu Exp $
 */
public class ScCustomerOrderInfoAdapter extends BaseAdapter{
    

    ArrayList<BasicInfoListAdapter.Info> custOrderList = new ArrayList<BasicInfoListAdapter.Info>();
    Context context;
    public ScCustomerOrderInfoAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return (custOrderList == null) ? 0:custOrderList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sc_customer_order_info_item, null);
            }
     
           fillView(convertView, custOrderList.get(position));
        return convertView;
    }
    
    /**
     * 
     * 
     * @param item 标签
     * @param data 数据
     */
    public void addItem(String item, String data) {
        BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
        info.key = item;
        info.value = data;
        custOrderList.add(info);
        this.notifyDataSetChanged();
    }
    //删除list中的数据
    public void clear (){
        if(custOrderList != null){
            custOrderList.clear();
        }
        
    }
    /**
     * 添加显示信息
     * @param converView 列表的每一项
     * @param info 显示的信息
     * @return boolean
     */
    public boolean fillView(View converView,BasicInfoListAdapter.Info info){
        TextView lbName = (TextView)converView.findViewById(R.id.lb_name);
        TextView content = (TextView)converView.findViewById(R.id.cust_value);
        CheckBox chValue = (CheckBox)converView.findViewById(R.id.ck_value);
        if(info.key.equals("内饰颜色必选") || info.key.equals("订单流失标识")
           ||info.key.equals("自购车标识")){
            lbName.setText(info.key+":");
            content.setVisibility(View.GONE);
            chValue.setVisibility(View.VISIBLE);
            chValue.setClickable(false);
            chValue.setEnabled(false);
            chValue.setChecked(Boolean.parseBoolean(info.value));
        }else{
            chValue.setVisibility(View.GONE);
            lbName.setText(info.key+":");
            if(StringUtils.isEmpty(info.value)){
                content.setText("");
            }else{
                content.setText(info.value.trim());
            }
        }
        return true;
    }
    
    @Override
    public Object getItem(int position) {
        return custOrderList.get(position);
    }
}
