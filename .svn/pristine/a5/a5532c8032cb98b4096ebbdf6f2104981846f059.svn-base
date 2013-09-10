package com.roiland.crm.sm.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem.DataChangeListener;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 购车信息Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmPurchaseCarInfoActivity.java, v 0.1 2013-5-30 下午4:48:50 shuang.gao Exp $
 */
public class ScPurchaseCarInfoActivity extends BaseInfoActivity implements BottomEventListener,
                                                               DataChangeListener {
    private final static String       tag          = ScPurchaseCarInfoActivity.class
                                                       .getSimpleName();
    private ScPurchaseCarInfoFragment mainFragment = null;              //购车信息Fragment
    private Project                   project      = null;              //销售线索信息
    private BottomBar                 mBottomBar;
    private boolean                   addFlag;                          //添加标记

    public ScPurchaseCarInfoActivity() {
        super();
        mainFragment = new ScPurchaseCarInfoFragment();
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
        addFlag = intent.getBooleanExtra("addFlag", false);
        project = intent.getParcelableExtra("purchaseProject");
        //进入ScOppoInfoFragment
        intent.putExtra("fromCustManager", true);
        addFragment(mainFragment);
//        OppoTask task = new OppoTask(ScPurchaseCarInfoActivity.this);
//        if (!addFlag) {
//            task.execute(project.getCustomer().getProjectID(), project.getCustomer()
//                .getCustomerID());
//        } else {
//            addFragment(mainFragment);
//        }
        mBottomBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
        mBottomBar.setVisible(false);
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
    public void dataModify(String key, String value) {
        dataModify(key, value,null);
    }

    @Override
    public void dataModify(String key, String value, String pairKey) {
        mainFragment.dataModify(key, value, pairKey);
    }

}
