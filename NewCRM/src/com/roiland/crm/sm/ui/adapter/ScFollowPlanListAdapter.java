package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售顾问跟踪计划列表Adapter适配
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScFollowPlanListAdapter.java, v 0.1 2013-6-24 上午8:55:38 shuang.gao Exp $
 */
public class ScFollowPlanListAdapter extends SeparatedListAdapter<TracePlan>{
    public ScFollowPlanListAdapter(Context context) {
        super(context, R.layout.sm_list_follow_plan_item);
    }

    @Override
    protected View getContentView(int headerPosition, int position,
            View convertView, ViewGroup parent, TracePlan item) {
        View view = super.getContentView(headerPosition, position, convertView, parent, item);
        try {
            ((TextView)view.findViewById(R.id.list_follow_name)).setText(StringUtils.notNull(item.getCustomer().getCustName()));
            ((TextView)view.findViewById(R.id.list_follow_phone_num)).setText(StringUtils.notNull(item.getCustomer().getCustMobile()));
            ((TextView)view.findViewById(R.id.list_follow_action)).setText(StringUtils.notNull(item.getActivityType()));
            ((TextView)view.findViewById(R.id.list_follow_action_status)).setText(StringUtils.notNull(item.getExecuteStatus()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}