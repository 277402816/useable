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
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售过程管理高级搜索Fragment
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmFollowPlanSearchFragment.java, v 0.1 2013-5-25 下午4:33:20 shuang.gao Exp $
 */
public class SmFollowPlanSearchFragment extends SherlockFragment {

    public List<Dictionary>               downloadedResult;
    protected BaseActivity                activity;
    private SmFollowPlanSearchListAdapter adapter;
    private boolean chooseEnd = false;
    private int                           selectedIndex;              //选定的搜索项的索引
    private boolean                       isStartTime         = false; //开始时间
    private boolean                       isEndTime           = true; //结束时间
    private boolean                       isOrderByCreateDate = false; //true 按建立时间排序 false 按执行时间排序
    private Dictionary                    activityType;               //活动类型
    private Dictionary                    followStatus;               //执行状态
    StringBuilder                         startTime           = null; //开始时间
    StringBuilder                         endTime             = null; //结束时间

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

    /**
     * 
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sm_oppo_search_info, container, false);
        final String[] valueList1 = { getString(R.string.buildup_date_order),
                getString(R.string.execute_time_order) };
        final String[] valueList2 = { getString(R.string.already_comment),
                getString(R.string.no_comment) };
        if (adapter == null) {
            adapter = new SmFollowPlanSearchListAdapter(getActivity());
        }
        adapter.clearData();
        ListView listView = (ListView) view.findViewById(R.id.oppo_info_list);
        ImageButton searchStart = (ImageButton) view.findViewById(R.id.oppo_search_start_button);
        //设置开始查询事件监听
        searchStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TracePlan t = new TracePlan();
                TracePlan.AdvancedSearch advancedSearch = t.new AdvancedSearch();
                advancedSearch.setExecuteStatus(adapter.getExecuteStatus().getDicKey());
                if (adapter.getLeaderComment() != null) {
                    if (adapter.getLeaderComment().equals(getString(R.string.already_comment))) {
                        //已批示
                        advancedSearch.setLeaderComment("1");
                    }
                    if (adapter.getLeaderComment().equals(getString(R.string.no_comment))) {
                        //未批示
                        advancedSearch.setLeaderComment("2");
                    }
                }
                if (adapter.getSortWay() != null) {
                    if (adapter.getSortWay().equals(getString(R.string.buildup_date_order))) {
                        //按建立时间正序排列
                        isOrderByCreateDate = true;
                        advancedSearch.setSort("1");
                    }
                    if (adapter.getSortWay().equals(getString(R.string.execute_time_order))) {
                        //按执行时间正序排列
                        isOrderByCreateDate = false;
                        advancedSearch.setSort("2");
                    }
                }
                advancedSearch.setStartDate(DateFormatUtils.parseDateToLong(adapter.getStartTime()));
                if(chooseEnd){
                    advancedSearch.setEndDate(DateFormatUtils.parseDateToLong(adapter.getEndTime())+24*60*60*1000);
                }else{
                    advancedSearch.setEndDate(DateFormatUtils.parseDateToLong(adapter.getEndTime()));
                }
                advancedSearch.setActionType(adapter.getActivityType().getDicKey());
                advancedSearch.setOwnerId(adapter.getOwnerId().getDicKey());
                //设置搜索条件
                t.setAdvancedSearch(advancedSearch);
                //搜索操作
                doSearch(t);
            }
        });
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == 3 || arg2 == 4) {
                    getTime(arg2);
                } else if (arg2 == 2) {
                    getOrderType(arg2, valueList1);
                } else if (arg2 == 1) {
                    getOrderType(arg2, valueList2);
                } else if (arg2 == 6) {
                    downloadOwnerID(arg2);
                } else {
                    downloadDicTask(arg2);
                }
            }
        });

        return view;
    }

    /**
     * 创建菜单
     * @param menu 菜单项
     * @param inflater
     * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem itemCancle = menu.findItem(R.id.cancel);
        itemCancle.setVisible(true);
    }

    /**
     * 菜单选择项
     * @param item 菜单项
     * @return
     * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cancel:
                activity.onItemSelected(2, -1, null);
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
     * 从数据字典获取销售顾问ID
     * </pre>
     *
     * @param position 所在条目位置
     */
    public void downloadOwnerID(int position) {

        new BaseTask<List<Dictionary>>(activity) {
            List<Dictionary> downloadEmployee;

            @Override
            protected List<Dictionary> doInBackground(String... params) {
                selectedIndex = Integer.parseInt(params[0]);
                CRMManager manager = ((RoilandCRMApplication) getActivity().getApplication())
                    .getCRMManager();
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
                if (responseException != null) {
                    Toast.makeText(activity, responseException.getMessage(), Toast.LENGTH_LONG)
                        .show();
                }
                if (result != null) {
                    downloadEmployee = result;
                    String[] valueList = new String[result.size()];
                    for (int i = 0; i < result.size(); i++) {
                        valueList[i] = result.get(i).getDicValue();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(adapter.getCondition(selectedIndex));
                    builder.setItems(valueList, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dictionary employee = downloadEmployee.get(which);
                            adapter.setOwnerId(employee);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.create().show();
                }
            }

        }.execute(String.valueOf(position));
    }

    /**
     * 
     * <pre>
     * 从数据字典获取执行状态和活动类型
     * </pre>
     *
     * @param index 所在条目位置
     */
    public void downloadDicTask(int index) {
        new BaseTask<List<Dictionary>>(getActivity()) {

            @Override
            protected List<Dictionary> doInBackground(String... params) {

                int index = Integer.parseInt(params[0]);
                selectedIndex = index;
                try {
                    CRMManager manager = ((RoilandCRMApplication) getActivity().getApplication())
                        .getCRMManager();
                    switch (selectedIndex) {
                        case 0:
                            return manager
                                .getDictionariesByType(GlobalConstant.DictionaryKeyConstant.EXECUTE_STATUS_DIC_KEY);
                        case 5:
                            return manager
                                .getDictionariesByType(GlobalConstant.DictionaryKeyConstant.ACTIVITY_TYPE_DIC_KEY);

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
                        Toast.makeText(getActivity(), responseException.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (result != null) {
                        downloadedResult = result;
                        String[] valueList = new String[result.size()];
                        for (int i = 0; i < result.size(); i++) {
                            valueList[i] = result.get(i).getDicValue();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(adapter.getCondition(selectedIndex));

                        builder.setItems(valueList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                Dictionary dic = downloadedResult.get(item);
                                switch (selectedIndex) {
                                    case 0:
                                        adapter.setExecuteStatus(dic);
                                        if (followStatus == null) {
                                            followStatus = dic;
                                        }
                                        if (!dic.getDicValue().equals(followStatus.getDicValue())) {
                                            adapter.notifyDataSetChanged();
                                            followStatus = dic;
                                        }
                                        break;
                                    case 5:
                                        adapter.setActivityType(dic);
                                        if (activityType == null) {
                                            activityType = dic;
                                        }
                                        if (!dic.getDicValue().equals(activityType.getDicValue())) {
                                            adapter.notifyDataSetChanged();
                                            activityType = dic;
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
     * 跟踪计划管理高级搜索适配
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: SmFollowPlanSearchFragment.java, v 0.1 2013-5-30 下午4:22:20 shuang.gao Exp $
     */
    public class SmFollowPlanSearchListAdapter extends BaseAdapter {
        Dictionary executeStatus = null;                               //执行状态
        String     sortWay       = null;                               //排序方式
        Dictionary activityType;                                       //活动类型
        String     leaderComment = null;                               //是否批示
        Dictionary ownerId       = null;                               //销售顾问ID
        String     startTime     = null;                               //开始时间
        String     endTime       = null;                               //结束时间
        List<View> viewList;
        Context    context;
        String[]   condition     = { getString(R.string.executestatus),
                                         getString(R.string.iscomment),
                                         getString(R.string.sort_way),
                                         getString(R.string.start_time),
                                         getString(R.string.end_time),
                                         getString(R.string.activitytype),
                                         getString(R.string.ownerId) };

        public SmFollowPlanSearchListAdapter(Context context) {
            this.context = context;
            try {
                viewList = new ArrayList<View>();
                for (int i = 0; i < condition.length; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.sm_oppo_search_item,
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
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * 列表视图
         * @param position 当前位置
         * @param convertView
         * @param parent
         * @return
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                case 0:
                    fillView(viewList.get(position), condition[0], executeStatus);
                    break;
                case 1:
                    fillView(viewList.get(position), condition[1], leaderComment);
                    break;
                case 2:
                    fillView(viewList.get(position), condition[2], sortWay);
                    break;
                case 3:
                    fillView(viewList.get(position), condition[3], startTime);
                    break;
                case 4:
                    fillView(viewList.get(position), condition[4], endTime);
                    break;
                case 5:
                    fillView(viewList.get(position), condition[5], activityType);
                    break;
                case 6:
                    fillView(viewList.get(position), condition[6], ownerId);
                    break;
            }
            return viewList.get(position);
        }

        /**
         * 
         * <pre>
         * 将内容显示在列表上
         * </pre>
         *
         * @param view
         * @param condition 显示题目
         * @param parm 要显示内容
         */
        private void fillView(View view, String condition, String parm) {
            ((TextView) view.findViewById(R.id.txt_title)).setText(condition);
            if (parm != null) {
                ((TextView) view.findViewById(R.id.txt_content)).setText(parm);
            } else {
                ((TextView) view.findViewById(R.id.txt_content)).setText("");
            }
        }

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
         * 清空数据用
         * </pre>
         *
         */
        public void clearData() {
            Dictionary dic = new Dictionary();
            this.setExecuteStatus(dic);
            this.setLeaderComment("");
            this.setActivityType(dic);
            this.setSortWay("");
            this.setStartTime(new StringBuilder());
            this.setEndTime(new StringBuilder());
            this.setOwnerId(dic);
            this.notifyDataSetChanged();
        }

        public Dictionary getExecuteStatus() {
            return executeStatus;
        }

        public void setExecuteStatus(Dictionary executeStatus) {
            this.executeStatus = executeStatus;
        }

        public String getSortWay() {
            return sortWay;
        }

        public void setSortWay(String sortWay) {
            this.sortWay = sortWay;
        }

        public String getLeaderComment() {
            return leaderComment;
        }

        public void setLeaderComment(String leaderComment) {
            this.leaderComment = leaderComment;
        }

        public Dictionary getActivityType() {
            return activityType;
        }

        public void setActivityType(Dictionary activityType) {
            this.activityType = activityType;
        }

        public String getStartTime() {
            return startTime != null ? startTime.toString() : null;
        }

        public void setStartTime(StringBuilder startTime) {
            this.startTime = startTime.toString();
        }

        public String getEndTime() {
            return endTime != null ? endTime.toString() : null;
        }

        public void setEndTime(StringBuilder endTime) {
            this.endTime = endTime.toString();
        }

        public Dictionary getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(Dictionary employee) {
            this.ownerId = employee;
        }

    }

    /**
     * 
     * <pre>
     * 点击排序方式
     * </pre>
     * @param valueList 列表显示数据
     *
     */
    public void getOrderType(final int arg2, final String[] valueList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(adapter.getCondition(arg2));

        builder.setItems(valueList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (arg2) {
                    case 1:
                        adapter.setLeaderComment(valueList[item]);
                        break;
                    case 2:

                        if (!adapter.getSortWay().equals(valueList[item])) {
                            adapter.setEndTime(new StringBuilder(""));
                        }
                        adapter.setSortWay(valueList[item]);
                        break;
                    default:
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    /**
     * 
     * <pre>
     * 点击开始时间或是结束时间弹出框
     * </pre>
     *
     * @param arg2 点击的开始时间或是结束时间
     */
    public void getTime(int arg2) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (arg2 == 3) {
            isStartTime = true;
            isEndTime = false;
        } else {
            isStartTime = false;
            isEndTime = true;
        }
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), mDateSetListener, year,
            month, day);
        //        dialog.getDatePicker().setMinDate(DateFormatUtils.parseDateToLong("1970-01-01"));
        dialog.show();
    }

    /**
     * 开始时间和结束时间的点击事件
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker arg0,int arg1,int arg2,int arg3) {
                            Log.i("DatePickerDialog:",String.valueOf(arg1)+ String.valueOf(arg2 + 1)+ String.valueOf(arg3));
                            StringBuilder data = new StringBuilder().append(arg1).append("-").append(SmOppoSearchFragment.formateTime(arg2 + 1))
                                .append("-")
                                .append(
                                    SmOppoSearchFragment
                                        .formateTime(arg3));
                            if (DateFormatUtils
                                .parseDateToLong(data .toString()) <= DateFormatUtils .parseDateToLong("1970-01-01")) {
                                                                            //                                                                          Toast.makeText(getActivity(), "时间不能小于1970-01-01", Toast.LENGTH_LONG).show();
                                             return;
                                        }
                                        if (isStartTime) {
                                            try {
                                                if (!StringUtils
                                                    .isEmpty(adapter
                                                        .getEndTime())
                                                    && DataVerify
                                                        .dateCompare1(
                                                            data.toString(),
                                                            adapter
                                                                .getEndTime())) {
                                                    Toast
                                                        .makeText(
                                                            activity,
                                                            "开始时间大于结束时间",
                                                            Toast.LENGTH_LONG)
                                                        .show();
                                                    return;
                                                }
                                            } catch (Exception e) {
                                                Toast
                                                    .makeText(
                                                        activity,
                                                        e.getMessage(),
                                                        Toast.LENGTH_LONG)
                                                    .show();
                                            }
                                            adapter
                                                .setStartTime(data);
                                        }
                                        if (isEndTime) {
                                            try {

                                                if (!StringUtils
                                                    .isEmpty(adapter
                                                        .getStartTime())
                                                    && DataVerify
                                                        .dateCompare1(
                                                            adapter
                                                                .getStartTime(),
                                                            data.toString())) {
                                                    Toast
                                                        .makeText(
                                                            activity,
                                                            "开始时间大于结束时间",
                                                            Toast.LENGTH_LONG)
                                                        .show();
                                                    return;
                                                }
                                                if (DataVerify.dateCompare1(data.toString(),DataVerify.systemDate())
                                                    && !StringUtils.isEmpty(adapter.getSortWay()) && 
                                                    !adapter.getSortWay().equals(getString(R.string.execute_time_order))) {
                                                    Toast.makeText(activity,"结束时间不能大于今日",Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                            } catch (Exception e) {
                                                Toast
                                                    .makeText(
                                                        activity,
                                                        e.getMessage(),
                                                        Toast.LENGTH_LONG)
                                                    .show();
                                            }
                                            adapter
                                                .setEndTime(data);
                                            chooseEnd = true;
                                        }
                                        adapter
                                            .notifyDataSetChanged();
                                    }
                                };

    /**
     * 
     * <pre>
     * 开始查询任务
     * </pre>
     *
     */
    private void doSearch(TracePlan t) {
        chooseEnd = false;
        Bundle bundle = new Bundle();
        bundle.putParcelable("advancedSearch", t);
        bundle.putBoolean("isOrderByCreateDate", isOrderByCreateDate);
        bundle.putInt("currentPage", 0);
        bundle.putInt("begin", 0);
        activity.onItemSelected(2, -1, bundle, null);
    }
}
