package com.roiland.crm.sm.ui.view;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.widget.BottomBar;

import android.os.Bundle;


/**
 * 
 * <pre>
 * 分配销售顾问列表Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmCustomerOwenrListActivity.java, v 0.1 2013-6-2 上午11:51:31 shuang.gao Exp $
 */
public class SmCustomerOwenrListActivity extends BaseInfoActivity{
    SmCustomerOwenrListFragment  mainFragment = null;
    private BottomBar                        mBottomBar;
    public SmCustomerOwenrListActivity() {
        super();
        mainFragment = new SmCustomerOwenrListFragment();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        addFragment(mainFragment);
        setTitle(getString(R.string.customer_owner));
        mBottomBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
        mBottomBar.setVisible(false);
    }

}
