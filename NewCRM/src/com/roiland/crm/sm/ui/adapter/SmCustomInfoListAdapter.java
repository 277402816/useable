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
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * 客户信息adapter
 * @extends BaseAdapter
 * @author liuyu
 * @version $Id: SmCustomInfoListAdapter.java, v 0.1 2013-5-14 下午5:43:44 liuyu Exp $
 */
public class SmCustomInfoListAdapter extends BaseAdapter {
    ArrayList<BasicInfoListAdapter.Info> customInfoList;
    //ArrayList<Integer> priorityArray;
    Context                              context;
    Project                              project;
    int                                  size;
    int                                  num;
    public boolean                       moreFlag = true;
    public boolean                       editFlag = false;

    public SmCustomInfoListAdapter(ArrayList<BasicInfoListAdapter.Info> customInfoList,
                                 Context context) {
        this.customInfoList = customInfoList;
        this.context = context;
    }

    public SmCustomInfoListAdapter(Context context) {
        this.customInfoList = new ArrayList<BasicInfoListAdapter.Info>();
        this.context = context;
    }

    /**
     * 
     * <pre>
     * 添加字段项方法
     * </pre>
     *
     * @param item  字段标题
     * @param data  字段值
     */
    public void addItem(String item, String data) {
        BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
        info.key = item;
        info.value = data;
        customInfoList.add(info);
        this.notifyDataSetChanged();
    }
    
    /**
     * 
     * <pre>
     * 删除字段项
     * </pre>
     *
     * @param item  字段标题
     */
    public void removeItem(String item) {
        for (int i = 0; i < customInfoList.size(); i++) {
            if (customInfoList.get(i).key.equals(item.replaceAll(":", ""))) {
                customInfoList.remove(i);
                break;
            }
        }
    }

    /**
     * 
     * <pre>
     * 删除字段项
     * </pre>
     *
     * @param index 字段下标
     */
    public void removeItem(int index) {
        customInfoList.remove(index);
        this.notifyDataSetChanged();
    }

    /**
     * 更新客户信息list信息
     * @param item item名称
     * @param data item内容
     * @param pairKey item内容对应code
     */
    public void setItem(String item, String data) {
        //如果页面加载1次或者data不为空值，则为customInfoList中添加新数据
        if (num == 1 || !StringUtils.isEmpty(data)) {
            for (int i = 0; i < customInfoList.size(); i++) {
                if (customInfoList.get(i).key.equals(item.replaceAll(":", ""))) {
                    customInfoList.get(i).value = data;
                    break;
                }
            }
        }
    }

    /**
     * 获取显示字段总数
     * @return  
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return customInfoList !=null?customInfoList.size():0;
    }

    @Override
    public Object getItem(int arg0) {
        arg0 = getIndex(arg0);
        if (arg0 < customInfoList.size()) {
            return customInfoList.get(arg0);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.sm_opp_info_item, null);
        fillView(convertView, customInfoList.get(position));
        return convertView;
    }
    
    /**
     * 
     * <pre>
     * 显示信息
     * </pre>
     *
     * @param convertView list中每行用到的item
     * @param info 显示的数据
     */
    private void fillView(View convertView, Info info) {
        TextView lbName = (TextView)convertView.findViewById(R.id.lb_name);
        TextView custValue = (TextView)convertView.findViewById(R.id.cust_value);
        CheckBox ckValue = (CheckBox)convertView.findViewById(R.id.ck_value);
        if(context.getString(R.string.rebuyStoreCustTag).equals(info.key) ||
           context.getString(R.string.rebuyOnlineCustTag).equals(info.key) || 
           context.getString(R.string.changeCustTag).equals(info.key) || 
           context.getString(R.string.regularCustTag).equals(info.key) ||
           context.getString(R.string.loanCustTag).equals(info.key) ||
           context.getString(R.string.headerQuartCustTag).equals(info.key) ||
           context.getString(R.string.bigCustTag).equals(info.key)){
            lbName.setText(info.key+":");
            custValue.setVisibility(View.GONE);
            ckValue.setVisibility(View.VISIBLE);
            ckValue.setChecked(Boolean.parseBoolean(info.value));
        }else {
            lbName.setText(info.key+":");
            custValue.setText(info.value);
            ckValue.setVisibility(View.GONE);
        }
        
    }

    /**
     * 
     * <pre>
     * 删除数据
     * </pre>
     *
     */
    public void clearData() {
        customInfoList.clear();
    }

    private int getIndex(int index) {
        if (!moreFlag) {
            if (index == 7) {
                return customInfoList.size() - 1;
            }
        }
        return index;
    }

    public ArrayList<BasicInfoListAdapter.Info> getCustomInfoList() {
        return customInfoList;
    }
}
