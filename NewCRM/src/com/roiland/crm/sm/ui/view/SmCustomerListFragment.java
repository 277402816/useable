package com.roiland.crm.sm.ui.view;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.SmCustomerListAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.PromissionController;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客戶管理列表Fragment
 * </pre>
 * @extends BaseRefreshListFragment
 * @author liuyu
 * @version $Id: SmCustomerListFragment.java, v 0.1 2013-5-21 上午9:48:00 liuyu Exp $
 */
public class SmCustomerListFragment extends BaseRefreshListFragment {
    //判断调用获取列表的接口
    public static String               loadFlag;
    private final static String   tag                = Log.getTag(SmCustomerListFragment.class); //Log标志
    private SmCustomerSearchFragment smCustomerSearchFragment;                                   //高级搜索视图
    private SmCustomerInfoFragment smCustomerInfoFragment;
    List<Customer>                customerList       = new ArrayList<Customer>();
    int                           currentPage        = 0;
    String                        searchText         = "";
    String                        searchColumns      = "custName,custMobile";
    private String advanceCustOwer = null;
    private String advanceCustStatus = null;
    private String asvanceOwnerID = null;
    private Boolean flag;
    Map<String, List<Customer>>   customerMap        = new HashMap<String, List<Customer>>();
    List<String>                  sectionKeyList     = new ArrayList<String>();
    private boolean               loadingbarviewable = false;
    private SmCustomerListAdapter listAdapter;
	private Bundle bundle;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        //获取从高级搜索中的传入的条件参数
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             advanceCustOwer = bundle.getString("custOwer");
             advanceCustStatus = bundle.getString("custStatus");
             asvanceOwnerID = bundle.getString("ownerId");
             flag = bundle.getBoolean("doSave");
            defaultSearch(advanceCustOwer,advanceCustStatus,asvanceOwnerID);
        } else {
            //初期化检索
            defaultSearch();
        }
	}
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	              if (resultCode == 6) {
	            	 Bundle bundle = data.getExtras();
	            	 flag = bundle.getBoolean("doSave");
	        }
	    }
    @Override
	public void onDetach() {
		super.onDetach();
	}
    
    @Override
	public void onResume() {
		super.onResume();
     		if(flag!=null && flag){
    			defaultSearch();
		 }

	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		super.setHasOptionsMenu(true);
		if (listAdapter == null) {
			listAdapter = new SmCustomerListAdapter(this.getActivity());
			setListAdapter(listAdapter);
		}
		//设为单选，允许列表项切换checked/unchecked状态
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setOnScrollListener(this);

	}
	/**
	 * onListItemClick 单击列表条目时的单击事件
	 * @param l listView 显示信息的listview
	 * @param v listView中的item
	 * @param position 条目位置
	 * @param id 条目id
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//如果正在加载中 跳出改方法
		if (loadingbarviewable && position == getListView().getLastVisiblePosition() + 1)
			return;
		super.onListItemClick(l, v, position, id);
		//将当前条目的客户信息传给客户详细信息列表
		Intent intent = new Intent(activity, SmCustomerInfoActivity.class);
		intent.putExtra("CUSTOMERID", ((Customer)listAdapter.getItem(position)).getCustomerID());
		startActivityForResult(intent, 2);
	}
	
	
	/**
	 * 
	 * <pre>
	 * search 异步操作 查询客户列表信息
	 * </pre>
	 *
	 * @param searchText 搜索关键字
     * @param start 搜索开始索引
     * @param rowCount 获取的记录条数
     * @param displayLoadingDown 是否执行刷新
	 * @param asvanceOwnerID 高级搜索客户归属
	 * @param advanceCustStatus 高级搜索销售顾问ID
	 * @param advanceCustOwer 高级搜索客户状态
	 */
	public void search(String searchText, int start, int rowCount, boolean displayLoadingDown, final String advanceCustOwer, final String advanceCustStatus, final String asvanceOwnerID) {
		//正在刷新 跳出该方法
		if (isDownloadThreadRunning) return;
		isDownloadThreadRunning = true;
		new BaseTask<List<Customer>>(activity, "", true, true, displayLoadingDown) {

			@Override
			protected List<Customer> doInBackground(String... params) {
				CRMManager manager = mApplication.getCRMManager();
				List<Customer> downloadedList = null;
				try {
					String searchText = params[0];
					int start = Integer.parseInt(params[1]);
					int rowCount = Integer.parseInt(params[2]);
					if("0".equals(loadFlag)){
					    //获取没有跟踪计划的客户列表信息
					    downloadedList = manager.noActionPlanProjectCount(searchText, searchColumns, start, rowCount);
					}else{
		                //获取客户列表信息
	                    downloadedList = manager.getCustomerList(searchText, searchColumns, start, rowCount, null,advanceCustOwer,advanceCustStatus,asvanceOwnerID);
					}
				} catch (ResponseException e) {
					responseException = e;
				} catch (Exception e) {
					e.printStackTrace();
				}
				//获取信息失败 downloadedList为空 画面显示空的列表
				if (downloadedList == null) {
					downloadedList = new ArrayList<Customer>();
				}
				return downloadedList;
			}
			
			@Override
			protected void onPostExecute(List<Customer> result) {
				super.onPostExecute(result);
				//下拉刷新
				completeRefresh();
				//捕获异常 提示错误信息
				if (responseException != null) {
                    //当时间超过30分钟则跳转到首页
                    if (responseException.getStatusCode() == StatusCodeConstant.UNAUTHORIZED) {
                        DialogUtils.overMinuteDo(getActivity(),responseException);
                    } else {
                        Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show(); 
                    }
					displayList();
				} else {
					customerList.addAll(result);
					displayList();
				}
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
	 * 
	 * <pre>
	 * displayList 将列表按首位字母排序显示出来
	 * </pre>
	 * 
	 */
	public void displayList() {
		
		sectionKeyList.clear();
		customerMap.clear();
		if (customerList == null)
			return;
		for (int i = 0; i < customerList.size(); i ++) {
			Customer customer = customerList.get(i);
			String key = "";
			if (customer.getCustName() != null && customer.getCustName().length() != 0 ) {
			    key = DateFormatUtils.formatDate(customer.getCreateDate());
			}
			List<Customer> subList = customerMap.get(key);
			if (subList == null) {
				subList = new ArrayList<Customer>();
				customerMap.put(key, subList);
				sectionKeyList.add(key);
			}
			subList.add(customer);
		}
		listAdapter.removeAll();
		loadingbarviewable = false;
		Collections.sort(sectionKeyList, new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				return rhs.compareTo(lhs);
			}
		});

		for (int i = 0; i < sectionKeyList.size(); i ++) {
			if (i == sectionKeyList.size() - 1 && customerList.size() % DEFAULT_PAGE_SIZE == 0 && customerList.size() > 10) {
				listAdapter.loadingbarViewable = true;
				loadingbarviewable = true;
			} else
				listAdapter.loadingbarViewable = false;
			listAdapter.addSection(sectionKeyList.get(i), customerMap.get(sectionKeyList.get(i)));
		}
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * 每页显示20条后滑动刷新 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。   
	 * @param arg0
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 * @param totalItemCount
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (customerMap != null && getListView().getLastVisiblePosition() - customerMap.keySet().size() == currentPage * DEFAULT_PAGE_SIZE) {
			currentPage ++;
			if((advanceCustOwer != null) || (advanceCustStatus != null) || (asvanceOwnerID != null)){
		         if (currentPage == 1) {
		                search(searchText, (currentPage - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, false,advanceCustOwer,advanceCustStatus,asvanceOwnerID);
		            } else {
		                search(searchText, (currentPage - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, true,advanceCustOwer,advanceCustStatus,asvanceOwnerID);
		            }   
			}else{
		         if (currentPage == 1) {
		                search(searchText, (currentPage - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, false,null,null,null);
		            } else {
		                search(searchText, (currentPage - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, true,null,null,null);
		            }
			}
		}
	}
	
	/**
	 * 搜索时根据关键字查询
	 * @param query 查询的关键字
	 * @return
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onQueryTextSubmit(java.lang.String)
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		if(StringUtils.isEmpty(query)){
		    listAdapter.removeAll();
		    listAdapter.notifyDataSetChanged();
		    listAdapter.loadingbarViewable = false;
		}else{
		    searchText = query.trim();
	        customerList.clear();
	        currentPage = 0;
	        listAdapter.loadingbarViewable = false;
	        search(searchText, currentPage ++ * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, false,null,null,null);
		}
		return false;
	}
	
	/**
	 * defaultSearch 页面加载查询
	 * 每次刷新都调用此方法
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#defaultSearch()
	 */
	@Override
	public void defaultSearch() {
	    defaultSearch(null,null,null);
	}

    
    private void defaultSearch(String advanceCustOwer, String advanceCustStatus,
                               String asvanceOwnerID) {
        searchText = "";
        customerList.clear();
        currentPage = 0;
        if (listAdapter != null) {
            listAdapter.loadingbarViewable = false;
            listAdapter.removeAll();
            setListAdapter(listAdapter);
        }
        if(isRefreshing){
            search(searchText, currentPage ++ * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, true,advanceCustOwer,advanceCustStatus,asvanceOwnerID);   
        }else{
            search(searchText, currentPage ++ * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, false,advanceCustOwer,advanceCustStatus,asvanceOwnerID);   
        
        }
                    }
	/**
	 * ActionBar显示内容
	 * @param menu 要是显示的菜单
	 * @param inflater
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
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
	 * 监听Actionbar的单击事件
	 * @param item 单击的item
	 * @return 
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                mSearchMode1 = true;
                setSearchMode(true);
                break;

            case R.id.advanced_search:
                mSearchMode1 = true;
                activity.advanceSearch = true;
                if (smCustomerSearchFragment == null)
                    smCustomerSearchFragment = new SmCustomerSearchFragment();
                mItemAdvancedSearch.setVisible(false);
                mItemAdvancedSearchCancel.setVisible(true);
                 activity.switchContent(smCustomerSearchFragment);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    
    /**
     * 刷新时查询
     * @param view 视图
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onRefresh(android.view.View)
     */
	@Override
	public void onRefresh(View view) {
		super.onRefresh(view);
		customerList.clear();
		advanceCustOwer = null;
		advanceCustStatus = null;
		asvanceOwnerID = null;
        loadFlag = null;
		currentPage = 0;
		search(searchText, currentPage ++ * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, true,null,null,null);
	}
	/**
	 * 待办事项点击了那个条目
	 * @param flag
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#setFlag(java.lang.String)
	 */
    public void setFlag(String flag) {
        loadFlag = flag;
    }
}