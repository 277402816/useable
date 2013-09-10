package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.FollowInfo;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

public class ScOppoTracePlanFragmentAdapter extends BaseCacheListAdapter<FollowInfo>{
    public ScOppoTracePlanFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        
        return super.getView(arg0, arg1, arg2, R.layout.sc_list_oppo_follow_plan_item);
    }


    /**
     * 页面添加数据
     * @param view 要添加数据的视图
     * @param item 添加的数据
     * @return 是否添加成功
     * @see com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter#fillView(android.view.View, java.lang.Object)
     */
    @Override
    protected boolean fillView(View view, FollowInfo item) {
        
            ((TextView)view.findViewById(R.id.list_oppo_action_content)).setText(StringUtils.notNull(item.getActionContent()));
            ((TextView)view.findViewById(R.id.list_oppo_action_type)).setText(StringUtils.notNull(item.getActionType()));
            ((TextView)view.findViewById(R.id.list_oppo_do_time)).setText(DateFormatUtils.formatDate(item.getDoTime()));
            ((TextView)view.findViewById(R.id.list_oppo_do_status)).setText(StringUtils.notNull(item.getDoStatus()));
            return true;
    }



}
