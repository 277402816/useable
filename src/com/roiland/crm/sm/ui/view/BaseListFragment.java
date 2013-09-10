package com.roiland.crm.sm.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 列表Fragment基础类
 * </pre>
 * @extends SherlockListFragment
 * @implements OnQueryTextListener, OnScrollListener
 * @author shuang.gao
 * @version $Id: BaseListFragment.java, v 0.1 2013-7-2 上午9:31:24 shuang.gao Exp $
 */
public class BaseListFragment extends SherlockListFragment implements OnQueryTextListener, OnScrollListener {
    private String tag = Log.getTag(BaseListFragment.class);
	public static final String SECTION_KEY = "section";
	public static final int DEFAULT_PAGE_SIZE = 20; // 默认每页20条记录

	protected BaseActivity activity;
	protected RoilandCRMApplication mApplication;
	protected Boolean isDownloadThreadRunning = false;
    protected Boolean searchMenuExpanded = false;

    /**
     * 
     * <pre>
     * 回调方法
     * </pre>
     *
     */
	public void onCreate() {
		this.setHasOptionsMenu(true);
	}
	
	/**
	 * 创建菜单列表
	 * @param menu
	 * @param inflater
	 * @see com.actionbarsherlock.app.SherlockListFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		Log.d(tag,"onCreateOptionsMenu>>>");
        
	}
	
	/**
	 * 回调方法
	 * @param savedInstanceState
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Log.d(tag,"onActivityCreated>>>");
        
        this.getListView().setSelector(R.drawable.list_selector);
        this.getListView().setCacheColorHint(0);
        this.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.getListView().setOnScrollListener(this);
	}
	
	/**
	 * 回调方法
	 * @param activity
	 * @see com.actionbarsherlock.app.SherlockListFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			if (activity instanceof BaseActivity) {
				this.activity = (BaseActivity) activity;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.activity = null;
		}
		mApplication = (RoilandCRMApplication) activity.getApplication();
	}
	
	/**
	 * 回调方法
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
    public void onResume() {
		super.onResume();
	}
	
	/**
	 * 回调方法
	 * @param requestCode 请求code
	 * @param resultCode 返回code
	 * @param data 返回的数据
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
    }

	/**
	 * 滑动监听
	 * @param view
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 * @param totalItemCount
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	/**
	 * 滑动监听
	 * @param view 视图
	 * @param scrollState 位置
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	/**
	 * Actionbar搜索方法
	 * @param query 关键字
	 * @return false
	 * @see com.actionbarsherlock.widget.SearchView.OnQueryTextListener#onQueryTextSubmit(java.lang.String)
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	/**
	 * 监听输入框内容改变
	 * @param newText 关键字
	 * @return false
	 * @see com.actionbarsherlock.widget.SearchView.OnQueryTextListener#onQueryTextChange(java.lang.String)
	 */
	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	/**
	 * 
	 * <pre>
	 * 默认查询方法
	 * </pre>
	 *
	 */
	public void defaultSearch() {
	}
}