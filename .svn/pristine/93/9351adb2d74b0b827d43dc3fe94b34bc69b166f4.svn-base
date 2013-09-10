package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 销售线索跟踪计划Fragment 
 * @extends BaseListFragment
 */
public class SmOppoFollowPlanFragment extends BaseListFragment {
	
	private final static String tag = Log.getTag(SmOppoFollowPlanFragment.class);
	List<FollowInfo> followList;
	List<TracePlan> resultList = null;
	public Activity activity;
	public Customer customer = null;
    private boolean isComeFromCustManager = false;

	public SmOppoFollowPlanFragment() {
		super();
	}
	/**
	 * @exception Throwable
	 */
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		this.activity = activity;

		System.out.println("Fragment-->onAttach");
	}

	@Override
	public void onStart() {
		
		super.onStart();
		System.out.println("Fragment-->onStart");
	}

	@Override
	public void onResume() {
		
		super.onResume();
		System.out.println("Fragment-->onResume");

		TracePlanTask task = new TracePlanTask(activity);
		task.execute(customer.getProjectID(),customer.getCustomerID());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		System.out.println("Fragment-->onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		System.out.println("Fragment-->onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 
	 * @param o0    o0
	 */
	@SuppressLint("ResourceAsColor")
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		this.getListView().setBackgroundResource(R.drawable.list_item_background);

		followList = new ArrayList<FollowInfo>();		
		displayList();
	}
	/**
	 * 异步线程，调用API获取跟踪计划列表信息
	 * @extends BaseTask
	 *
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
				 resultList = manager.getTracePlanList(arg0[0],arg0[1], "", "", 0, 0, null);
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
					System.out.println("==========="+trace.getExecuteTime());
					info.setActionContent(trace.getActivityContent());
					info.setActionType(trace.getActivityType());
					info.setDoTime(DateFormatUtils.formatDate(trace.getExecuteTime()));
					info.setDoStatus(trace.getExecuteStatus());
					followList.add(info);
				}
				displayList();
			} else {
				DialogUtils.alert(activity, getString(R.string.tracking_program_info), responseException.getMessage(), null);
			}
		}

	}

	public void displayList() {
	    OppoFollowPlangmentAdapter adapter = new OppoFollowPlangmentAdapter(activity);
		adapter.setCaches(followList);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//列表单击事件，将选择的数据信息通过INTENT传递到Activity中
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(),SmFollowPlanActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("tracePlan", resultList.get(position));
		bundle.putParcelable("customerinfo", customer);
		intent.putExtras(bundle);
		intent.putExtra("EditMode", false);
        intent.putExtra("ShowBottonBar", false);
        intent.putExtra("ShowCustomerInfo", false);
        intent.putExtra("ComeFromCustManager", isComeFromCustManager);
		startActivity(intent);
	}
	
	
	/**
	 * 销售线索跟踪计划Adapter
	 * @extends BaseCacheListAdapter
	 */
	public class OppoFollowPlangmentAdapter extends BaseCacheListAdapter<FollowInfo> {
		public OppoFollowPlangmentAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			return super.getView(arg0, arg1, arg2, R.layout.sm_list_oppo_follow_plan_item);
		}


		@Override
		protected boolean fillView(View view, FollowInfo item) {
			
			try {
				((TextView)view.findViewById(R.id.list_oppo_action_content)).setText(StringUtils.notNull(item.getActionContent()));
				((TextView)view.findViewById(R.id.list_oppo_action_type)).setText(StringUtils.notNull(item.getActionType()));
				((TextView)view.findViewById(R.id.list_oppo_do_time)).setText(StringUtils.notNull(item.getDoTime()));
				((TextView)view.findViewById(R.id.list_oppo_do_status)).setText(StringUtils.notNull(item.getDoStatus()));
				return true;
			} catch (Exception e) {
				Log.e(tag, "fillView error.", e);
			}
			return false;
		}

	}
	
	public int getCount(){
		if(resultList != null){
			return resultList.size();
		}else{
			return 0;
		}
	}
	/**
	 * 跟踪计划用实体类
	 * 
	 */
	public class FollowInfo {
		private String actionContent; 
		private String actionType;
		private String doTime;
		private String doStatus;
		
		public String getActionContent(){
			return actionContent;
		}
		public void setActionContent(String n){
			actionContent = n;
		}
		
		public String getActionType(){
			return actionType;
		}
		public void setActionType(String p){
			actionType = p;
		}
		
		public String getDoTime(){
			return doTime;
		}
		public void setDoTime(String ps){
			doTime = ps;
		}
		
		public String getDoStatus(){
			return doStatus;
		}
		public void setDoStatus(String ct){
			doStatus = ct;
		}
	}

	public void transCustomer(Customer customer, boolean isComeFromCustManager) {
		this.isComeFromCustManager  = isComeFromCustManager;
		this.customer=customer;
	}
}
