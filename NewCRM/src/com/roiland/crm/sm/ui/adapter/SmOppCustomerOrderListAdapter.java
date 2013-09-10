package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

public class SmOppCustomerOrderListAdapter extends BaseCacheListAdapter<CustOrder> {

	public SmOppCustomerOrderListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent, R.layout.sm_opp_customer_order_list_item);
	}

	@Override
	protected boolean fillView(View view, CustOrder item) {
		// TODO Auto-generated method stub
		try {
			((TextView)view.findViewById(R.id.customer_order_list_customer_name)).setText(item.getOrderID());
			((TextView)view.findViewById(R.id.customer_order_list_mobilephone)).setText(DateFormatUtils.formatDate(item.getCreateTime()));
			((TextView)view.findViewById(R.id.customer_order_list_car_model)).setText(StringUtils.notNull(item.getMatchingChassisNo()));
			((TextView)view.findViewById(R.id.customer_order_list_order_status)).setText(item.getOrderStatus());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
