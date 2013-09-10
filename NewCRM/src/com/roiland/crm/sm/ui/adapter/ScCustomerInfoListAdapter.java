package com.roiland.crm.sm.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Customer;

/**
 * 
 * <pre>
 * 客户选择列表Adapter
 * </pre>
 * extends BaseAdapter
 * @author liuyu
 * @version $Id: ScCustomerInfoListAdapter.java, v 0.1 2013-7-3 下午4:14:32 liuyu Exp $
 */

public class ScCustomerInfoListAdapter extends BaseAdapter{
    
    private List<Customer> customerList;
    
    Context context;
    
    public ScCustomerInfoListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return customerList == null ? 0:customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.sc_customer_select_itme, null);
        }
        fillView(customerList.get(position), convertView);
        return convertView;
    }
    
    public void fillView (Customer item,View convertView){
        TextView custName = (TextView)convertView.findViewById(R.id.customer_name);
        TextView custPhone = (TextView)convertView.findViewById(R.id.customer_phone);
        
        custName.setText(item.getCustName());
        custPhone.setText(item.getCustMobile());
    }

    public void setCustomerList (List<Customer> customerList){
        this.customerList = customerList;
    }
}
