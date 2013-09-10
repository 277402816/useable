package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客户订单列表Adapter
 * </pre>
 * @extends SeparatedListAdapter
 * @author liuyu
 * @version $Id: ScCustomerOrderListAdapter.java, v 0.1 2013-7-29 下午1:30:40 liuyu Exp $
 */
public class ScCustomerOrderListAdapter extends SeparatedListAdapter<CustOrder> {


    Context context;
	public ScCustomerOrderListAdapter(Context context) {
		super(context, R.layout.sc_customer_order_list_item);
		this.context = context;
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
			
			((TextView)view.findViewById(R.id.customer_order_list_customer_name)).setText(StringUtils.trimNull(custName));
			((TextView)view.findViewById(R.id.customer_order_list_mobilephone)).setText(StringUtils.trimNull(custMobile));
			PurchaseCarIntention purchaseCarIntention = item.getPurchaseCarIntention();
			((TextView)view.findViewById(R.id.customer_order_list_car_model)).setText(StringUtils.notNull(purchaseCarIntention != null?purchaseCarIntention.getCarConfiguration():""));
			((TextView)view.findViewById(R.id.customer_order_list_order_status)).setText(StringUtils.notNull(item.getOrderStatus()));
			((TextView)view.findViewById(R.id.customer_order_list_order_id)).setText(StringUtils.notNull(item.getOrderID()));

		} catch (Exception e) {
		    Toast.makeText(context, new ResponseException(e).getMessage(), Toast.LENGTH_LONG).show();
		}
		return view;
	}


}