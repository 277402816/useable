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
 * 车辆资源信息用Adapter
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScCarResourceItemAdapter.java, v 0.1 2013-7-15 下午12:17:12 shuang.gao Exp $
 */
public class ScCarResourceItemAdapter extends BaseAdapter {
    
    ArrayList<BasicInfoListAdapter.Info> carResourceInfoList;
    Context context;

    /**
     * 
     * <pre>
     * 把页面中应该显示的信息添加到oInfoInfoList中
     * </pre>
     *
     */
    private void initKeyList(){
        if (this.carResourceInfoList == null)
            this.carResourceInfoList = new ArrayList<BasicInfoListAdapter.Info>();
    }

    public ScCarResourceItemAdapter(Context context) {
        initKeyList();
        this.context = context;
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
        carResourceInfoList.add(item);
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
        carResourceInfoList.add(info);
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
        if (carResourceInfoList.isEmpty()) {
            carResourceInfoList.add(item);
        } else {
            int index = carResourceInfoList.indexOf(item);
            carResourceInfoList.set(index, item);
        }
    }
    /**
     * 
     * <pre>
     * 把页面上显示的信息和数据添加到oInfoInfoList中
     * </pre>
     * 
     * @param item 显示的信息
     * @param data 显示的数据
     * @param pairKey 显示数据对应的key
     */
    public void setItem(String item, String data,String pairKey) {
        for(int i =0;i<carResourceInfoList.size();i++){
            BasicInfoListAdapter.Info info = carResourceInfoList.get(i);
            if (info.key.equals(item.replaceAll(":", "").trim())) {
                info.value = data;
                info.pairKey = pairKey;
            }
        }
    }

    @Override
    public int getCount() {
        return  (carResourceInfoList == null)? 0:carResourceInfoList.size();
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
        for(int i=0; i<carResourceInfoList.size(); i++){
            carResourceInfoList.get(i).value="";
            carResourceInfoList.get(i).pairKey=null;
        }
    }

    
    @Override
    public Object getItem(int arg0) {
        
        return carResourceInfoList.get(arg0);
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
         view = LayoutInflater.from(context).inflate(R.layout.sc_car_resource_info_row_view, null);
         fillView (view,carResourceInfoList.get(position));
        return view;
    }
    
    protected boolean fillView(View view, Info info) {
        
        try {
            TextView TextViewKey = (TextView)view.findViewById(R.id.car_info_row_keytext);
            TextView TextViewValue = (TextView)view.findViewById(R.id.car_info_row_valuetext);
            TextViewKey.setText(info.key);
            TextViewValue.setText(info.value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
