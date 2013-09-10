package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客户订单列表Adapter
 * </pre>
 *
 * @author Bonsa
 * @version $Id: SmCustomerOrderListAdapter.java, v 0.1 2013-8-6 上午9:27:51 Bonsa Exp $
 */
public class SmCustomerOrderListAdapter extends SeparatedListAdapter<CustOrder> {
	public SmCustomerOrderListAdapter(Context context) {
		super(context, R.layout.sm_customer_order_list_item);
	}

	@Override
	protected View getContentView(int headerPosition, int position,
			View convertView, ViewGroup parent, CustOrder item) {
		View view = super.getContentView(headerPosition, position, convertView, parent, item);
		try {
			String custName = "";
			String custMobile = "";
			
			if (item.getCustomer() != null) {
				if (item.getCustomer().getCustName() != null) custName = item.getCustomer().getCustName();
				if (item.getCustomer().getCustMobile() != null) custMobile = item.getCustomer().getCustMobile();
			}
			
			((TextView)view.findViewById(R.id.customer_order_list_customer_name)).setText(custName);
			((TextView)view.findViewById(R.id.customer_order_list_mobilephone)).setText(custMobile);
			PurchaseCarIntention purchaseCarIntention = item.getPurchaseCarIntention();
			((TextView)view.findViewById(R.id.customer_order_list_car_model)).setText(StringUtils.notNull(purchaseCarIntention != null?purchaseCarIntention.getCarConfiguration():""));
			((TextView)view.findViewById(R.id.customer_order_list_order_status)).setText(StringUtils.notNull(item.getOrderStatus()));
			((TextView)view.findViewById(R.id.customer_order_list_order_id)).setText(StringUtils.notNull(item.getOrderID()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}