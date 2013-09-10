package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客户信息列表ListAdapter
 * </pre>
 *
 * @author Zhao.jaiqi
 * @version $Id: SmCustomerListAdapter.java, v 0.1 2013-6-14 下午4:25:07 Zhao.jaiqi Exp $
 */
public class SmCustomerListAdapter extends SeparatedListAdapter<Customer> {

    private Context context;
	public SmCustomerListAdapter(Context context) {

		super(context, R.layout.sm_customer_list_item);
		this.context = context;
	}

	@Override
	protected View getContentView(int headerPosition, int position,
			View convertView, ViewGroup parent, Customer item) {

		View view = super.getContentView(headerPosition, position, convertView, parent, item);

		try {
			((TextView)view.findViewById(R.id.customer_list_custName)).setText(StringUtils.notNull(item.getCustName()));
			((TextView)view.findViewById(R.id.customer_list_mobile)).setText(StringUtils.notNull(item.getCustMobile()));
			((TextView)view.findViewById(R.id.customer_list_custType)).setText(StringUtils.notNull(item.getCustStatus()));
			((TextView)view.findViewById(R.id.customer_list_customer_id)).setText(StringUtils.notNull(item.getCustomerID()));
			((TextView)view.findViewById(R.id.customer_owner)).setText(StringUtils.notNull(item.getOwnerName()));
            //判断是否有有对应的跟踪计划，如果没有则加上标示
            if (!(context.getString(R.string.dormancy)).equals(item.getCustStatus()) && (("false").equals(item.getHasUnexePlan()) || item.getHasUnexePlan() == null) )
                view.findViewById(R.id.plan_state).setVisibility(View.VISIBLE);
            else
                view.findViewById(R.id.plan_state).setVisibility(View.GONE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}
}
