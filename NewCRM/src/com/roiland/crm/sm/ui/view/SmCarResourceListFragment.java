package com.roiland.crm.sm.ui.view;

import java.util.List;

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
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.SmCarResourceListAdapter;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.PromissionController;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 车辆资源查询列表
 * </pre>
 * @extends BaseRefreshListFragment
 * @author liuyu
 * @version $Id: SmCarResourceListFragment.java, v 0.1 2013-5-29 上午11:56:03 liuyu Exp $
 */
public class SmCarResourceListFragment extends BaseRefreshListFragment {
    private static final String tag = Log.getTag(SmCarResourceListFragment.class);
    
	private int currentPage = 0;                                   //当前页码.
	private String searchText = "";                                //检索关键字.
	private Vehicle criteria = null;                               //搜索的变量.
	private String SEARCH_COLUMNS = "chassisNo,carConfig";         // 查询变量定值.
	private SmCarResourceListAdapter listAdapter;                  //列表适配对象
	private SmCarResourceSearchFragment carSearchFragment;         //高级搜索视图
	private String NORMAL_MODE = "0";                              //正常检索flag
    private String ADVANCED_MODE = "1";                            //关联检索flag
	
	
	/**
	 * 
	 * @param Bundle savedInstanceState
	 *
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listAdapter = new SmCarResourceListAdapter(getActivity());
		setListAdapter(listAdapter);
	}

	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		Bundle bundle = this.getArguments();
		if(bundle != null){
		    Vehicle vehicle = new Vehicle();
		    vehicle = bundle.getParcelable("advancedSearch");
		    defaultSearch(vehicle);
		}else{
		    init();
		}
        
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	/**
	 * 创建菜单
	 * @param menu 菜单项
	 * @param inflater
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        Log.e(tag, "onCreateOptionsMenu()");
        MenuItem item1 = menu.findItem(R.id.search);
        MenuItem item2 = menu.findItem(R.id.advanced_search);
        item1.setVisible(PromissionController.hasPermission(item1.getTitle().toString()));
        item2.setVisible(PromissionController.hasPermission(item2.getTitle().toString()));
    }
	
	/**
	 * 选中菜单事件
	 * @param item
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
                currentPage = 0;
                activity.advanceSearch = true;
                if (carSearchFragment == null)
                    carSearchFragment = new SmCarResourceSearchFragment();
                mItemAdvancedSearch.setVisible(false);
                mItemAdvancedSearchCancel.setVisible(true);
                activity.switchContent(carSearchFragment);
                return true;
            }
	    return super.onOptionsItemSelected(item);
	}

	/**
	 * 列表选中事件
	 * @param l 列表
	 * @param v 列表中的视图
	 * @param position 位置
	 * @param id id
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (listAdapter.loadingbarViewable && position == getListView().getLastVisiblePosition() + 1)
			return;
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(activity, SmCarResourceInfoActivity.class);
		Vehicle vehicle = listAdapter.getItem(position);
		//Put vehicle object into intent and transfer to CarResourceInfoActivity class
		Bundle bundle = new Bundle();
		bundle.putParcelable("resultVehicle", vehicle);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	/**
	 * 
	 * <pre>
	 * 加载方法
	 * </pre>
	 *
	 */
	public void init (){
	    init (null);
	}
	
	/**
	 * 
	 * <pre>
	 * 加载方法
	 * </pre>
	 *
	 * @param vehicle 车辆信息
	 */
	public void init(Vehicle vehicle){
	    search(vehicle,searchText,currentPage * DEFAULT_PAGE_SIZE,DEFAULT_PAGE_SIZE,false);
	}

