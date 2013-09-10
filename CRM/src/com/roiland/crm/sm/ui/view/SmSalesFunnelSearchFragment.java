/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * <pre>
 * 销售漏斗搜索画面
 * </pre>
 *
 * @author cjyy
 * @version $Id: SmSalesFunnelSearchFragment.java, v 0.1 2013-5-27 上午12:16:48 cjyy Exp $
 */
public class SmSalesFunnelSearchFragment extends SherlockFragment {

    protected BaseActivity activity ;
    private SmSalesFunnelSearchAdapter searchAdapter;
    private boolean isStartDate = false;
    private boolean isEndDate = true;
    
    SmSalesFunnelSearchAdapter.Info info;

   
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof BaseActivity) {
                this.activity = (BaseActivity) activity;
            }
        } catch (Exception e) {
            this.activity = null;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.sm_sales_funnel_search,  container, false);
        searchAdapter = new SmSalesFunnelSearchAdapter(getActivity());
        ListView listView = (ListView) view.findViewById(R.id.funnel_condition_items);
        
        listView.setAdapter(searchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                onItemClickHandle(index);

            }
            
        });
        //创建搜索按钮
        ImageButton searchButton = (ImageButton) view.findViewById(R.id.funnel_start_search_button);
        searchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                doSearch();
            }
        });
        return view;
        
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                activity.toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * 
     * <pre>
     * 搜索条件点击处理
     * </pre>
     *
     * @param index 备选中项索引
     */
    
    public void onItemClickHandle(int index) {
        if(index == 0 || index == 1){
            getTime(index);
        }
    }
    /**
     * 
     * <pre>
     * 点击开始时间或是结束时间弹出框
     * </pre>
     *
     * @param arg2 点击的开始时间或是结束时间
     */
    public void getTime(int arg2){
        final Calendar c = Calendar.getInstance(); 
        int year = c.get(Calendar.YEAR);     
        int month = c.get(Calendar.MONTH);     
        int day = c.get(Calendar.DAY_OF_MONTH); 
        if(arg2 == 0){
            isStartDate = true;
            isEndDate = false;
        }else{
            isStartDate = false;
            isEndDate = true;
        }
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
//        dialog.getDatePicker().setMinDate(DateFormatUtils.parseDateToLong("1970-01-01"));
        dialog.show();
    }
    
    /**
     * 开始时间和结束时间的点击事件
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener =     
        new DatePickerDialog.OnDateSetListener() {     
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                
                Log.i("DatePickerDialog:",String.valueOf(arg1)+String.valueOf(arg2+1)+String.valueOf(arg3));
                StringBuilder data = new StringBuilder().append(arg1).append("-")  
                    .append(formateTime(arg2+1)).append("-")  
                    .append(formateTime(arg3));
                if(DateFormatUtils.parseDateToLong(data.toString()) <= DateFormatUtils.parseDateToLong("1970-01-01")){
//                    Toast.makeText(getActivity(), "时间不能小于1970-01-01", Toast.LENGTH_LONG).show();
                    return;
                }
                if(isStartDate){
                    try {
                        if(!StringUtils.isEmpty(searchAdapter.getEndTime()) && DataVerify.dateCompare1(data.toString(), searchAdapter.getEndTime())){
                            Toast.makeText(getActivity(), "开始时间大于结束时间", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    searchAdapter.setStartTime(String.valueOf(data));
                }
                if(isEndDate){
                    try {
                        if(!StringUtils.isEmpty(searchAdapter.getStartTime()) && DataVerify.dateCompare1(searchAdapter.getStartTime(),data.toString())){
                            Toast.makeText(getActivity(), "开始时间大于结束时间", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    searchAdapter.setEndTime(String.valueOf(data));
                }
                searchAdapter.notifyDataSetChanged();
            }
    };
        
    /**
     * 
     * <pre>
     * 时间格式转换
     * </pre>
     *
     * @param time 要转换的int型时间
     * @return
     */
    static String formateTime(int time) {  
        
        String timeStr = "";  
        if (time < 10){  
            timeStr = "0" + String.valueOf(time);  
        }else {  
            timeStr = String.valueOf(time);  
        }  
        return timeStr;  
     } 
    

    
    /**
     * 
     * <pre>
     * 执行搜索方法
     * </pre>
     *
     */
    private void doSearch() {
        
          //新建intent传递
            Intent intent = new Intent(getActivity(), SmSalesFunnelActivity.class);
            intent.putExtra("startTime", searchAdapter.getStartTime());
            intent.putExtra("endTime", searchAdapter.getEndTime());
            startActivity(intent);
            
    }
    /**
     * 
     * <pre>
     * 搜索条件项适配对象
     * </pre>
     *
     * @author JKim
     * @version $Id: SmSalesFunnelSearchFragment.java, v 0.1 2013-6-6 上午12:19:33 JKim Exp $
     */
    public class SmSalesFunnelSearchAdapter extends BaseAdapter {
       
        
        List<View>    viewList;
        Context       context;
        String        startTime     = null;                               //开始时间
        String        endTime       = null;                               //结束时间
        String[]      condition    = { getString(R.string.funnel_start_date),
                                       getString(R.string.funnel_end_date)};
        public class Info {
            String label;       //标题
            String value;       //内容
            public Info(String label, String value) {
                this.label = label;
                this.value = value;
            }
            public String getLabel() {
                return label;
            }
            public String getValue() {
                return value;
            }
            
        }
        
        /**
         * @param context
         */
        public SmSalesFunnelSearchAdapter(Context context) {
           this.context = context;
            try {
                viewList = new ArrayList<View>();
                for (int i = 0; i < condition.length; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.sm_sales_funnel_search_item,
                        null);
                    viewList.add(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public CharSequence getCondition(int selectedIndex) {
            return condition[selectedIndex];
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Info getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /** 
         * @param position
         * @param convertView
         * @param parent
         * @return
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View view = super.getView(position, convertView, parent, R.layout.sm_sales_funnel_search_item);
            switch (position) {
                case 0:
                    fillView(viewList.get(position), condition[0], startTime==null?startTime =DataVerify.systemDate():startTime);
                    break;
                case 1:
                    fillView(viewList.get(position), condition[1], endTime==null?endTime = DataVerify.nextMonth():endTime);
                    break;
            }
            return viewList.get(position);
        }
        /** 
         * @param view
         * @param item
         * @return
         * @see com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter#fillView(android.view.View, java.lang.Object)
         */
        protected boolean fillView(View view, String condition, String item) {
            try {
                TextView title = (TextView) view.findViewById(R.id.search_title);
                title.setText(condition);
                TextView value = (TextView) view.findViewById(R.id.search_content);
                value.setText(item);
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
            notifyDataSetChanged();
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
            notifyDataSetChanged();
        }
        
    }
}
