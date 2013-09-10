package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客户信息管理用Adapter
 * </pre>
 *
 * @author  Zhao.jiaqi
 * @version $Id: SmCustomerInfoItemAdapter.java, v 0.1 2013-8-6 上午9:17:06  Zhao.jiaqi Exp $
 */
public class SmCustomerInfoItemAdapter extends BaseAdapter {
    Boolean editable;
    ArrayList<BasicInfoListAdapter.Info> customerInfoList;
    Context context;
    String []CheckBoxValue ;
    /**
     * 
     * <pre>
     * 把页面中应该显示的信息添加到oInfoInfoList中
     * </pre>
     *
     */
    private void initKeyList()
    {
        if (this.customerInfoList == null)
            this.customerInfoList = new ArrayList<BasicInfoListAdapter.Info>();
    }

    /**
     *  构造方法  初始化一些信息
     * @param context 上下文
     */
    public SmCustomerInfoItemAdapter(Context context) {
        initKeyList();
        this.context = context;
        CheckBoxValue = new String[]{ context.getString(R.string.rebuyStoreCustTag),
                                        context.getString(R.string.rebuyOnlineCustTag),
                                        context.getString(R.string.changeCustTag),
                                        context.getString(R.string.loanCustTag),
                                        context.getString(R.string.regularCustTag),
                                        context.getString(R.string.bigCustTag),
                                        context.getString(R.string.headerQuartCustTag),};
    }

    /**
     * 
     * <pre>
     * 把信息和数据添加到oInfoInfoList中，供页面显示
     * </pre>
     *
     * @param item 销售线索
     */
    public void addItem(BasicInfoListAdapter.Info item) {
        customerInfoList.add(item);
    }

    /**
     * 
     * <pre>
     * 把信息和数据添加到oInfoInfoList中，供页面显示
     * </pre>
     *
     * @param item 显示的信息
     * @param data 显示的数据
     * @param pairKey 显示数据对应的key
     */
    public void addItem(String item, String data,String pairKey) {
        BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
        info.key = item;
        info.value = data;
        info.pairKey = pairKey;
        customerInfoList.add(info);
    }

    /**
     * 
     * <pre>
     * 把页面上显示的信息和数据添加到oInfoInfoList中
     * </pre>
     *
     * @param item 销售线索
     */
    public void setItem(BasicInfoListAdapter.Info item) {
        if (customerInfoList.isEmpty()) {
            customerInfoList.add(item);
        } else {
            int index = customerInfoList.indexOf(item);
            customerInfoList.set(index, item);
        }
    }


    @Override
    public int getCount() {
        return  (customerInfoList == null)? 0:customerInfoList.size();
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    /**
     * 
     * <pre>
     * 页面加载时候调用次方法，显示页面中应该显示的信息和数据
     * </pre>
     *
     */
    public void clearData()
    {
        customerInfoList.clear();
    }

    
    @Override
    public Object getItem(int arg0) {
        
        return customerInfoList.get(arg0);
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
         view = LayoutInflater.from(context).inflate(R.layout.sm_customer_info_row_view, null);
         fillView (view,customerInfoList.get(position));
        return view;
    }
    
    protected boolean fillView(View view, Info info) {
        
        try {
            TextView TextViewKey = (TextView)view.findViewById(R.id.cust_info_row_keytext);
            TextView TextViewValue = (TextView)view.findViewById(R.id.cust_info_row_valuetext);
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.cust_info_row_checkbox);
            if(info.key.equals(context.getString(R.string.custMobile))){
                TextViewKey.setText(info.key);
                checkBox.setVisibility(View.INVISIBLE);
                TextViewValue.setText(StringUtils.isEmpty(info.value) ? "":info.value.trim());
            }else if(info.key.equals(context.getString(R.string.custOtherPhone))){
                TextViewKey.setText(info.key);
                checkBox.setVisibility(View.INVISIBLE);
                TextViewValue.setText(StringUtils.isEmpty(info.value) ? "":info.value.trim());
            }else if(info.key.equals(context.getString(R.string.birthday))){
                TextViewKey.setText(info.key);
                checkBox.setVisibility(View.INVISIBLE);
                TextViewValue.setText(info.value);
            }else{
                checkBox.setVisibility(View.INVISIBLE);
                TextViewKey.setText(info.key);
                TextViewValue.setText(StringUtils.isEmpty(info.value) ? "":info.value.trim());
            }
            
            for(int i=0;i<CheckBoxValue.length;i++){
                if(info.key.equals(CheckBoxValue[i])){
                    TextViewKey.setText(info.key);
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(Boolean.parseBoolean(info.value));
                    TextViewValue.setVisibility(View.INVISIBLE);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public Boolean getEditable() {
        return editable;
    }
    public void setEditable(Boolean editable) {
        this.editable = editable;
        for (BasicInfoListAdapter.Info row : customerInfoList) {
            //row.setEditable(editable);
        }
    }

}
