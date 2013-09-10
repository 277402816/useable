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
 * 
 * <pre>
 * 销售线索跟踪计划Fragment
 * </pre>
 * @extends BaseListFragment
 * @author liuyu
 * @version $Id: SmOppoTracePlanFragment.java, v 0.1 2013-5-22 下午12:08:44 liuyu Exp $
 */
public class SmOppoTracePlanFragment extends BaseListFragment {
	
    public Activity activity;
    public Customer customer = null;
    public String projectId = null;
	private final static String tag = Log.getTag(SmOppoInfoActivity.class);
	List<FollowInfo> followList;
	List<TracePlan> resultList = null;
	public SmOppoTracePlanFragment() {
		super();
	}

	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		this.activity = activity;
	}


	@Override
	public void onResume() {
		
		super.onResume();
		TracePlanTask task = new TracePlanTask(activity);
		task.execute(projectId,customer.getCustomerID());
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
	                info.setActionContent(trace.getActivityType());
	                info.setActionType(trace.getActivityContent());
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

	/**
	 * 
	 * <pre>
	 * 页面显示
	 * </pre>
	 *
	 */
	public void displayList() {
		OppoTestDriveFragmentAdapter adapter = new OppoTestDriveFragmentAdapter(activity);
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
		Intent intent = new Intent(getActivity(),SmFollowPlanActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("tracePlan", resultList.get(position));
		bundle.putParcelable("customerinfo", customer);
		intent.putExtras(bundle);
		intent.putExtra("EditMode", false);
        intent.putExtra("ShowBottonBar", false);
        intent.putExtra("ShowCustomerInfo", false);
        intent.putExtra("ComeFromOppo", true);
		startActivity(intent);
	}
	
	/**
	 * 
	 * <pre>
	 * 销售线索跟踪计划Adapter
	 * </pre>
	 * @extends BaseCacheListAdapter
	 * @author liuyu
	 * @version $Id: SmOppoTracePlanFragment.java, v 0.1 2013-5-22 下午12:12:19 liuyu Exp $
	 */
	public class OppoTestDriveFragmentAdapter extends BaseCacheListAdapter<FollowInfo> {
		public OppoTestDriveFragmentAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			return super.getView(arg0, arg1, arg2, R.layout.sm_list_oppo_follow_plan_item);
		}


		/**
		 * 页面添加数据
		 * @param view 要添加数据的视图
		 * @param item 添加的数据
		 * @return 是否添加成功
		 * @see com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter#fillView(android.view.View, java.lang.Object)
		 */
		@Override
		protected boolean fillView(View view, FollowInfo item) {
			
			try {
				((TextView)view.findViewById(R.id.list_oppo_action_content)).setText(StringUtils.notNull(item.getActionContent()).trim());
				((TextView)view.findViewById(R.id.list_oppo_action_type)).setText(StringUtils.deleteBlank(StringUtils.notNull(item.getActionType()).trim()));
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
	 * 
	 * <pre>
	 * 跟踪计划用实体类
	 * </pre>
	 *
	 * @author liuyu
	 * @version $Id: SmOppoTracePlanFragment.java, v 0.1 2013-5-22 下午12:13:52 liuyu Exp $
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

	public void transCustomer(Customer customer) {
		
		this.customer=customer;
	}
	
	public void transProjectID(String projectId){
	    this.projectId = projectId;
	}
}
