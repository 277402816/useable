package com.roiland.crm.sm.ui.view;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScCustomerOrderListAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.StringUtils;
/**
 * 
 * <pre>
 * 客户订单列表Fragment
 * </pre>
 * @extends BaseRefreshListFragment
 * @author liuyu
 * @version $Id: ScCustomerOrderListFragment.java, v 0.1 2013-5-17 上午9:30:41 liuyu Exp $
 */
public class ScCustomerOrderListFragment extends BaseRefreshListFragment{
    //判断调用获取列表的接口
    public static String               loadFlag;
    String                             searchText         = "";
    private int                        beginnum           = 0;
    int                                currentPage        = 0;
    String                             SEARCH_COLUMNS     = "custName,custMobile";
    List<String>                       sectionKeyList     = new ArrayList<String>();
    Map<String, List<CustOrder>>       customerOrderMap   = new HashMap<String, List<CustOrder>>();
    BaseTask<List<CustOrder>>          task;
    private boolean                    loadingbarviewable = false;
    private boolean                    posinit            = true;
    private List<CustOrder>            CustOrderList      = null;
    private ScCustomerOrderListAdapter listAdapter        = null;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//初期化检索
		init();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		if (listAdapter == null) {
			listAdapter = new ScCustomerOrderListAdapter(this.getActivity());
			setListAdapter(listAdapter);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	/**
	 * 初期化检索时调用
	 */
	private void init() {
		currentPage = 0;
		searchText = null;
		if (customerOrderMap != null)
			customerOrderMap.clear();
		if (sectionKeyList != null)
			sectionKeyList.clear();
		searchText = null; // Reset search world.
		if (task != null)
			task.cancel(true);
		posinit = true;
		search(searchText, 0, DEFAULT_PAGE_SIZE, false);
	}
	
	/**
	 * 点击列表条目监听事件
	 * @param l 页面的ListView
     * @param v ListView的item
     * @param position 单击时候item的位置
     * @param id 点击listview中的item id
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (loadingbarviewable && position == getListView().getLastVisiblePosition() + 1)
			return;
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(activity, ScCustomerOrderInfoActivity.class);
		intent.putExtra("ShowBottomBar", true);
		intent.putExtra("ORDERID", ((TextView)v.findViewById(R.id.customer_order_list_order_id)).getText().toString());
		startActivityForResult(intent,1);
	}

	
	/**
	 * 
	 * <pre>
	 * 查询检索信息
	 * </pre>
	 * 
	 * @param searchText 搜索关键字
     * @param start 搜索开始位置
     * @param rowCount 获取的记录条数 
     * @param displayLoadingDown 是否加载
	 */
	public void search(String searchText, int start, int rowCount, boolean displayLoadingDown) {
	    Log.i("displayLoadingDown", ""+displayLoadingDown);
		if (isDownloadThreadRunning) return;
		isDownloadThreadRunning = true;
		this.searchText = searchText;
		currentPage = start / DEFAULT_PAGE_SIZE;
		//执行异步操作 调去API 获取订单列表信息
		task = new BaseTask<List<CustOrder>>(getActivity(), "", true, true, displayLoadingDown) {
			
			@Override
			protected List<CustOrder> doInBackground(String... params) {
				CRMManager manager = mApplication.getCRMManager();
				if (manager == null) return null;
				try {
                    if("0".equals(loadFlag)){
                        //3日到期订单
//                        loadFlag = null;
                        CustOrderList = manager.getThreeDaysDeliveryOrderList(params[0], SEARCH_COLUMNS, Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                    }else{
                        CustOrderList = manager.getOrderList(null,null,params[0], SEARCH_COLUMNS, Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                    }
				} catch (ResponseException e) {
					responseException = e;
				}
				return CustOrderList;
			}

			@Override
			protected void onPostExecute(List<CustOrder> result) {
				super.onPostExecute(result);
				//完成刷新操作
				completeRefresh();
				//捕获异常 提示错误信息
				if (responseException != null) {
					if (getActivity() != null) {
					    //当时间超过30分钟则跳转到首页
					    if (responseException.getStatusCode() == StatusCodeConstant.UNAUTHORIZED) {
					        DialogUtils.overMinuteDo(getActivity(),responseException);
					    } else {
					        Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show(); 
					    }
						
					}
				} else {
					if (currentPage == 0) {
						displayList(true, result);
						currentPage ++;
					} else {
						displayList(false, result);
						currentPage ++;
					}
				}
				isDownloadThreadRunning = false;
			}
			//取消异步操作
			@Override
			protected void onCancelled() {
				isDownloadThreadRunning = false;
				super.onCancelled();
			}
		};
		task.execute(searchText, String.valueOf(start), String.valueOf(rowCount));
	}

	public void displayList(List<CustOrder> orderList) {
		displayList(false, orderList);
	}
	
	/**
	 * 
	 * <pre>
	 * 将列表按时间顺序列出来
	 * </pre>
	 * 查询后显示信息
	 * @param init true 则清空列表
	 * @param orderList 订单信息List集合
	 */
	public void displayList(boolean init, List<CustOrder> orderList) {
		if (init) {
			//初期清空
			sectionKeyList.clear();
			customerOrderMap.clear();
			listAdapter.removeAll();
			setListAdapter(listAdapter);
		}
		
		if (orderList == null || orderList.size() == 0){
		    listAdapter.loadingbarViewable = false;
	        loadingbarviewable = false;
	        return;
		}
		  
		for (int i = 0; i < orderList.size(); i ++) {
			CustOrder custOrder = orderList.get(i);
			String key = null;
			//流失订单和订单完成显示在后无日期栏
//			if(getString(R.string.lost_order).equals(custOrder.getOrderStatus()) || getString(R.string.finish_order).equals(custOrder.getOrderStatus())){
//			    key = "";
//			}else{
                key = DateFormatUtils.formatDate(custOrder.getCreateTime());
//			}			
			List<CustOrder> subList = customerOrderMap.get(key);
			if (subList == null) {
				subList = new ArrayList<CustOrder>();
				customerOrderMap.put(key, subList);
				sectionKeyList.add(key);
			}
			subList.add(custOrder);
		}
		loadingbarviewable = false;
		for (int i = 0; i < sectionKeyList.size(); i ++) {
			if (i == sectionKeyList.size() - 1 && orderList.size() % DEFAULT_PAGE_SIZE == 0 && orderList.size() > 10) {
				//显示加载中
				listAdapter.loadingbarViewable = true;
				loadingbarviewable = true;
			} else
				listAdapter.loadingbarViewable = false;
			listAdapter.notifyDataSetChanged();
			listAdapter.addSection(sectionKeyList.get(i), customerOrderMap.get(sectionKeyList.get(i)));
		}
		if (posinit) {
			posinit = false;
		}
		listAdapter.notifyDataSetChanged();
	}
	
	
	/**
	 * 列表滑到底端时查询
	 * @param view 视图
	 * @param scrollState 判断是否执行刷新的状态
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
	    case OnScrollListener.SCROLL_STATE_IDLE:
	    	if (getListView().getLastVisiblePosition() - listAdapter.getHeaderCount() >= currentPage * DEFAULT_PAGE_SIZE)
	    	{
	    		search(searchText, currentPage * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, true);
		    }
	    	break;
		}
	}
	
	/**
	 * 搜索时根据关键字查询
	 * @param query 检索关键字
	 * @return 
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onQueryTextSubmit(java.lang.String)
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
	    super.onQueryTextSubmit(query);
	    if(StringUtils.isEmpty(query)){
            listAdapter.removeAll();
            listAdapter.notifyDataSetChanged();
            listAdapter.loadingbarViewable = false;
        }else{
            beginnum = 0;
            listAdapter.loadingbarViewable = false;
            search(query.trim(), beginnum, DEFAULT_PAGE_SIZE, false);
        }
		return false;
	}
	
	/**
	 * 页面加载查询
	 * 
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#defaultSearch()
	 */
	@Override
	public void defaultSearch() {
		listAdapter.loadingbarViewable = false;
		 if(isRefreshing){
		     search("", 0, DEFAULT_PAGE_SIZE, true);
		 }else{
		     search("", 0, DEFAULT_PAGE_SIZE, false);
		 }
		
	}
	
	/**
	 * 下拉刷新调用此方法
	 * @param view
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onRefresh(android.view.View)
	 */
	@Override
	public void onRefresh(View view) {
		super.onRefresh(view);
		searchText = "";
		loadFlag = null;
		search(searchText, 0, DEFAULT_PAGE_SIZE, true);
	}

	/**
	 * 设置ActionBar的显示菜单
	 * @param menu 显示的菜单
	 * @param inflater
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item1 = menu.findItem(R.id.search);
        item1.setVisible(true);
    }

    /**
     * 单击ActionBar菜单的监听事件
     * @param item 单击的item
     * @return
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                setSearchMode(true);
                break;
            case R.id.advanced_search:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFlag(String flag) {
        super.setFlag(flag);
        loadFlag = flag;
    }
}