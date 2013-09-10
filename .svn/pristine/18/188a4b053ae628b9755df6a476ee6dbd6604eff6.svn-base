package com.roiland.crm.sm.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 客戶管理详细信息Activity
 * </pre>
 * @extends BaseInfoActivity
 * @implements BottomEventListener
 * @author liuyu
 * @version $Id: SmCustomerInfoActivity.java, v 0.1 2013-5-21 上午9:57:17 liuyu Exp $
 */
public class SmCustomerInfoActivity extends BaseInfoActivity implements
		BottomEventListener {
	SmCustomerInfoFragment mainFragment;
	SmPurchaseCarListFragment purchaseCarListFragment;
	SmOppoFollowPlanFragment followplanfragment;
	int contacterPage = -1;
	int puchaseCarPage = -1;
	int attachPage = -1;
	int followInfoPage = -1;
	public CRMManager crmManager;
	public Customer editCustomer;
	private Customer customerInfo;
	private Bundle bundle;

	public SmCustomerInfoActivity() {
		super();
	}
	

	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	              if (resultCode == 4) {
	            	 Boolean flag = data.getBooleanExtra("doSave",false);
	            	 bundle = new Bundle();
	            	 bundle.putBoolean("doSave", flag);
	        }
	    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	       switch (item.getItemId()) {
           case android.R.id.home:
               if (((ViewPager)findViewById(R.id.base_info_pager))!=null&&((ViewPager)findViewById(R.id.base_info_pager)).getCurrentItem() > 0) {
                   ((ViewPager)findViewById(R.id.base_info_pager)).setCurrentItem(0);
               }else {
            	   if(bundle!=null){

                	   Intent intent = new Intent();
                	   intent.putExtras(bundle);
                	   this.setResult(6, intent);
            	   }
                   finish();
               }
               break;
           default:
               break;
	       }
		return super.onOptionsItemSelected(item);
	}



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置标题
		setTitle(getResources().getString(R.string.customer_info_title));
		crmManager = ((RoilandCRMApplication) getApplication()).getCRMManager();
		final BottomBar bbar = new BottomBar(this, findViewById(R.id.mybottom_bar));
		bbar.addVisiableBottom(BottomBar.PURCHASE_CAR_BOTTOM);
		bbar.addVisiableBottom(BottomBar.TRACE_PLAN_BOTTOM);
		bbar.setBottomEventListener(this);
		
		//ViewPager转换监听事件
		((ViewPager) findViewById(R.id.base_info_pager))
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						if (arg0 == 0) {
								bbar.setVisible(true);
							bbar.setCurrentTab(-1);
							setTitle(getResources().getString(
									R.string.customer_info_title));
						} else if (arg0 == followInfoPage) {
							bbar.setCurrentTab(BottomBar.TAB_INDEX_TRACEPLAN);
							setTitle(getResources().getString(
									R.string.traceplan_tab));
							bbar.setVisible(true);
						}else if(arg0==puchaseCarPage){
							bbar.setCurrentTab(BottomBar.TAB_INDEX_PURCHASE_CAR);
							setTitle(getResources().getString(
									R.string.purchase_car_info_title));
							bbar.setVisible(true);
						}
					}

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }
				});
		
		if (mainFragment == null)
            mainFragment = new SmCustomerInfoFragment();
		downloadCustomerInfo(getIntent().getStringExtra("CUSTOMERID"));
		
	}
	/**
	 * responseData 返回时将Customer传回
	 */
	public void responseData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("editcustomerinfo", editCustomer);
		intent.putExtras(bundle);
		setResult(Activity.RESULT_OK, intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * 监听bottomTab单击事件
	 * @param tabIndex BottomBar索引
	 * @see com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener#bottomTabClick(int)
	 */
	@Override
	public void bottomTabClick(int tabIndex) {
		if (customerInfo == null) return;
		
		switch (tabIndex) {
		//点击了购车信息
		case BottomBar.TAB_INDEX_PURCHASE_CAR:
			onPurchaseClick();
			if (purchaseCarListFragment == null) {
				purchaseCarListFragment = new SmPurchaseCarListFragment(customerInfo);
				addFragment(purchaseCarListFragment);
				puchaseCarPage = ((ViewPager) findViewById(R.id.base_info_pager))
						.getCurrentItem();
			}
			else {
				((ViewPager) findViewById(R.id.base_info_pager))
				.setCurrentItem(puchaseCarPage);
			}
			break;
		//点击了跟踪计划
		case BottomBar.TAB_INDEX_TRACEPLAN:
			onTracePlanClick();
			if (followplanfragment == null) {
				followplanfragment = new SmOppoFollowPlanFragment();
				followplanfragment.transCustomer(mainFragment.getCustomer(),true);
				addFragment(followplanfragment);
				followInfoPage = ((ViewPager) findViewById(R.id.base_info_pager))
						.getCurrentItem();
			} else {
				((ViewPager) findViewById(R.id.base_info_pager))
						.setCurrentItem(followInfoPage);
			}
			break;
		}
	}
	/**
	 * 
	 * <pre>
	 * 点击购车信息后更改名称图标等
	 * </pre>
	 * 客户管理bottombar点购车信息之后调用此方法
	 */
	private void onPurchaseClick() {
		setTitle(getResources().getString(R.string.purchase_car_info_title));
	}
	/**
	 * 
	 * <pre>
	 * 点击跟踪计划后更改名称图标等
	 * </pre>
	 * 客户管理bottombar点跟踪计划之后调用此方法
	 */
	private void onTracePlanClick() {
		setTitle(getResources().getString(R.string.traceplan_tab));
	}
	
	/**
	 * 
	 * <pre>
	 * 异步操作 调取API 获得当前获取客户详细信息列表
	 * </pre>
	 * 页面跳转到客户详细信息页面之后调用此方法，通过客户id来获取客户详细信息并显示
	 * @param customerID 客户ID
	 */
	public void downloadCustomerInfo(String customerID) {
		new BaseTask<Customer>(this) {

			@Override
			protected Customer doInBackground(String... arg0) {
				Customer order = null;
				try {
					order = ((RoilandCRMApplication) getApplication())
							.getCRMManager().getCustomerDetail(arg0[0]);
				} catch (ResponseException e) {
					responseException = e;
				}
				return order;
			}

			@Override
			protected void onPostExecute(Customer result) {
				super.onPostExecute(result);
				//捕获异常提示异常信息
				if (responseException != null) {
					Toast.makeText(getApplicationContext(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
					finish();
				} else {
				    if (mainFragment == null)
			            mainFragment = new SmCustomerInfoFragment();
                    addFragment(mainFragment);
					customerInfo = result;
					mainFragment.setCustomer(result);
					//显示列表
					mainFragment.displayCustomerInfo(false);
				}
			}
			//取消异步
			@Override
			protected void onCancelled() {
				finish();
				super.onCancelled();
			}
		}.execute(customerID);
	}
}