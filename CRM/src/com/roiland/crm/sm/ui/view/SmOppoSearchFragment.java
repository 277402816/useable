package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sm.GlobalConstant;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售线索高级搜索Fragment
 * </pre>
 * @extends SherlockFragment
 * @author liuyu
 * @version $Id: SmOppoSearchFragment.java, v 0.1 2013-5-20 下午2:45:04 liuyu Exp $
 */
public class SmOppoSearchFragment extends SherlockFragment {

   public List<Dictionary> downloadedResult ;
   private boolean chooseEnd = false;
   protected BaseActivity activity;
   private SmOppoSearchListAdapter adapter;
   int selectedIndex; //选定的搜索项的索引
   private Dictionary brandDic; //brand dictionary
   private Dictionary modelDic;
   private Dictionary followStatus;//流程状态
   private boolean isStartTime = false;
   private boolean isEndTime = true;
   private boolean isPreorderDate = false;//true 按开始正序排序
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
        View view = inflater.inflate(R.layout.sm_oppo_search_info, container,false);
        if (adapter == null) {
            adapter = new SmOppoSearchListAdapter(getActivity());
        }
        adapter.clearData();
        ListView listView = (ListView) view.findViewById(R.id.oppo_info_list);
        ImageButton searchStart = (ImageButton)view.findViewById(R.id.oppo_search_start_button);
        //设置开始查询事件监听
        searchStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Project p = new Project();
                Project.AdvancedSearch advancedSearch = p.new AdvancedSearch();
                advancedSearch.setBrand(adapter.getBrandDic().getDicKey());
                advancedSearch.setModel(adapter.getModelDic().getDicKey());
                advancedSearch.setFollowStatus(adapter.getFollowStatus().getDicKey());
                advancedSearch.setOwner(adapter.getEmployee().getDicKey());
                if(adapter.getSortWay() != null){
                    if(adapter.getSortWay().equals("建立时间倒序排序")){
                        advancedSearch.setOrderBy(advancedSearch.ORDER_BY_BUILDUP_DATE);
                        isPreorderDate = false;
                    }
                    if(adapter.getSortWay().equals("预购日期倒序排序")){
                        advancedSearch.setOrderBy(advancedSearch.ORDER_BY_PREORDER_DATE);
                        isPreorderDate = true;
                    }
                    
                }
                advancedSearch.setStartDate(DateFormatUtils.parseDateToLong(adapter.getStartTime()));
                if(chooseEnd){
                    advancedSearch.setEndDate(DateFormatUtils.parseDateToLong(adapter.getEndTime())+24*60*60*1000);
                }else{
                    advancedSearch.setEndDate(DateFormatUtils.parseDateToLong(adapter.getEndTime()));
                }
                
                //TODO 设置搜索条件
                p.setAdvancedSearch(advancedSearch);
                doSearch(p);
                
