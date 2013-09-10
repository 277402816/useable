package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.FollowInfo;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScOppoTracePlanFragmentAdapter;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 销售线索跟踪计划Fragment
 * </pre>
 * @extends BaseListFragment
 * @author liuyu
 * @version $Id: ScOppoTracePlanFragment.java, v 0.1 2013-7-3 上午10:24:17 liuyu Exp $
 */
public class ScOppoTracePlanFragment extends BaseListFragment {
	
    public Activity activity;
    public Customer customer = null;
    public String projectId = null;
    public int currentPage = 0;
	private final static String tag = Log.getTag(SmOppoInfoActivity.class);
	List<FollowInfo> followList;
	List<TracePlan> resultList = null;
	
	public ScOppoTracePlanFragment() {
		super();
	}

	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		this.activity = activity;
	}


	@Override
	public void onResume() {
	    currentPage = 0;
		search();
		super.onResume();
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 页面创建时调用此方法
	 * @param savedInstanceState
	 * @see com.roiland.crm.sm.ui.view.BaseListFragment#onActivityCreated(android.os.Bundle)
	 */
	@SuppressLint("ResourceAsColor")
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		this.getListView().setBackgroundResource(R.drawable.list_item_background);

		followList = new ArrayList<FollowInfo>();		
		displayList();
	}
	
	public void search(){
	    TracePlanTask task = new TracePlanTask(activity);
        task.execute(projectId,customer.getCustomerID());
	}
	
	/**
	 * 
	 * <pre>
	 * 异步线程，调用API获取跟踪计划列表信息
	 * </pre>
	 * @extends BaseTask
	 * @author liuyu
	 * @version $Id: SmOppoTracePlanFragment.java, v 0.1 2013-5-22 下午12:10:15 liuyu Exp $
	 */
	private class TracePlanTask extends BaseTask<List<TracePlan>> {

		private String tag = Log.getTag(TracePlanTask.class);
		public TracePlanTask(Activity activity) {
			super(activity);
		}
		
		@Override
		protected List<TracePlan>  doInBackground(String... arg0) {
			RoilandCRMApplication application =(RoilandCRMApplication)activity.getApplication();
			try {
				 CRMManager manager = application.getCRMManager();
				 resultList = manager.getTracePlanList(arg0[0],arg0[1], "", "", currentPage, (currentPage+1) * DEFAULT_PAGE_SIZE, null);
			} catch (ResponseException e) {
				responseException = e;
			}
			return resultList;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(List<TracePlan> result) {
			
			 Log.i(tag, "onPostExecute  => " + result);
			super.onPostExecute(result);
			if (result != null) {
				resultList = result;
				followList.clear();
				int num = result.size();
				for (int i = 0; i < num; i++) {
					TracePlan trace = result.get(i);
					trace = result.get(i);
					FollowInfo info = new FollowInfo();
	                info.setActionContent(trace.getActivityContent());
	                info.setActionType(trace.getActivityType());
	                info.setDoTime(trace.getExecuteTime());
	                info.setDoStatus(trace.getExecuteStatus());
	                followList.add(info);
				}
				currentPage ++;
				displayList();
				ScOppoTracePlanFragment.this.setHasOptionsMenu(true);
			}
		}

	}

	/**
	 * 
	 * <pre>
	 * 页面显示
	 * </pre>
	 *
	 */
	public void displayList() {
		ScOppoTracePlanFragmentAdapter adapter = new ScOppoTracePlanFragmentAdapter(activity);
		adapter.setCaches(followList);
		setListAdapter(adapter);
	}
	
	/**
	 * ListView单击事件
	 * @param l 页面的ListView
     * @param v ListView的item
     * @param position 单击时候item的位置
     * @param id 点击listview中的item id
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
	    //列表单击事件，将选择的数据信息通过INTENT传递到Activity中
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(),ScFollowPlanActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("tracePlan", resultList.get(position));
		bundle.putParcelable("customerinfo", customer);
		intent.putExtras(bundle);
        intent.putExtra("ShowBottonBar", false);
        intent.putExtra("ShowCustomerInfo", false);
        intent.putExtra("ComeFromOppo", true);
		startActivity(intent);
	}
	
	
	
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        MenuItem item = menu.findItem(R.id.add);
        item.setVisible(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(getActivity(),ScFollowPlanActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("addFlag", true);
                bundle.putParcelable("customerOppoFrom", customer);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                int lastposition = getListView().getLastVisiblePosition();
                
                if (lastposition == currentPage * DEFAULT_PAGE_SIZE-1) {
                    search();
                }
                break;
        }
    }


    public int getCount(){
		if(resultList != null){
			return resultList.size();
		}else{
			return 0;
		}
	}

	public void transCustomer(Customer customer) {
		
		this.customer=customer;
	}
	
	public void transProjectID(String projectId){
	    this.projectId = projectId;
	}
}
