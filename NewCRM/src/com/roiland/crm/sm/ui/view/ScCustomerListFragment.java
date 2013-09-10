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
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScCustomerListAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客戶管理列表Fragment
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScCustomerListFragment.java, v 0.1 2013-8-2 下午1:53:08 shuang.gao Exp $
 */
public class ScCustomerListFragment extends BaseRefreshListFragment{
	List<Customer> customerList = new ArrayList<Customer>();
    public static String               loadFlag;  //判断调用获取列表的接口
	int currentPage = 0;
	String searchText = "";
	String searchColumns = "custName,custMobile";
	Map<String, List<Customer>> customerMap = new HashMap<String, List<Customer>>();
	List<String> sectionKeyList = new ArrayList<String>();
	private boolean loadingbarviewable = false;
	private boolean isMoreData = true;
	private ScCustomerListAdapter listAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//初期化显示列表
		defaultSearch();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            if(data != null){
                Customer customer = data.getParcelableExtra("editcustomerinfo");
                if(customer != null){
                    setCurrentItem(customer);
                    displayList();
                }
            } 
        }
    }

    public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		if (listAdapter == null) {
			listAdapter = new ScCustomerListAdapter(this.getActivity());
			setListAdapter(listAdapter);
		}
		//设为单选，允许列表项切换checked/unchecked状态
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setOnScrollListener(this);
		
	}
	/**
	 * onListItemClick 单击列表条目时的单击事件
	 * @param l listView
	 * @param v View
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
		Intent intent = new Intent(activity, ScCustomerInfoActivity.class);
		intent.putExtra("CUSTOMERID", ((Customer)listAdapter.getItem(position)).getCustomerID());
		startActivityForResult(intent, 2);
	}
	/**
	 * search 异步操作 查询客户列表信息
	 * @param searchText 搜索关键字
	 * @param start 搜索开始索引
	 * @param rowCount 获取的记录条数
	 * @param displayLoadingDown 是否执行刷新
	 * 
	 */
	public void search(String searchText, int start, int rowCount, boolean displayLoadingDown) {
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
                        downloadedList = manager.getCustomerList(searchText, searchColumns, start, rowCount, null,null,null,null);
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
					setMoreData(false);
					displayList();
				} else {
					customerList.addAll(result);
					if (result.size() == 0) {
						setMoreData(false);
					} else {
						setMoreData(true);
					}
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
	 * 将列表按建立时间倒叙排序显示出来
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
	 * 	每页显示20条后滑动刷新 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。    
	 * @param arg0
	 * @param firstVisibleItem 当前能看见的第一个列表项ID
	 * @param visibleItemCount 当前能看见的列表项个数 
	 * @param totalItemCount：列表项共数 
	 */
	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (customerMap != null && getListView().getLastVisiblePosition() - customerMap.keySet().size() == currentPage * DEFAULT_PAGE_SIZE) {
			currentPage ++;
			if (currentPage == 1) {
				search(searchText, (currentPage - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, false);
			} else {
				search(searchText, (currentPage - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, true);
			}
		}
	}
	/**
	 * onQueryTextSubmit 搜索时根据关键字查询
	 * @param query 查询的关键字
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
            search(searchText, currentPage ++ * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, false);
        }
		return false;
	}
	/**
	 * defaultSearch 页面加载查询
	 * @param 
	 */
	@Override
	public void defaultSearch() {
		searchText = "";
		customerList.clear();
		currentPage = 0;
		if (listAdapter != null) {
			listAdapter.loadingbarViewable = false;
			listAdapter.removeAll();
			setListAdapter(listAdapter);
		}
		search(searchText, currentPage ++ * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, false);		
	}
	/**
	 * setCurrentItem 从客户管理详细列表返回客户管理列表时从新设置Customer的信息
	 * @param editCust 客户信息
	 */
	public void setCurrentItem(Customer editCust) {
		String editCustId = editCust.getCustomerID();
		for (Customer cust : customerList) {
			if (!cust.getCustomerID().equals(editCustId))
				continue;
			cust.setCustName(editCust.getCustName());
			cust.setCustMobile(editCust.getCustMobile());
			cust.setCustType(editCust.getCustType());
			cust.setCustStatus(editCust.getCustStatus());
			break;
		}
	}
	/**
	 * onRefresh 刷新时查询
	 * @param view 
	 */
	@Override
	public void onRefresh(View view) {
		super.onRefresh(view);
		customerList.clear();
		currentPage = 0;
		 loadFlag = null;
		search(searchText, currentPage ++ * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE, true);
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
	public boolean isMoreData() {
		return isMoreData;
	}

	public void setMoreData(boolean isMoreData) {
		this.isMoreData = isMoreData;
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