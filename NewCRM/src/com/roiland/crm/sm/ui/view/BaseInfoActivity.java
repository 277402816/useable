package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.utils.Log;

/**
 * 一个抽象类，显示信息画面的activity基础类
 * @author JKim
 * @extends SherlockFragmentActivity
 */
public abstract class BaseInfoActivity extends SherlockFragmentActivity {
	private final static String tag = Log.getTag(BaseInfoActivity.class);
	protected SearchView searchView = null;                           //定义搜索视图控件
    protected boolean mSearchMode = false;                            //模糊搜索状态标识
    protected boolean mSearchMode1 = false;                           //高级搜索状态标识
    
    public Fragment mFrament;
    protected BaseInfoFragmentAdapter adapter;
    protected TextView titleText;                                     //标题控件
    protected ActionBar actionBar = null;                             //ActionBar控件

	public BaseInfoActivity() {
		adapter = new BaseInfoFragmentAdapter(getSupportFragmentManager());
	}
	
	public BaseInfoActivity(Fragment f) {
		adapter = new BaseInfoFragmentAdapter(getSupportFragmentManager(), f);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        // 填充上边的VIEW
//      if (savedInstanceState != null) {
//          String roleName = savedInstanceState.getString("roleName");
//          PromissionController.setCurrentRole(roleName);
//          mFrament = getSupportFragmentManager().getFragment(savedInstanceState, "mFrament");
//          
//      }
		setContentView(R.layout.base_info_activity_layout);
		actionBar = (ActionBar)getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.section_oppoinfo_header));
        actionBar.setIcon(getResources().getDrawable(R.drawable.back_btn1));
        actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.info_actionbar_title_layout);
        actionBar.setHomeButtonEnabled(true);
        titleText = (TextView)actionBar.getCustomView().findViewById(R.id.actionbar_title);
		((ViewPager)findViewById(R.id.base_info_pager)).setAdapter(adapter);
		
	}
    
	@Override
	public void onActionModeFinished(ActionMode mode) {
	}
	
	/**
	 * 设置tittle bar的文字
	 * @param title
	 */
	public void setTitle(String title) {
		titleText.setText(title);
	}
	
	
	/**
	 * 创建菜单
	 * @param menu     菜单对象
	 * @return         
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
        Log.e(tag, "onCreateOptionsMenu : ===" );
        getSupportMenuInflater().inflate(R.menu.info_menu, menu);
        return false;
    }

    /**
     * 菜单被选择事件
     * @param item
     * @return
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (((ViewPager)findViewById(R.id.base_info_pager))!=null&&((ViewPager)findViewById(R.id.base_info_pager)).getCurrentItem() > 0) {
                    ((ViewPager)findViewById(R.id.base_info_pager)).setCurrentItem(0);
                }else {
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
	/**
	 * 添加fragment到pager adapter并且移动到pager adapter中
	 * @param f
	 */
	public void addFragment(Fragment f) {
		try {
		    //this.mFrament = f;
			adapter.addFragment(f);
			((ViewPager)findViewById(R.id.base_info_pager)).setCurrentItem(adapter.getCount() - 1, true);
		} catch (Exception e) {
			Log.e(tag, "addFragment() >> ", e);
		}
	}
	/**
	 * 添加fragment到pager adapter
	 * @param f
	 * @param index
	 */
	public void addFragment(Fragment f, int index) {
		adapter.addFragment(f, index);
	}
	 
	/**
	 * 从pager adapter中移除fragment
	 * @param f
	 */
	public void removeFragment(Fragment f) {
		adapter.removeFragment(f);
	}
	
	/**
	 * 从pager adapter中移除fragment
	 * @param index
	 */
	public void removeFragment(int index) {
		adapter.removeFragment(index);
	}
	
	/**
	 * 从pager adapter中移除fragment
	 */
	public void removeFragment() {
		adapter.removeFragment();
	}
	
	/**
	 * pager adapter类
	 * @author JKim
	 *
	 */
	public class BaseInfoFragmentAdapter extends FragmentStatePagerAdapter {
		List<Fragment> fragmentList;
		
		public BaseInfoFragmentAdapter(FragmentManager fm) {
			super(fm);
			fragmentList = new ArrayList<Fragment>();
		}

		public BaseInfoFragmentAdapter(FragmentManager fm, Fragment f) {
			super(fm);
			fragmentList = new ArrayList<Fragment>();
			fragmentList.add(f);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
		public void addFragment(Fragment f) {
			fragmentList.add(f);
			notifyDataSetChanged();
		}
		
		public void addFragment(Fragment f, int index) {
			fragmentList.add(index, f);
			notifyDataSetChanged();
		}
		
		public void removeFragment(Fragment f) {
			fragmentList.remove(f);
			notifyDataSetChanged();
		}
		
		public void removeFragment(int index) {
			fragmentList.remove(index);
			notifyDataSetChanged();
		}
		
		public void removeFragment() {
			fragmentList.remove(fragmentList.size() - 1);
			notifyDataSetChanged();
		}
	}
	

	/**
	 * 返回键监听方法
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
	    if (((ViewPager)findViewById(R.id.base_info_pager)).getCurrentItem() > 0) {
            ((ViewPager)findViewById(R.id.base_info_pager)).setCurrentItem(0);
        }else {
            finish();
        }
	}
	
	
}
