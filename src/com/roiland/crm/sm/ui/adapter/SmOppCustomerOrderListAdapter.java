package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售线索下客户订单列表Adapter
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmOppCustomerOrderListAdapter.java, v 0.1 2013-8-6 上午9:30:19 shuang.gao Exp $
 */
public class SmOppCustomerOrderListAdapter extends BaseCacheListAdapter<CustOrder> {

	public SmOppCustomerOrderListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent, R.layout.sm_opp_customer_order_list_item);
	}

	@Override
	protected boolean fillView(View view, CustOrder item) {
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
