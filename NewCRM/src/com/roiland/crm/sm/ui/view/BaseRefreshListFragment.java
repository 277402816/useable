package com.roiland.crm.sm.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.ui.widget.PullToRefreshListView;
import com.roiland.crm.sm.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.roiland.crm.sm.utils.Log;

/**
 * 支持下拉刷新的基础类
 * @author JKim
 * @extends SherlockFragment
 * @implements OnQueryTextListener 搜索事件监听, OnScrollListener 滑动事件监听, OnRefreshListener 下拉刷新事件监听
 */
public class BaseRefreshListFragment extends SherlockFragment implements OnQueryTextListener, OnScrollListener, OnRefreshListener {
    private static final String tag = Log.getTag(BaseRefreshListFragment.class);
    
	public static final String SECTION_KEY = "section";
	public static final int DEFAULT_PAGE_SIZE = 20; // 默认每页20条记录

	protected BaseActivity activity;
	protected RoilandCRMApplication mApplication;
	protected Boolean isDownloadThreadRunning = false;
	protected Boolean searchMenuExpanded = false;
	protected boolean isRefreshing = false;

	private ListAdapter mAdapter;
	private PullToRefreshListView mList;
	private View mEmptyView;
	private TextView mStandardEmptyView;
	private View mProgressContainer;
	private View mListContainer;
	private CharSequence mEmptyText;
	private boolean mListShown;

	final private Handler mHandler = new Handler();

	static final int INTERNAL_EMPTY_ID = 0x00ff0001;
	static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
	static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;


	protected SearchView searchView = null;                           //定义搜索视图控件
	protected boolean mSearchMode = false;                            //模糊搜索状态标识
	protected boolean mSearchMode1 = false;                           //高级搜索状态标识
    
    protected MenuItem mItemSearch = null;                          //定义搜索 menu item 变量
    protected MenuItem mItemAdvancedSearch = null;                  //定义高级搜索 menu item 变量
    protected MenuItem mItemAdvancedSearchCancel = null;            //定义取消高级搜索 menu item 变量
    protected MenuItem mItemSearchView = null;                      //定义搜索视图 menu item 变量
    private FrameLayout root ;
    
	/**
	 * 创建列表视图
	 * @param savedInstanceState
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true); 
		Log.d(tag, "onCreate()");
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Context context = getActivity();

		 root = new FrameLayout(context);

		// ------------------------------------------------------------------

		LinearLayout pframe = new LinearLayout(context);
		pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
		pframe.setOrientation(LinearLayout.VERTICAL);
		pframe.setVisibility(View.GONE);
		pframe.setGravity(Gravity.CENTER);

		ProgressBar progress = new ProgressBar(context, null,
				android.R.attr.progressBarStyleLarge);
		pframe.addView(progress, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		root.addView(pframe, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		// ------------------------------------------------------------------

		FrameLayout lframe = new FrameLayout(context);
		lframe.setId(INTERNAL_LIST_CONTAINER_ID);
		
		TextView tv = new TextView(getActivity());
		tv.setId(INTERNAL_EMPTY_ID);
		tv.setGravity(Gravity.CENTER);
		lframe.addView(tv, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		PullToRefreshListView lv = new PullToRefreshListView(getActivity());
		lv.setId(android.R.id.list);
		lv.setDrawSelectorOnTop(false);
		lv.setOnRefreshListener(this);
		lframe.addView(lv, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		root.addView(lframe, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		// ------------------------------------------------------------------

		root.setLayoutParams(new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Log.d(tag, "onActivityCreted()");
		ListView listView = getListView();
		listView.setSelector(R.drawable.list_selector);
		listView.setCacheColorHint(0);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnScrollListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			if (activity instanceof BaseActivity) {
				this.activity = (BaseActivity) activity;
			}
		} catch (Exception e) {
			this.activity = null;
		}
		mApplication = (RoilandCRMApplication) activity.getApplication();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 创建菜单方法
	 * @param menu
	 * @param inflater
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
        searchView = new SearchView(activity.getSupportActionBar().getThemedContext());
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setSearchMode(false);
                defaultSearch();
                return false;
            }});
        searchView.setOnQueryTextListener(this);    //设置搜索事件监听
        mItemAdvancedSearch = menu.findItem(R.id.advanced_search);
        mItemAdvancedSearchCancel = menu.findItem(R.id.cancel);
        mItemSearch = menu.findItem(R.id.search);
        mItemSearchView = menu.add(R.string.search);
        mItemSearchView.setActionView(searchView);
        mItemSearchView.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mItemSearchView.setVisible(false);
       
	}

	/**
	 * 选中菜单事件
	 * @param item
	 * @return
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(tag, "onOptionsItemSelected : " + String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mSearchMode) {
                    cancelSearch();
                } else {
                    activity.toggle();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
	}
	
    /**
     * 
     * <pre>
     * 设置搜索模式状态
     * </pre>
     *
     * @param search
     */
    protected void setSearchMode(boolean search) {
        mSearchMode = search;
        activity.getSupportActionBar().setIcon(search ? R.drawable.back : R.drawable.menu_top_btn);
        if (search) {
            searchView.setIconified(false);
            mItemSearch.setVisible(false);
            mItemSearchView.setVisible(true);
        } else {
            mItemSearch.setVisible(true);
            mItemSearchView.setVisible(false);
        }
    }
    
