package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScOppCustomerOrderListAdapter;

/**
 * 
 * <pre>
 * 线索下客户订单列表
 * </pre>
 * extends BaseListFragment
 * @author liuyu
 * @version $Id: ScSalesCustomerOrderListFragment.java, v 0.1 2013-7-3 上午8:38:43 liuyu Exp $
 */
public class ScSalesCustomerOrderListFragment extends BaseListFragment{
	List<CustOrder> customerOrderList; 
	private String mProjectID;
	private String mCustomerID;
	
	public ScSalesCustomerOrderListFragment()
	{
		super();
	}
	
	public ScSalesCustomerOrderListFragment(String prjID, String custID) {
		super();
		mCustomerID = custID;
		mProjectID = prjID;
	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize() throws Throwable {
		super.finalize();
	}

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        this.getListView().setBackgroundResource(R.drawable.list_item_background);
        customerOrderList = new ArrayList<CustOrder>();
        displayList();
		
        //从API中获取客户订单列表
		new BaseTask<List<CustOrder>>(getActivity(), "", true) {

			@Override
			protected List<CustOrder> doInBackground(String... params) {
				CRMManager manager = mApplication.getCRMManager();
				
				try {
					customerOrderList = manager.getOrderList(mProjectID,mCustomerID,null, null, null, null);
				} catch (ResponseException e) {
					responseException = e;
					customerOrderList = new ArrayList<CustOrder>();
				} catch (Exception e) {
					e.printStackTrace();
					customerOrderList = new ArrayList<CustOrder>();
				}
				if (customerOrderList == null) {
					customerOrderList = new ArrayList<CustOrder>();
				}
				return customerOrderList;
			}
			
			@Override
			protected void onPostExecute(List<CustOrder> result) {
				super.onPostExecute(result);
				if (responseException != null) {
					if (getActivity() != null) {
						Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} else {
					displayList();
				}
			}
		}.execute();

		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	public void displayList() {
		ScOppCustomerOrderListAdapter adapter = new ScOppCustomerOrderListAdapter(this.getActivity());
		adapter.setCaches(customerOrderList);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), ScCustomerOrderInfoActivity.class);
		Bundle bundle = new Bundle();
		intent.putExtra("ShowBottomBar", false);
		String strOrderID = customerOrderList.get(position).getOrderID();
		intent.putExtra("ORDERID", strOrderID);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
