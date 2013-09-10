package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.model.TracePlan.AdvancedSearch;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.SmFollowPlanListAdapter;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.PromissionController;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售过程管理列表
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmFollowPlanListFragment.java, v 0.1 2013-5-20 下午4:00:53 shuang.gao Exp $
 */
public class SmFollowPlanListFragment extends BaseRefreshListFragment {
    private final static String        tag                = Log
                                                              .getTag(SmFollowPlanListFragment.class); //Log标志
    String                             searchText         = "";                                        //搜索关键字
    int                                currentPage        = 0;                                         //当前页
    String                             SEARCH_COLUMNS     = "custName,custMobile";                     //查询字段
    TracePlan                          advancedTracePlan  = null;                                      //高级查询跟踪计划信息
    List<TracePlan>                    tracePlanList      = null;                                      //跟踪计划列表集合
    List<String>                       sectionKeyList     = new ArrayList<String>();                   //排序用集合
    Map<String, List<TracePlan>>       tracePlanMap       = new HashMap<String, List<TracePlan>>();
    //判断调用获取列表的接口
    public static String               loadFlag;
    private boolean                    loadingbarviewable = false;                                     //加载中
    private boolean                    isNew = false;                                                  //最新标识
    private boolean                    isOrderByCreateDate = false;                                    //true 按建立时间排序
    private SmFollowPlanListAdapter    listAdapter;                                                    //跟踪计划列表适配器
    private SmFollowPlanSearchFragment followPlanSearchFragment;                                        //高级搜索视图
    private int beginnum = 0;                                                                           //开始条目
   
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(tag, "==================onActivityResult== resultCode==" + String.valueOf(resultCode) + "  requestCode==" + String.valueOf(requestCode));
        if (requestCode == 131075 || requestCode == 3) {
              if (resultCode == 4) {
                  TracePlan tracePlan = data.getParcelableExtra("tracePlan");
                  if(tracePlan != null){
                      editTracePlanInfo(tracePlan);
                  }
              }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setHasOptionsMenu(true);
        if (listAdapter == null) {
            listAdapter = new SmFollowPlanListAdapter(this.getActivity());
            setListAdapter(listAdapter);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //获取从高级搜索中的传入的条件参数
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             advancedTracePlan = bundle.getParcelable("advancedSearch");
             isOrderByCreateDate = bundle.getBoolean("isOrderByCreateDate");
             currentPage = bundle.getInt("currentPage");
             beginnum = bundle.getInt("begin");
            defaultSearch(advancedTracePlan.getAdvancedSearch());
        } else {
            //初期化检索
            defaultSearch();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    };

    public void onResume() {
        super.onResume();
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
        Log.e(tag, "onCreateOptionsMenu()");
                MenuItem item1 = menu.findItem(R.id.search);
                MenuItem item2 = menu.findItem(R.id.advanced_search);
                item1.setVisible(PromissionController.hasPermission(item1.getTitle().toString()));
                item2.setVisible(PromissionController.hasPermission(item2.getTitle().toString()));
    }

    /**
     * 选中菜单事件
     * @param item 点击哪项菜单按钮
     * @return
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(tag, "onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.search:
                setSearchMode(true);
                break;
            case R.id.advanced_search:
                mSearchMode1 = true;
                activity.advanceSearch = true;
                if (followPlanSearchFragment == null)
                    followPlanSearchFragment = new SmFollowPlanSearchFragment();
                mItemAdvancedSearch.setVisible(false);
                mItemAdvancedSearchCancel.setVisible(true);
                activity.switchContent(followPlanSearchFragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * item单击事件
     * @param l ListView
     * @param v 视图
     * @param position 点击条目位置
     * @param id 点击条目id
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (loadingbarviewable && position == getListView().getLastVisiblePosition() + 1)
            return;
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(activity, SmFollowPlanActivity.class);
        Bundle bundle = new Bundle();
        TracePlan tracePlan = (TracePlan) listAdapter.getItem(position);
        bundle.putParcelable("tracePlan", tracePlan);
        bundle.putParcelable("customerinfo", tracePlan.getCustomer());
        intent.putExtras(bundle);
        intent.putExtra("EditMode", false);
        startActivityForResult(intent, 3);
    }

    /**
     * 
     * <pre>
     * 将新编辑的tracePlan放到第一条
     * </pre>
     *
     * @param tracePlan 新编辑的tracePlan
     */
    public void editTracePlanInfo(TracePlan tracePlan) {
        List<TracePlan> tt = new ArrayList<TracePlan>();
        tt.add(tracePlan);
        for (int i = 0; i < tracePlanList.size(); i++) {
            if(!tracePlan.getActivityID().equals(tracePlanList.get(i).getActivityID()))
                tt.add(tracePlanList.get(i));
        }
        setCurrentItem(tracePlan,tt);
        Log.i(tag, "addProjectInfo:" + String.valueOf(tracePlanList.size()));
    }

    /**
     * 
     * <pre>
     *  用编辑后的跟踪计划替换原来的跟踪计划
     * </pre>
     *
     * @param tracePlan 编辑之后的跟踪计划
     */
    public void setCurrentItem(TracePlan tracePlan, List<TracePlan> planList) {
        String editCustId = tracePlan.getCustomer().getCustomerID();
        for (TracePlan tp : tracePlanList) {
            if (StringUtils.isEmpty(tp.getCustomer().getCustomerID()) ||  !tp.getCustomer().getCustomerID().equals(editCustId))
                continue;
            tp = tracePlan;

            break;
        }
        displayList(true, planList, true);
    }
    

    /**
     * 
     * <pre>
     * 查询跟踪计划方法
     * </pre>
     *
     * @param searchText 查询关键字
     * @param start 开始位置
     * @param rowCount 每次查询的多少行数据
     * @param displayLoadingDown 判断是否正在加载
     * @param advancedSearch 
     */
    public void search(String searchText, int start, int rowCount, boolean displayLoadingDown, final AdvancedSearch advancedSearch) {
        
        if (isDownloadThreadRunning)
            return;
        isDownloadThreadRunning = true;
        this.searchText = searchText;
       // currentPage = start / DEFAULT_PAGE_SIZE;

        /**
         * 获取跟踪计划异步方法
         */
        new BaseTask<List<TracePlan>>(getActivity(), "", true, true, displayLoadingDown) {
            @Override
            protected List<TracePlan> doInBackground(String... params) {
                CRMManager manager = mApplication.getCRMManager();
                if (manager == null)
                    return null;
                try {
                    if("0".equals(loadFlag)){
                        loadFlag = "";
                        return manager.getExpiredActionPlanList(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        
                    }else if("1".equals(loadFlag)){
                        loadFlag = "";
                        return manager.getTodayActionPlanList(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                    }else{
                        return manager.getTracePlanList(null, null, params[0], SEARCH_COLUMNS,
                            Integer.parseInt(params[1]), Integer.parseInt(params[2]), advancedSearch);
                    }
                } catch (ResponseException e) {
                    responseException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<TracePlan> result) {
                super.onPostExecute(result);
                completeRefresh();
                if (responseException != null) {
                    //当时间超过30分钟则跳转到首页
                    if (responseException.getStatusCode() == StatusCodeConstant.UNAUTHORIZED) {
                        DialogUtils.overMinuteDo(getActivity(),responseException);
                    } else {
                        Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show(); 
                    }
                } else if(result != null){
                    
                    if (currentPage == 0 && tracePlanList == null) {
                        //重新赋值
                        tracePlanList = result;
                        currentPage++;
                    } else {
                        if (tracePlanList == null) {
                            tracePlanList = new ArrayList<TracePlan>();
                        }
                        //追加
                        tracePlanList.addAll(result);
                        currentPage++;
                    }
                }else if(result == null || result.size() == 0){
                    return;
                }

                displayList(true, tracePlanList, false);
                isDownloadThreadRunning = false;
            }

            @Override
            protected void onCancelled() {
                isDownloadThreadRunning = false;
                super.onCancelled();
            }
        }.execute(searchText, String.valueOf(start), String.valueOf(rowCount));
    }
    /**
     * 显示从服务器端获取的数据
     * @param init 是否初次进入
     * @param planList 跟踪计划列表
     * @param b 是否最新编辑
     */
    public void displayList(boolean init, List<TracePlan> planList, boolean isNew) {
        String key;
        this.isNew = isNew;
        if(planList == null)
            return;
        if(planList.size() == 0){
            listAdapter.removeAll();
            listAdapter.notifyDataSetChanged();
            return;
        }
        if (init) {
            sectionKeyList.clear();
            tracePlanMap.clear();
            listAdapter.removeAll();
        }

       
        for (int i = 0; i < planList.size(); i++) {
            TracePlan tracePlan = planList.get(i);
            if(isOrderByCreateDate){
                 key = DateFormatUtils.formatDate(tracePlan.getCreateDate());
            }else{
                 key = DateFormatUtils.formatDate(tracePlan.getExecuteTime());
            }
            
            if(compareDate(DataVerify.systemDate(),key)){
                key = "";
            }
            List<TracePlan> subList = tracePlanMap.get(key);
            if (isNew && i == 0) {
                if(planList.size()==1){
                    sectionKeyList.add(key);
                }
                 continue;
            }
            if (subList == null) {
                subList = new ArrayList<TracePlan>();
                tracePlanMap.put(key, subList);
                sectionKeyList.add(key);
            }

            subList.add(tracePlan);//subList为每个日期下的计划列表
        }
        loadingbarviewable = false;
        for (int i = 0; i < sectionKeyList.size(); i++) {//sectionKeyList为时间列表
            if (i == sectionKeyList.size() - 1 && planList.size() % DEFAULT_PAGE_SIZE == 0
                && planList.size() > 10) {
                listAdapter.loadingbarViewable = true;
                loadingbarviewable = true;
            } else {
                listAdapter.loadingbarViewable = false;
            }
            if (isNew) {
                //让最近编辑显示首行
                if (i == 0) {
                    List<TracePlan> list = new ArrayList<TracePlan>();
                    list.add(planList.get(0));
                    if(planList.size()==1){
                        listAdapter.addSection(getString(R.string.nearest_edit), list);
                    }else{
                    listAdapter.addSection(getString(R.string.nearest_edit), list);
                    listAdapter.addSection(sectionKeyList.get(i),
                    tracePlanMap.get(sectionKeyList.get(i)));
                    }
                } else {
                    listAdapter.addSection(sectionKeyList.get(i),
                        tracePlanMap.get(sectionKeyList.get(i)));
                }
            } else {
                listAdapter.addSection(sectionKeyList.get(i),
                    tracePlanMap.get(sectionKeyList.get(i)));
            }
        }
        listAdapter.notifyDataSetChanged();
        //编辑情况跳到第一条数据
        if(isNew){
            setSelection(0);
        }
    }

    private boolean compareDate(String str1, String str2) {

        Long d1 = DateFormatUtils.parseDateToLong(str1);
        Long d2 = DateFormatUtils.parseDateToLong(str2);
        long l = 24 * 60 * 60 * 1000 * 7;
        if (d2 - d1 >= 0 /* || d2 - d1 < 0 */) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 实现上拉刷新
     * @param view
     * @param scrollState
     * @see com.roiland.crm.ui.view.BaseRefreshListFragment#onScrollStateChanged(android.widget.AbsListView, int)
     */
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                int lastposition = getListView().getLastVisiblePosition();
                if(isNew){
                    lastposition = lastposition - 1;
                }
                if(beginnum==0){
                    beginnum = beginnum + DEFAULT_PAGE_SIZE;
                }else{
                    beginnum = beginnum + DEFAULT_PAGE_SIZE;
                }
                if (lastposition - tracePlanMap.keySet().size() == currentPage * DEFAULT_PAGE_SIZE ) {
                    if(advancedTracePlan != null && advancedTracePlan.getAdvancedSearch() != null){
                        search(searchText, beginnum, DEFAULT_PAGE_SIZE, true,advancedTracePlan.getAdvancedSearch());
                    }else{
                        search(searchText, beginnum, DEFAULT_PAGE_SIZE, true,null);
                    }
                }
                break;
        }
    }

    /**
     * 实现跟踪计划搜索功能
     * @param query 搜索关键字
     * @return
     * @see com.roiland.crm.ui.view.BaseRefreshListFragment#onQueryTextSubmit(java.lang.String)
     */
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        if (tracePlanList != null)
            tracePlanList.clear();
        beginnum = 0;
        currentPage = 0;
        listAdapter.loadingbarViewable = false;
        search(query.trim(), beginnum, DEFAULT_PAGE_SIZE, false,null);
        return true;
    }
    public void defaultSearch() {
        defaultSearch(null);
    }
    /**
     * 
     * 主要实现页面加载，调用查询方法进行页面加载
     * @see com.roiland.crm.ui.view.BaseRefreshListFragment#defaultSearch()
     */
    public void defaultSearch(TracePlan.AdvancedSearch advancedSearch) {
        if (listAdapter == null) {
            listAdapter = new SmFollowPlanListAdapter(getActivity());
        } else {
            listAdapter.loadingbarViewable = false;
            listAdapter.removeAll();
        }
        if (tracePlanList != null)
            tracePlanList.clear();
        setListAdapter(listAdapter);
        if(isRefreshing){
            search("", beginnum, DEFAULT_PAGE_SIZE, true,advancedSearch);
        }else{
            search("", beginnum, DEFAULT_PAGE_SIZE, false,advancedSearch);
        }
        
    }

    /**
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onRefresh(android.view.View)
     */
    @Override
    public void onRefresh(View view) {
        super.onRefresh(view);
        //下拉刷新后将高级搜索资源清空
        advancedTracePlan = null;
        beginnum = 0;
        currentPage = 0;
        loadFlag = "";
        if (tracePlanList != null)
            tracePlanList.clear();
        
        search("", beginnum, DEFAULT_PAGE_SIZE, true,null);
    }
    
    @Override
    public void setFlag(String flag) {
        super.setFlag(flag);
        this.loadFlag = flag;
    }
}
