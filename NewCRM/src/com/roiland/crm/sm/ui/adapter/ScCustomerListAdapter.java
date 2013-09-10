package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售顾问客户管理列表Adapter
 * </pre>
 *
 * @author zhao.jiaqi
 * @version $Id: ScCustomerListAdapter.java, v 0.1 2013-6-24 上午9:40:11 zhao.jiaqi Exp $
 */
public class ScCustomerListAdapter extends SeparatedListAdapter<Customer> {
    private Context context;
	public ScCustomerListAdapter(Context context) {
		super(context, R.layout.sc_customer_list_item);
		this.context = context;
	}

	@Override
	protected View getContentView(int headerPosition, int position,
			View convertView, ViewGroup parent, Customer item) {

		View view = super.getContentView(headerPosition, position, convertView, parent, item);

		try {
			((TextView)view.findViewById(R.id.customer_list_custName)).setText(StringUtils.notNull(item.getCustName()));
			((TextView)view.findViewById(R.id.customer_list_mobile)).setText(StringUtils.notNull(item.getCustMobile()));
			((TextView)view.findViewById(R.id.customer_list_custType)).setText(StringUtils.notNull(item.getCustStatus()));//修改为显示客户状态，李春吉修改
			((TextView)view.findViewById(R.id.customer_list_customer_id)).setText(StringUtils.notNull(item.getCustomerID()));
            if (!(context.getString(R.string.dormancy)).equals(item.getCustStatus()) && (("false").equals(item.getHasUnexePlan()) || item.getHasUnexePlan() == null) )
                ((ImageView)view.findViewById(R.id.plan_state)).setVisibility(View.VISIBLE);
            else
                ((ImageView)view.findViewById(R.id.plan_state)).setVisibility(View.GONE);
//                view.findViewById(R.id.plan_state).setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}
}
