package com.roiland.crm.sm.ui.view;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.TodayWorkContent;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.widget.WelcomeBodyWidgetView;
import com.roiland.crm.sm.ui.widget.WelcomeHeaderWidgetView;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.PromissionController;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 
 * <pre>
 * 主页画面
 * </pre>
 *
 * @author cjyy
 * @version $Id: WelcomeFragment.java, v 0.1 2013-5-13 下午11:55:18 cjyy Exp $
 */
public class WelcomeFragment extends SherlockFragment {
    private final static String tag = Log.getTag(WelcomeFragment.class);
    
	public WelcomeHeaderWidgetView headerView;
	public WelcomeBodyWidgetView bodyView;
	private BaseActivity mActivity;
	private TodayWorkContent todayWorkCentent;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        initData();
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);	//设置介绍menu事件
        Log.d(tag, "onCreate()");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        Log.d(tag, "onCreateView()");
        View rootView = inflater.inflate(R.layout.welcome,	container, false);
        
		headerView = (WelcomeHeaderWidgetView) rootView.findViewById(R.id.header_view);
		bodyView = (WelcomeBodyWidgetView) rootView.findViewById(R.id.todo_view);
		return rootView;
    }
    
    /**
     * 设置初始数据
     */
    private void initData() {
    	if (todayWorkCentent != null) {
    		User user = todayWorkCentent.getUser();
    		String[] item1 = new String[] {getString(R.string.depart_label), user.getDepartName()};
            String[] item2 = new String[] {getString(R.string.position_label), user.getPosiName()};
            String[] item3 = new String[] {getString(R.string.user_name_label), user.getUserName()};
            headerView.getWelcomeHeaderAdapter().addItem(item1);
            headerView.getWelcomeHeaderAdapter().addItem(item2);
            headerView.getWelcomeHeaderAdapter().addItem(item3);
    	}
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        Log.d(tag, "onActivityCreted()");
        //Do task and load todo work content.
        WelcomeTask task = new WelcomeTask(mActivity);
        task.execute();
    }
    
    
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    Log.e(tag, "onCreateOptionsMenu()");
	    
        
	}
	
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * 获取待办事项的信息
     */
    private class WelcomeTask extends BaseTask<TodayWorkContent> {
    	
		private String tag = Log.getTag(WelcomeTask.class);
    	protected ResponseException responseException;
        
        public WelcomeTask(Activity activity) {
			super(activity);
		}
    	
		@Override
		protected TodayWorkContent doInBackground(String... params) {
			TodayWorkContent todayWorkCentent = null;
			try {
				RoilandCRMApplication application = (RoilandCRMApplication) mActivity.getApplication();
				CRMManager manager = application.getCRMManager();
				todayWorkCentent = manager.getTodayWorking();
			} catch (ResponseException e) {
				Log.e(tag, "Get today working list failure.", e);
				responseException = e;
			}
			return todayWorkCentent;
		}
		
		@Override
		protected void onPostExecute(TodayWorkContent result) {
			Log.i(tag, "onPostExecute  => " + result);
			super.onPostExecute(result);
			if (responseException != null) {
			    if(getActivity() != null ) {
	                if (responseException.getStatusCode() == StatusCodeConstant.UNAUTHORIZED) {
	                    DialogUtils.overMinuteDo(getActivity(),responseException);
	                } else {
	                    Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
	                }
			    }
			} else if (result != null) {
				//用户信息
	    		User user = result.getUser();
	    		String[] item1 = new String[] {getString(R.string.depart_label), user.getDepartName()};
	            String[] item2 = new String[] {getString(R.string.position_label), user.getPosiName()};
	            String[] item3 = new String[] {getString(R.string.user_name_label), user.getUserName()};
	            headerView.getWelcomeHeaderAdapter().addItem(item1);
	            headerView.getWelcomeHeaderAdapter().addItem(item2);
	            headerView.getWelcomeHeaderAdapter().addItem(item3);
	            headerView.getWelcomeHeaderAdapter().notifyDataSetChanged();
	            
	            //我的待办事项信息
	            String[] info1 = new String[]{getString(R.string.todo_trace_plan_label), ""
	                                          , getString(R.string.todo_expired_trace_plan), result.getExpiredActionPlanCount()
	                                          , getString(R.string.todo_today_trace_plan), result.getTodayActionPlanCount()};
	    		
	    		String[] info2 = new String[]{getString(R.string.todo_sales_oppo_label), ""
	    				, getString(R.string.todo_expired_sales_oppo), result.getExpiredSalesProjectCount()
	    				, getString(R.string.todo_three_day_oppo), result.getThreeDaysSalesProjectCount()};
	    		
	    		String[] info3 = new String[]{getString(R.string.todo_cust_order_label), ""
	    				, getString(R.string.todo_three_day_order), result.getThreeDaysDeliveryOrderCount(), "", ""};
	    		
	    		String[] info4 = new String[]{getString(R.string.todo_cust_manager_label), ""
	    				, getString(R.string.todo_no_traceplan_customer), result.getNoActionPlanProjectCount(), "", ""};
	    		
	    		bodyView.getWelcomeBodyAdapter().addItem(info1);
	    		bodyView.getWelcomeBodyAdapter().addItem(info2);
	    		bodyView.getWelcomeBodyAdapter().addItem(info3);
	    		bodyView.getWelcomeBodyAdapter().addItem(info4);
	    		
	    		headerView.getWelcomeHeaderAdapter().notifyDataSetChanged();
	    		
	    	}
		}
		
		
		@Override  
		protected void onCancelled() {
			super.onCancelled();
		}
    }
}