	/**
	 * 查询
	 * @param criteria 车辆信息
	 * @param searchText 搜索关键字
	 * @param start 开始位置
	 * @param rowCount 每页显示条数
	 * @param displayLoadingDown 是否显示loading图标
	 */
	public void search(Vehicle criteria, String searchText, int start, int rowCount, boolean displayLoadingDown) {
		if (isDownloadThreadRunning) return;
		isDownloadThreadRunning = true;
		this.searchText = searchText;
		this.criteria = criteria;
		currentPage = start / DEFAULT_PAGE_SIZE;
		
		new BaseTask<List<Vehicle>>(getActivity(), "", true, true, displayLoadingDown) {

			@Override
			protected List<Vehicle> doInBackground(String... params) {
				
				CRMManager manager = mApplication.getCRMManager();
				if (manager == null) return null;
				try {
    				    if (SmCarResourceListFragment.this.criteria == null) {
                            return manager.getCarResList(NORMAL_MODE, params[0], SEARCH_COLUMNS, null, Integer.parseInt(params[1]), Integer.parseInt(params[2]), null);
                        } else {
                            return manager.getCarResList(ADVANCED_MODE, null, SEARCH_COLUMNS, SmCarResourceListFragment.this.criteria, Integer.parseInt(params[1]), Integer.parseInt(params[2]), null);
                        }
				    } catch (ResponseException e) {
					responseException = e;
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Vehicle> result) {
				
				super.onPostExecute(result);
				completeRefresh();
				isDownloadThreadRunning = false;
				if (responseException != null) {
                    //当时间超过30分钟则跳转到首页
                    if (responseException.getStatusCode() == StatusCodeConstant.UNAUTHORIZED) {
                        DialogUtils.overMinuteDo(getActivity(),responseException);
                    } else {
                        Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show(); 
                    }
				} else {
					if (currentPage == 0) {
						displayList(true, result);
						currentPage ++;
					} else {
						displayList(false, result);
					}
				}
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
	 * 显示列表内容
	 * </pre>
	 *
	 * @param vehicleList 车辆资源列表
	 */
	public void displayList(List<Vehicle> vehicleList) {
		displayList(false, vehicleList);
	}
	
	/**
	 * 
	 * <pre>
	 * 显示列表内容
	 * </pre>
	 *
	 * @param init 是否加载
	 * @param vehicleList 车辆资源列表
	 */
	public void displayList(boolean init, List<Vehicle> vehicleList) {
		if (init) {
			listAdapter.clearData();
			listAdapter.notifyDataSetChanged();
			setListAdapter(listAdapter);
		}
		if (vehicleList == null) return;
		
		for (int i = 0; i < vehicleList.size(); i ++) {
			listAdapter.addItem(vehicleList.get(i));
		}
		if (vehicleList.size() % DEFAULT_PAGE_SIZE == 0 && vehicleList.size() > 10)
			listAdapter.loadingbarViewable = true;
		else
			listAdapter.loadingbarViewable = false;
		    listAdapter.notifyDataSetChanged();
	}

	/**
	 * 滑动监听
	 * @param view 视图
	 * @param scrollState 位置
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		switch (scrollState) {
	    case OnScrollListener.SCROLL_STATE_IDLE:
	    	//判断是否到底
	    	if (getListView().getLastVisiblePosition() == currentPage * DEFAULT_PAGE_SIZE) {
	    		search(criteria, searchText, currentPage * DEFAULT_PAGE_SIZE , DEFAULT_PAGE_SIZE, true);
                currentPage ++;
	    	}
	    	break;
		}
	}
	
	/**
	 * 搜索内容事件
	 * @param query 搜索关键字
	 * @return
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onQueryTextSubmit(java.lang.String)
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		
		super.onQueryTextSubmit(query);
		if(StringUtils.isEmpty(query)){
		    if(listAdapter != null){
		        listAdapter.clearData();
	            listAdapter.notifyDataSetChanged();
	            listAdapter.loadingbarViewable = false;
		    }
		    
		}else{
		    search(null, query.trim(), 0, DEFAULT_PAGE_SIZE, false);
		}
		return false;
	}
	
	   
	/**
	 * 
	 * <pre>
	 * 默认搜索
	 * </pre>
	 *
	 * @param vehicle 车辆信息
	 */
    private void defaultSearch(Vehicle vehicle) {
        if(isRefreshing){
        search( vehicle, "", 0, DEFAULT_PAGE_SIZE, true);
        }else{
            search( vehicle, "", 0, DEFAULT_PAGE_SIZE, false);
        }
    }
    
    /**
     * 
     * <pre>
     * 默认搜索
     * </pre>
     *
     */
	@Override
	public void defaultSearch() {
	    defaultSearch(null);
	}
	/**
	 * 刷新列表方法
	 * @param view 视图
	 * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onRefresh(android.view.View)
	 */
	@Override
	public void onRefresh(View view) {
		super.onRefresh(view);
		
		listAdapter.loadingbarViewable = false;
		listAdapter.notifyDataSetChanged();
		search(null, searchText, 0, DEFAULT_PAGE_SIZE, true);
	}
}