package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.StringUtils;


/**
 * 
 * <pre>
 * 销售过程管理列表Adapter适配
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmFollowPlanListAdapter.java, v 0.1 2013-5-16 下午4:59:35 shuang.gao Exp $
 */
public class SmFollowPlanListAdapter extends SeparatedListAdapter<TracePlan>{
    
    private ResponseException responseException ;
    Context context;
	public SmFollowPlanListAdapter(Context context) {
		super(context, R.layout.sm_list_follow_plan_item);
		this.context = context;
	}

	@Override
	protected View getContentView(int headerPosition, int position,
			View convertView, ViewGroup parent, TracePlan item) {
		View view = super.getContentView(headerPosition, position, convertView, parent, item);
		try {
			((TextView)view.findViewById(R.id.list_follow_name)).setText(StringUtils.notNull(item.getCustomer().getCustName()));
			((TextView)view.findViewById(R.id.list_follow_phone_num)).setText(StringUtils.notNull(item.getCustomer().getCustMobile()));
			((TextView)view.findViewById(R.id.list_follow_action)).setText(StringUtils.notNull(item.getActivityType()));
			((TextView)view.findViewById(R.id.owner)).setText(StringUtils.notNull(item.getOwerName()));
			((TextView)view.findViewById(R.id.list_follow_action_status)).setText(StringUtils.notNull(item.getExecuteStatus()));
		} catch (Exception e) {
		    responseException = new ResponseException(e);
		    Toast.makeText(context, responseException.getMessage(), Toast.LENGTH_LONG).show();
		}
		return view;
	}
}