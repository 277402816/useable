package com.roiland.crm.sm.ui.view;

import android.os.Bundle;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 客户选择列表Activity
 * </pre>
 * extends BaseInfoActivity
 * @author liuyu
 * @version $Id: ScCustomerInfoListActivity.java, v 0.1 2013-7-4 下午2:19:36 liuyu Exp $
 */
public class ScCustomerInfoListActivity extends BaseInfoActivity{

    private static String tag = ScCustomerInfoListActivity.class.getName();
    private BottomBar mBottomBar ;
    ScCustomerInfoListFragment mFragment;
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Log.i(tag, "---------onCreat--------");
        setTitle(getResources().getString(R.string.customer_select));
        mFragment = new ScCustomerInfoListFragment();
        addFragment(mFragment);
        mBottomBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
        mBottomBar.setVisible(false);
        
    }
    
}
