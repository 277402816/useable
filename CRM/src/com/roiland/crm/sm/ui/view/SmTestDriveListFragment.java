package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.DriveTest;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.SmTestDriveListAdapter;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 试乘试驾列表fragment
 * </pre>
 * @extends BaseListFragment
 * @author liuyu
 * @version $Id: SmTestDriveListFragment.java, v 0.1 2013-5-20 下午2:18:30 liuyu Exp $
 */

public class SmTestDriveListFragment extends BaseListFragment {
	public Activity activity;
	List<DriveTest> testdriveList = new ArrayList<DriveTest>();
	List<DriveTest> driverList = new ArrayList<DriveTest>();
	Project project = new Project();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
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

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.getListView().setBackgroundResource(R.drawable.list_item_background);
		displayList(new ArrayList<DriveTest>());
		TestDriveTask task = new TestDriveTask(activity);
		 task.execute();
	}

	/**
	 * 
	 * <pre>
	 * 异步线程处理 从后台获取试乘试驾列表信息
	 * </pre>
	 *
	 * @author liuyu
	 * @version $Id: SmTestDriveListFragment.java, v 0.1 2013-5-22 下午12:17:48 liuyu Exp $
	 */
	private class TestDriveTask extends BaseTask<List<DriveTest>> {

		 private String tag = Log.getTag(TestDriveTask.class);

		public TestDriveTask(Activity activity) {
			super(activity);
		}
		
		@Override
		protected List<DriveTest> doInBackground(String... arg0) {
			RoilandCRMApplication application =(RoilandCRMApplication)activity.getApplication();
			try {
				 CRMManager manager = application.getCRMManager();
				 //获得销售线索试乘试驾列表
				 driverList = manager.getProjectDriveTest(project.getCustomer().getProjectID());
			} catch (ResponseException e) {
				responseException = e;
			}
			return driverList;
		}
		@Override
		protected void onPostExecute(List<DriveTest> result) {
			 Log.i(tag, "onPostExecute  => " + result);
			super.onPostExecute(result);
			//当有异常时提示错误信息
			if (responseException != null) {
				if (getActivity() != null) {
					Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
				}
			} else if (result != null) {
				//当试乘试驾不为空时加载列表
				displayList(result);
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 加载列表
	 * </pre>
	 *
	 * @param dataList 试乘试驾信息List集合
	 */
	public void displayList(List<DriveTest> dataList) {
		SmTestDriveListAdapter adapter = new SmTestDriveListAdapter(activity);
		adapter.setCaches(dataList);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	/**
	 *  
	 */
	/**
	 * 列表点击监听事件
	 * @param l 页面的ListView
     * @param v ListView的item
     * @param position 单击时候item的位置
     * @param id 点击listview中的item id
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), SmTestDriveInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("driverinfo", driverList.get(position));
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	/**
	 * 
	 * <pre>
	 * 获得当前条目的Project
	 * </pre>
	 *
	 * @param  project 销售线索信息
	 */
	public void transferProjectId(Project project) {
		this.project = project;
	}
}
