package com.roiland.crm.sm.ui.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener;

/**
 * 
 * <pre>
 * 客户订单详细信息Activity
 * </pre>
 * @extends BaseInfoActivity
 * @implements BottomEventListener
 * @author liuyu
 * @version $Id: ScCustomerOrderInfoActivity.java, v 0.1 2013-7-3 上午10:21:08 liuyu Exp $
 */

public class ScCustomerOrderInfoActivity extends BaseInfoActivity implements BottomEventListener{
    public String orderId = null;
    private CustOrder customerOrder;
	private BottomBar bBar;
	private ScCustomerOrderInfoFragment orderFragment;
	
	public ScCustomerOrderInfoActivity(){
		super();
		orderFragment = new ScCustomerOrderInfoFragment(customerOrder);
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.customer_order_info_title));
		// 获得点击列表条目的客户订单等信息
		Intent intent = getIntent();
		boolean bshow = intent.getBooleanExtra("ShowBottomBar",true);
		downloadOrderInfo(getIntent().getStringExtra("ORDERID"));
		
		bBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
		bBar.addVisiableBottom(BottomBar.SALES_OPPO_BOTTOM);
		bBar.setBottomEventListener(this);
        bBar.setVisible(bshow);
        
		addFragment(orderFragment);
	}
    //点击销售线索按钮将当前客户订单信息带入销售线索
	@Override
	public void bottomTabClick(int tabIndex) {
		switch (tabIndex) {
		case BottomBar.TAB_INDEX_SALES_OPPO:
			if (orderFragment != null && orderFragment.getCustOrder() != null && orderFragment.getCustOrder().getCustomer() != null) {
				Intent oppIntent = new Intent(this, ScOppoInfoActivity.class);
				oppIntent.putExtra("EditMode", false);
				oppIntent.putExtra("ShowBottonBar", false);
				oppIntent.putExtra("CustFlowCome", true);
				oppIntent.putExtra("CustOrderCome", true);
				oppIntent.putExtra("orderId", orderFragment.getCustOrder().getOrderID());
				oppIntent.putExtra("orderStatus", orderFragment.getCustOrder().getOrderStatus());
				Bundle bundle = new Bundle();
				bundle.putParcelable("PROJECTID", orderFragment.getCustOrder().getCustomer());
				oppIntent.putExtras(bundle);
				startActivity(oppIntent);
			}
			bBar.setCurrentTab(-1);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 异步操作 调取API 获得订单详细信息
	 * </pre>
	 * 客户订单详细信息页面加载时候调用此方法
	 * @param orderID 客户订单ID
	 */
	public void downloadOrderInfo(String orderID) {
		new BaseTask<CustOrder>(this) {

			@Override
			protected CustOrder doInBackground(String... arg0) {
				CustOrder order = null;
				try {
					//获取订单详细信息
					order = ((RoilandCRMApplication)getApplication()).getCRMManager().getOrderDetailInfo(arg0[0]);
				} catch (ResponseException e) {
					responseException = e;
				}
				return order;
			}
			
			@Override
			protected void onPostExecute(CustOrder result) {
				super.onPostExecute(result);
				if (responseException != null) {
					//捕获异常提示错误信息
					Toast.makeText(getApplicationContext(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					orderFragment.setCustOrder(result);
					//初期进入不显示“更多”的信息
					orderFragment.displayOrderInfo(false);
				}
			}
			/**
			 * 取消异步操作
			 * 
			 * @see com.roiland.crm.sm.ui.view.BaseTask#onCancelled()
			 */
			@Override
			protected void onCancelled() {
				finish();
				super.onCancelled();
			}
			
			
		}.execute(orderID);
	}
}