package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售线索列表adapter
 * </pre>
 * @extends SeparatedListAdapter
 * @author liuyu
 * @version $Id: ProjectListAdapter.java, v 0.1 2013-5-17 上午11:41:31 liuyu Exp $
 */
public class ScProjectListAdapter extends SeparatedListAdapter<Project> {

    public ScProjectListAdapter(Context context) {
        super(context, R.layout.sc_list_complex);
    }
    
    protected View getContentView(int headerPosition, int position,
            View convertView, ViewGroup parent, Project item) {
        View view = super.getContentView(headerPosition, position, convertView, parent, item);
        try {
            ((TextView) view.findViewById(R.id.list_complex_name))
                    .setText(StringUtils.notNull(item.getCustomer().getCustName()));
            ((TextView) view.findViewById(R.id.list_complex_phone_num))
                    .setText(StringUtils.notNull(item.getCustomer().getCustMobile()));
            ((TextView) view.findViewById(R.id.list_complex_car_brand))
                    .setText(StringUtils.notNull(item.getPurchaseCarIntention().getBrand()));
            ((TextView) view.findViewById(R.id.list_complex_car_model))
                    .setText(StringUtils.notNull(item.getPurchaseCarIntention().getModel()));
            ((TextView) view.findViewById(R.id.list_complex_flow_status))
                    .setText(StringUtils.notNull(item.getPurchaseCarIntention().getFlowStatus()));
            //判断是否有有对应的跟踪计划，如果没有则加上标示
            if (("false").equals(item.getCustomer().getHasUnexePlan()) || item.getCustomer().getHasUnexePlan() == null)
                view.findViewById(R.id.plan_state).setVisibility(View.VISIBLE);
            else
                view.findViewById(R.id.plan_state).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