    /**
     * 
     * <pre>
     * 取消搜索方法
     * </pre>
     *
     */
    public void cancelSearch() {
        if (mSearchMode) {
            searchView.setIconified(true);
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
            }
            mItemSearch.setVisible(true);
            mItemSearchView.setVisible(false);
        }
    }
    
    
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	public void defaultSearch() {
	}

	// for ListFragment
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureList();
	}

	@Override
	public void onDestroyView() {
		mHandler.removeCallbacks(mRequestFocus);
		mList = null;
		mListShown = false;
		mEmptyView = mProgressContainer = mListContainer = null;
		mStandardEmptyView = null;
		super.onDestroyView();
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
	}

	public void setListAdapter(ListAdapter adapter) {
		boolean hadAdapter = mAdapter != null;
		mAdapter = adapter;
		if (mList != null) {
			mList.setAdapter(adapter);
			if (!mListShown && !hadAdapter) {
				//这个列表被隐藏了，以前没有adapter。现在应该显示。
				setListShown(true, getView().getWindowToken() != null);
			}
		}
	}

	public void setSelection(int position) {
		ensureList();
		mList.setSelection(position);
	}

	public int getSelectedItemPosition() {
		ensureList();
		return mList.getSelectedItemPosition();
	}

	public long getSelectedItemId() {
		ensureList();
		return mList.getSelectedItemId();
	}

	public ListView getListView() {
		ensureList();
		return mList;
	}

	public void setEmptyText(CharSequence text) {
		ensureList();
		if (mStandardEmptyView == null) {
			throw new IllegalStateException("Can't be used with a custom content view");
		}
		mStandardEmptyView.setText(text);
		if (mEmptyText == null) {
			mList.setEmptyView(mStandardEmptyView);
		}
		mEmptyText = text;
	}

	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}

	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false);
	}

	private void setListShown(boolean shown, boolean animate) {
		ensureList();
		if (mProgressContainer == null) {
			throw new IllegalStateException("Can't be used with a custom content view");
		}
		if (mListShown == shown) {
			return;
		}
		mListShown = shown;
		if (shown) {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
			} else {
				mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.GONE);
			mListContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 获得和这个activity的listview相关联的listadapter
	 */
	public ListAdapter getListAdapter() {
		return mAdapter;
	}

	private void ensureList() {
		if (mList != null) {
			return;
		}
		View root = getView();
		if (root == null) {
		    root = this.root;
		}
		if (root instanceof PullToRefreshListView) {
			mList = (PullToRefreshListView)root;
		} else {
			mStandardEmptyView = (TextView)root.findViewById(INTERNAL_EMPTY_ID);
			if (mStandardEmptyView == null) {
				mEmptyView = root.findViewById(android.R.id.empty);
			} else {
				mStandardEmptyView.setVisibility(View.GONE);
			}
			mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
			mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
			View rawListView = root.findViewById(android.R.id.list);
			if (!(rawListView instanceof ListView)) {
				if (rawListView == null) {
					throw new RuntimeException(
							"Your content must have a ListView whose id attribute is " +
							"'android.R.id.list'");
				}
				throw new RuntimeException(
						"Content has view with id attribute 'android.R.id.list' "
						+ "that is not a ListView class");
			}
			mList = (PullToRefreshListView)rawListView;
			if (mEmptyView != null) {
				mList.setEmptyView(mEmptyView);
			} else if (mEmptyText != null) {
				mStandardEmptyView.setText(mEmptyText);
				mList.setEmptyView(mStandardEmptyView);
			}
		}
		mListShown = true;
		mList.setOnItemClickListener(mOnClickListener);
		if (mAdapter != null) {
			ListAdapter adapter = mAdapter;
			mAdapter = null;
			setListAdapter(adapter);
		} else {
			// 我们开始没有适配器，所以假设我们将不会得到正确数据，并启动进度指示器。
			if (mProgressContainer != null) {
				setListShown(false, false);
			}
		}
		mHandler.post(mRequestFocus);
	}

	final private Runnable mRequestFocus = new Runnable() {
		public void run() {
			mList.focusableViewAvailable(mList);
		}
	};
	
	final private AdapterView.OnItemClickListener mOnClickListener
			= new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			onListItemClick((ListView)parent, v, position, id);
		}
	};

	@Override
	public void onRefresh(View view) {
		isRefreshing = true;
		cancelSearch();
	}

	public void completeRefresh() {
		if (isRefreshing) {
			isRefreshing = false;
			if (mList != null) {
				mList.onRefreshComplete();
			}
		}
	}



    public void setFlag(String flag) {
    }

}
