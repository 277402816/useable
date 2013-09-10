package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info.DataChangeListener;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener;

/**
 * 
 * <pre>
 * 购车信息Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmPurchaseCarInfoActivity.java, v 0.1 2013-5-30 下午4:48:50 shuang.gao Exp $
 */
public class SmPurchaseCarInfoActivity extends BaseInfoActivity implements BottomEventListener,
                                                               DataChangeListener {
    private SmOppoInfoFragment mainFragment = null; //购车信息Fragment
    private Project            project      = null; //销售线索信息
    private BottomBar          mBottomBar;

    public SmPurchaseCarInfoActivity() {
        super();
        mainFragment = new SmOppoInfoFragment();
    }

    @Override
    public void bottomTabClick(int tabIndex) {
    }

    /**
     * 创建
     * @param savedInstanceState
     * @see com.roiland.crm.sm.ui.view.BaseInfoActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.purchase_car_info_title));
        Intent intent = getIntent();
        project = intent.getParcelableExtra("purchaseProject");
        //进入SmOppoInfoFragment
        intent.putExtra("fromCustManager", true);
        if (project != null) {
            mainFragment.setProject(project);
        }
        mBottomBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
        mBottomBar.setVisible(false);
        addFragment(mainFragment);
    }

    /**
     * 物理返回键
     * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void chagedData(String key, String value) {
    }

}
