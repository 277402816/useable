package com.roiland.crm.sm.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem.DataChangeListener;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 跟踪计划详细列表Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScFollowPlanActivity.java, v 0.1 2013-6-21 下午2:03:29 shuang.gao Exp $
 */
public class ScFollowPlanActivity extends BaseInfoActivity implements BottomEventListener,DataChangeListener {
    private final static String  tag               = Log.getTag(ScFollowPlanActivity.class); //Log标志
    private boolean              bshowcustomerinfo = true;                                  //显示客户信息
    private boolean              ShowBottonBar     = true;                                  //根据是否是从销售线索进入 来控制是否显示BottomBar
    private String               projectId         = null;
    private BottomBar            mBottomBtn;                                                //底部按钮
    private TracePlan            tracePlan         = null;                                  //跟踪计划信息
    private Project              project           = null;                                  //销售线索信息
    private Customer             customer          = null;                                  //客户信息

    private ScFollowPlanFragment mainFragment      = null;                                  //跟踪计划Fragment

    public ScFollowPlanActivity() {
        super();
        mainFragment = new ScFollowPlanFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标题
        setTitle(getResources().getString(R.string.tracking_program_info_title));
        mBottomBtn = new BottomBar(this, findViewById(R.id.mybottom_bar));
        Intent intent = getIntent();
        project = intent.getExtras().getParcelable("projectinfo");
        customer = intent.getExtras().getParcelable("customerinfo");
        tracePlan = intent.getParcelableExtra("tracePlan");
        bshowcustomerinfo = intent.getBooleanExtra("ShowCustomerInfo", true);
        ShowBottonBar = intent.getBooleanExtra("ShowBottonBar", true);
        if (tracePlan != null && tracePlan.getCustomer() != null) {
            projectId = tracePlan.getCustomer().getProjectID();
        }
        if (tracePlan != null && bshowcustomerinfo != false) {
            if (StringUtils.isEmpty(projectId)) {
                mBottomBtn.setVisible(false);

            }
        }
        mBottomBtn.addVisiableBottom(BottomBar.SALES_OPPO_BOTTOM);
        mBottomBtn.setBottomEventListener(this);
        mainFragment.setTracePlan(tracePlan);
        mainFragment.setCustomer(customer);
        mainFragment.setProject(project);
        mainFragment.setProjectId(projectId);
        if ((!ShowBottonBar)) {
            mBottomBtn.setVisible(false);//从销售线索进入不显示BottomBar
        }
        addFragment(mainFragment);
    }
    
    /**
     * 监听物理键的操作
     * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("tracePlan", mainFragment.getTracePlan());
            intent.putExtras(bundle);
            this.setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * BottomBar点击事件
     * @param tabIndex BottomBar索引
     * @see com.roiland.crm.ui.widget.BottomBar.BottomEventListener#bottomTabClick(int)
     */
    @Override
    public void bottomTabClick(int tabIndex) {
        Log.d(tag, "----------- bottomTabClick-----------");
        switch (tabIndex) {
            case BottomBar.TAB_INDEX_SALES_OPPO:
                project = mainFragment.getProject();
                if (project != null) {
                    Intent intent = new Intent(ScFollowPlanActivity.this, ScOppoInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("projectinfo", project);
                    intent.putExtras(bundle);
                    intent.putExtra("CustFlowCome", true);
                    intent.putExtra("EditMode", false);
                    intent.putExtra("ShowBottonBar", false);
                    startActivity(intent);
                }
                mBottomBtn.setCurrentTab(-1);
                break;

            default:
                break;
        }
    }

    public TracePlan getTracePlan() {
        return tracePlan;
    }
    /**
     * 数据改变
     * @param key item的key值
     * @param value item的value值
     * @see com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem.DataChangeListener#dataModify(java.lang.String, java.lang.String)
     */
    @Override
    public void dataModify(String key, String value) {
        dataModify(key, value,null);
    }

    /**
     * 数据改变
     * @param key item的key值
     * @param value item的value值
     * @param pairKey item的字典code
     * @see com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem.DataChangeListener#dataModify(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void dataModify(String key, String value, String pairKey) {
        mainFragment.dataModify(key, value, pairKey);
    }
}