                //TODO 搜索操作
            }
        });
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //listView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                if(arg2 == 5 || arg2 == 6){
                    getTime(arg2);
                }else if(arg2 == 4){
                    getOrderType(arg2);
                }else if(arg2 == 3){
                    downloadOwner(arg2);
                }else{
                    downloadDicTask(arg2);   
                }

            }
        });
        return view;
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
        if(arg2 == 5){
            isStartTime = true;
            isEndTime = false;
        }else{
            isStartTime = false;
            isEndTime = true;
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
//                  Toast.makeText(getActivity(), "时间不能小于1970-01-01", Toast.LENGTH_LONG).show();
                  return;
              }
                if(isStartTime){
                    try {
                        if(!StringUtils.isEmpty(adapter.getEndTime()) && DataVerify.dateCompare1(data.toString(), adapter.getEndTime())){
                            Toast.makeText(activity, "开始时间大于结束时间", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    adapter.setStartTime(data);
                }
                if(isEndTime){
                    try {
                        if(!StringUtils.isEmpty(adapter.getStartTime()) && DataVerify.dateCompare1(adapter.getStartTime(),data.toString())){
                            Toast.makeText(activity, "开始时间大于结束时间", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (DataVerify.dateCompare1(data.toString(),DataVerify.systemDate()) && !adapter.getSortWay().equals(getString(R.string.preorder_Date_order))) {
                                Toast.makeText(activity,"结束时间不能大于今日",Toast.LENGTH_LONG).show();
                                return;
                            }
                    } catch (Exception e) {
                        Toast.makeText(activity, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    adapter.setEndTime(data);
                    chooseEnd = true;
                }
                adapter.notifyDataSetChanged();
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
     * 点击排序方式
     * </pre>
     *
     */
    public void getOrderType(int arg2){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(adapter.getCondition(arg2));
        final String[] valueList = {getString(R.string.buildup_date_order),
                                    getString(R.string.preorder_Date_order)};
        builder.setItems(valueList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                adapter.setSortWay(valueList[item]);
                adapter.setEndTime(new StringBuilder(""));
                adapter.notifyDataSetChanged();
                }
            });
            builder.create().show();
    }
    
    public void downloadOwner(int position){
        
        new BaseTask<List<Dictionary>>(activity) {
            List<Dictionary> downloadEmployee ;
            @Override
            protected List<Dictionary> doInBackground(String... params) {
                selectedIndex = Integer.parseInt(params[0]);
                CRMManager manager = ((RoilandCRMApplication)getActivity().getApplication()).getCRMManager();
                try {
                    return manager.getEmployeeList();
                } catch (ResponseException e) {
                    Log.i("-----------downloadEmployee----------", e.getMessage());
                    responseException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Dictionary> result) {
                super.onPostExecute(result);
                if(responseException != null){
                    Toast.makeText(activity, responseException.getMessage(), Toast.LENGTH_LONG).show();
                }
                if(result != null){
                    downloadEmployee = result ;
                    String[] valueList = new String[result.size()];
                    for (int i = 0; i < result.size(); i ++) {
                        valueList[i] = result.get(i).getDicValue();
                    }
                    
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(adapter.getCondition(selectedIndex));
                    builder.setItems(valueList, new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dictionary employee = downloadEmployee.get(which);
                            adapter.setEmployee(employee);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.create().show();
                }
            }
            
        }.execute(String.valueOf(position));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
 
        MenuItem itemCancle = menu.findItem(R.id.cancel);
        itemCancle.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch(item.getItemId()){
            case R.id.cancel:
                activity.onItemSelected(1, -1,null);
                break;
            case android.R.id.home:
                activity.toggle();
                break;
        }
        
        return super.onOptionsItemSelected(item);
        
    }

    /**
     * 
     * <pre>
     * 开始查询任务
     * </pre>
     *
     */
    private void doSearch(Project p) {
       
        chooseEnd = false;
        Bundle bundle = new Bundle();
        bundle.putParcelable("advancedSearch", p);
        bundle.putBoolean("isPreorderDate", isPreorderDate);
        activity.onItemSelected(1, -1, bundle,null);
    }
    
    public void downloadDicTask(int index) {
        new BaseTask<List<Dictionary>>(getActivity()) {
            
            @Override
            protected List<Dictionary> doInBackground(String... params) {
                
                int index = Integer.parseInt(params[0]);
                selectedIndex = index;
                try {
                    CRMManager manager = ((RoilandCRMApplication)getActivity().getApplication()).getCRMManager();
                    switch (selectedIndex) {
                    case 0:
                        return manager.getDictionariesByType(GlobalConstant.DictionaryKeyConstant.BRAND_DIC_KEY);
                    case 1:
                        if (adapter.getBrandDic().getDicKey() == null) return null;
                        return manager.getRelativeDictionaries(GlobalConstant.DictionaryKeyConstant.MODEL_KEY, adapter.getBrandDic().getDicKey());
                    case 2:
                        return manager.getDictionariesByType(GlobalConstant.DictionaryKeyConstant.FLOW_STATUS_DIC_KEY);
                    }
                } catch (ResponseException e) {
                    responseException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Dictionary> result) {
                if (responseException != null) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (result != null) {
                        downloadedResult = result;
                        String[] valueList = new String[result.size()];
                        for (int i = 0; i < result.size(); i ++) {
                            valueList[i] = result.get(i).getDicValue();
                        }
                        
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(adapter.getCondition(selectedIndex));
        
                        builder.setItems(valueList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Dictionary dic = downloadedResult.get(item);
                                switch (selectedIndex) {
                                case 0:
                                    adapter.setBrandDic(dic);
                                    if(brandDic==null){
                                        brandDic = dic;
                                    }
                                    if(!dic.getDicValue().equals(brandDic.getDicValue())){
                                        Dictionary dicty = new Dictionary();
                                        adapter.setModelDic(dicty);
//                                        adapter.setFollowStatus(dicty);
                                        adapter.notifyDataSetChanged();
                                        brandDic = dic;
                                    }
                                    break;
                                case 1:
                                    adapter.setModelDic(dic);
                                    if(modelDic==null){
                                        modelDic = dic;
                                    }
                                    if(!dic.getDicValue().equals(modelDic.getDicValue())){
                                        adapter.notifyDataSetChanged();
                                        modelDic = dic;
                                    }
                                    break;
                                case 2:
                                    adapter.setFollowStatus(dic);
                                    if(followStatus == null){
                                        followStatus = dic;
                                    }
                                    if(!dic.getDicValue().equals(followStatus.getDicValue())){
                                        adapter.notifyDataSetChanged();
                                        followStatus = dic;
                                    }
                                    break;                            
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder.create().show();
                    }
                }
                super.onPostExecute(result);
            }
            
        }.execute(String.valueOf(index));
    }

    /**
     * 
     * <pre>
     * 销售线索高级搜索页面Adapter
     * </pre>
     * @extends BaseAdapter
     * @author liuyu
     * @version $Id: SmOppoSearchFragment.java, v 0.1 2013-5-22 下午12:01:53 liuyu Exp $
     */
    public class SmOppoSearchListAdapter extends BaseAdapter{
        Dictionary brandDic = null; 
        Dictionary modelDic = null;
        Dictionary followStatus= null;
        Dictionary employee = null;
        String sortWay = null;
        String startTime = null;
        String endTime = null;
        List<View> viewList;
        Context context;
        String []condition = {getString(R.string.brand_2),getString(R.string.model),getString(R.string.flowStatus),getString(R.string.under_sc),getString(R.string.sort_way),getString(R.string.start_time),getString(R.string.end_time)};
        
        /**
         * 构造方法
         * @param context 页面所属的Activity
         */
        public SmOppoSearchListAdapter(Context context){
            this.context = context;
            try {
                viewList = new ArrayList<View>();
                for (int i = 0; i < condition.length; i ++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.sm_oppo_search_item, null);
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
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                case 0:
                    fillView(viewList.get(position), condition[0], brandDic);
                    break;
                case 1:
                    fillView(viewList.get(position), condition[1], modelDic);
                    break;
                case 2:
                    fillView(viewList.get(position), condition[2], followStatus);
                    break;
                case 3:
                    fillView(viewList.get(position), condition[3], employee);
                    break;
                case 4:
                    fillView(viewList.get(position), condition[4], sortWay);
                    break;
                case 5:
                    fillView(viewList.get(position), condition[5], startTime);
                    break;
                case 6:
                    fillView(viewList.get(position), condition[6], endTime);
                    break;
                }
                return viewList.get(position);
        }
        /**
         * 
         * <pre>
         * 把信息添加到相应的位置
         * </pre>
         * 调用getView()方法时调用此方法
         * @param view listView中的每个item的视图
         * @param condition 标题显示内容
         * @param sortWay2 排序方式
         */
        private void fillView(View view, String condition2, String sortWay2) {
            ((TextView) view.findViewById(R.id.txt_title)).setText(condition2);
            if (sortWay2 != null) {
                ((TextView) view.findViewById(R.id.txt_content)).setText(sortWay2);
            } else {
                ((TextView) view.findViewById(R.id.txt_content)).setText("");
            }
        }
        /**
         * 
         * <pre>
         * 把信息添加到相应的位置
         * </pre>
         * 调用getView()方法时调用此方法
         * @param view listView中的每个item的视图
         * @param condition 标题显示内容
         * @param dic 数据字典
         */
        public void fillView(View view, String condition, Dictionary dic) {
            ((TextView) view.findViewById(R.id.txt_title)).setText(condition);
            if (dic != null) {
                ((TextView) view.findViewById(R.id.txt_content)).setText(dic.getDicValue());
            } else {
                ((TextView) view.findViewById(R.id.txt_content)).setText("");
            }
        }
        
        /**
         * 
         * <pre>
         * 清空数据
         * </pre>
         *
         */
        public void clearData(){
            Dictionary dic = new Dictionary();
            this.setBrandDic(dic);
            this.setModelDic(dic);
            this.setFollowStatus(dic);
            this.setEmployee(dic);
            sortWay = "";
            startTime = "";
            endTime = "";
            this.notifyDataSetChanged();
        }
        
        
        public void setModelDic(Dictionary dicty) {
            this.modelDic = dicty;
            notifyDataSetChanged();
        }
        public Dictionary getModelDic(){
            return modelDic;
        }
        public void setBrandDic(Dictionary dic) {
            this.brandDic = dic;
            notifyDataSetChanged();
        }
        
        public Dictionary getBrandDic(){
            return brandDic;
        }
        public void setFollowStatus (Dictionary dic){
            this.followStatus = dic;
            notifyDataSetChanged();
        }
        public Dictionary getFollowStatus(){
            return followStatus;
        }
        public void setSortWay (String valueList){
            this.sortWay = valueList;
            notifyDataSetChanged();
        }
        public String getSortWay(){
            return sortWay;
        }
        public void setStartTime (StringBuilder data){
            this.startTime = data.toString();
            notifyDataSetChanged();
        }
        public String getStartTime(){
            return startTime != null ? startTime.toString():null;
        }
        public void setEndTime (StringBuilder data){
            this.endTime = data.toString();
            notifyDataSetChanged();
        }
        public String getEndTime(){
            return endTime != null ? endTime.toString():null;
        }
        public Dictionary getEmployee() {
            return employee;
        }
        public void setEmployee(Dictionary employee) {
            this.employee = employee;
            notifyDataSetChanged();
        }
    }

    public void destroy_adapter() {
        adapter = null;
    }
    
}
