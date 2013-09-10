package com.roiland.crm.sm.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
import com.roiland.crm.sm.utils.StringUtils;



/**
 * 
 * <pre>
 * 销售线索详细信息Activity
 * </pre>
 * @extends BaseInfoActivity
 * @implements BottomEventListener ,DataChangeListener
 * @author liuyu
 * @version $Id: ScOppoInfoActivity.java, v 0.1 2013-7-1 下午4:54:30 liuyu Exp $
 */

public class ScOppoInfoActivity extends BaseInfoActivity implements BottomEventListener,DataChangeListener{

    public boolean                           isNeedUpdateList   = false;
    public boolean                           isNewFlowPlan      = false;
    private boolean                          custFlowCome       = false;                              // true 从跟踪计划进入
    private boolean                          custOrderCome       = false;                              // true 从客户订单进入
    private boolean                          bshow              = true;
    private boolean                          bSelectProjectInfo = false;
    private boolean                          isOldCust          = false;                               //老客户信息带入
    private boolean 						 oldCust            = false;
    private final static String              tag                = Log
                                                                    .getTag(ScOppoInfoActivity.class);

    private ScContacterListFragment          contacterListFragment;
    private ScTestDriveListFragment          testdrivefragment;
    private ScOppoTracePlanFragment          followplanfragment;
    private ScAttachListFragment             attachListFragment;
    private ScSalesCustomerOrderListFragment orderListFragment;
    public BottomBar                        mBottomBar;
    private ScOppoInfoFragment               mainFragment       = null;
    Project                                  project            = new Project();
    Customer customer = new Customer();
    String                                   orderId            = "";
    String                                   orderStatus        = "";
    int                                      contacterPage      = -1;
    int                                      followInfoPage     = -1;
    int                                      attachPage         = -1;
    int                                      testdrivePage      = -1;
    int                                      orderPage          = -1;

    public ScOppoInfoActivity() {
        super();
        mainFragment = new ScOppoInfoFragment();
    }

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            customer = data.getParcelableExtra("customer");
            isOldCust = data.getBooleanExtra("isOldCust", false);
            oldCust = data.getBooleanExtra("oldCust", false);
            mainFragment.setOldCust(isOldCust);
            mainFragment.setOldeCustCollect(oldCust);
            mainFragment.displayCustomer(customer);
        }
    }
    
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收传递过来的Intent数据
        Intent intent = getIntent();
        custFlowCome = intent.getBooleanExtra("CustFlowCome", false);
        custOrderCome = intent.getBooleanExtra("CustOrderCome", false);
        setTitle(getResources().getString(R.string.oppotunity_info));
        //设置BottomBar控件
        if (!custFlowCome) {
            mBottomBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
            mBottomBar.addVisiableBottom(BottomBar.CONTACTER_BOTTOM);
            mBottomBar.addVisiableBottom(BottomBar.DRIVE_TEST_BOTTOM);
            mBottomBar.addVisiableBottom(BottomBar.SALES_ORDER_BOTTOM);
            mBottomBar.addVisiableBottom(BottomBar.TRACE_PLAN_BOTTOM);
            mBottomBar.addVisiableBottom(BottomBar.ATTACHMENT_BOTTOM);
           
            //监听BottomBar的单击事件
            mBottomBar.setBottomEventListener(this);
        } else {
            //从跟踪计划进入销售线索显示
            mBottomBar = new BottomBar(this, findViewById(R.id.mybottom_bar));
        }
        mainFragment.sendBottomBar(mBottomBar);
        //接收传递过来的Intent数据
        bshow = intent.getBooleanExtra("ShowBottonBar", true);
        if (!custFlowCome) {
            mBottomBar.setVisible(bshow);
        }else if(custOrderCome){
            mBottomBar.setVisible(bshow);  
        } else {
            //从跟踪计划进入不显示
            mBottomBar.setVisible(false);
        }
        customer = intent.getParcelableExtra("PROJECTID");
        project = intent.getParcelableExtra("projectinfo");
        isNeedUpdateList = intent.getBooleanExtra("IsNeedUpdateList", false);
        
        /**
         * 根据点击BottomBar设置页面UI
         */
        ((ViewPager) findViewById(R.id.base_info_pager))
            .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {

                    if (arg0 == 0) {
                        mBottomBar.setCurrentTab(-1);
                        setTitle(getResources().getString(R.string.oppotunity_info));
                        if (custFlowCome) {
                            mBottomBar.setVisible(true);
                        }
                    } else if (arg0 == contacterPage) {
                        mBottomBar.setCurrentTab(BottomBar.TAB_INDEX_CONTACTER);
                        setTitle(getResources().getString(R.string.contacter_tab));
                        mBottomBar.setVisible(true);
                    } else if (arg0 == testdrivePage) {
                        mBottomBar.setCurrentTab(BottomBar.TAB_INDEX_DRIVETEST);
                        setTitle(getResources().getString(R.string.drivetest_tab));
                        mBottomBar.setVisible(true);
                    } else if (arg0 == attachPage) {
                        mBottomBar.setCurrentTab(BottomBar.TAB_INDEX_ATTACHMENT);
                        setTitle(getResources().getString(R.string.attachment_tab));
                        mBottomBar.setVisible(true);
                    } else if (arg0 == followInfoPage) {
                        mBottomBar.setCurrentTab(BottomBar.TAB_INDEX_TRACEPLAN);
                        setTitle(getResources().getString(R.string.traceplan_tab));
                        if (custFlowCome) {
                            mBottomBar.setVisible(false);
                        } else {
                            mBottomBar.setVisible(true);
                        }
                    } else if (arg0 == orderPage) {
                        mBottomBar.setCurrentTab(BottomBar.TAB_INDEX_SALESORDER);
                        setTitle(getResources().getString(R.string.customer_order_tab));
                        mBottomBar.setVisible(true);
                    }
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }

            });
        
        
        //根据状态判断调用异步线程操作
        if (project != null) {
            OppoTask task = new OppoTask(this);
            task.execute(project.getCustomer().getProjectID(), project.getCustomer()
                .getCustomerID());
        } else if (customer != null && customer.getProjectID() != null) {
            OppoTask task = new OppoTask(this);
            task.execute(customer.getProjectID(), customer.getCustomerID());
        } else {
            addFragment(mainFragment);
        }
        //接收传递过来的Intent数据
        orderId = intent.getStringExtra("orderId");
        orderStatus = intent.getStringExtra("orderStatus");
       

    }

    /**
     * @see com.roiland.crm.sm.ui.view.BaseInfoActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    /**
     *  BottomBar点击事件
     * @param tabIndex
     * @see com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener#bottomTabClick(int)
     */
    public void bottomTabClick(int tabIndex) {

        Log.i(tag, "bottomTabClick：  " + String.valueOf(tabIndex));
        switch (tabIndex) {
            case 2:
                onContacterClick();
                if (contacterListFragment == null) {
                    contacterListFragment = new ScContacterListFragment(project.getCustomer()
                        .getProjectID(),project.getCustomer().getCustomerID(),"fromProject");
                    addFragment(contacterListFragment);
                    contacterPage = ((ViewPager) findViewById(R.id.base_info_pager))
                        .getCurrentItem();
                } else {
                    ((ViewPager) findViewById(R.id.base_info_pager)).setCurrentItem(contacterPage);
                }
                break;
            case 3:
                Log.i(tag, "BottomBar.DRIVE_TEST_BOTTOM");
                onDriveTestClick();
                if (testdrivefragment == null) {
                    testdrivefragment = new ScTestDriveListFragment();
                    testdrivefragment.transferProjectId(project);
                    addFragment(testdrivefragment);
                    testdrivePage = ((ViewPager) findViewById(R.id.base_info_pager))
                        .getCurrentItem();
                } else {
                    ((ViewPager) findViewById(R.id.base_info_pager)).setCurrentItem(testdrivePage);
                }
                break;
            case 4:
                onCustomerOrderClick();
                if (orderListFragment == null) {
                    orderListFragment = new ScSalesCustomerOrderListFragment(project.getCustomer()
                        .getProjectID(), project.getCustomer().getCustomerID());
                    addFragment(orderListFragment);
                    orderPage = ((ViewPager) findViewById(R.id.base_info_pager)).getCurrentItem();
                } else {
                    ((ViewPager) findViewById(R.id.base_info_pager)).setCurrentItem(orderPage);
                }
                break;
            case 5:
                onFollowPlanClick();
                if (custFlowCome) {
                    mBottomBar.setVisible(false);
                }
                if (followplanfragment == null) {
                    followplanfragment = new ScOppoTracePlanFragment();
                    if(project != null){
                        followplanfragment.transCustomer(project.getCustomer());
                        followplanfragment.transProjectID(project.getProjectID());
                    }else if(customer != null){
                        followplanfragment.transCustomer(customer);
                    }
                    addFragment(followplanfragment);
                    followInfoPage = ((ViewPager) findViewById(R.id.base_info_pager))
                        .getCurrentItem();
                } else {
                    ((ViewPager) findViewById(R.id.base_info_pager)).setCurrentItem(followInfoPage);
                }
                break;
            case 6:
                onAttachmentClick();

                if (attachListFragment == null) {
                    attachListFragment = new ScAttachListFragment(project.getCustomer()
                        .getProjectID(), project.getCustomer().getCustomerID());
                    addFragment(attachListFragment);
                    attachPage = ((ViewPager) findViewById(R.id.base_info_pager)).getCurrentItem();
                } else {
                    ((ViewPager) findViewById(R.id.base_info_pager)).setCurrentItem(attachPage);
                }
                break;
        }
    }

    private void onContacterClick() {
        setTitle(getResources().getString(R.string.contacter_info_title));
    }

    private void onAttachmentClick() {
        setTitle(getResources().getString(R.string.attachment_list_title));
    }

    private void onDriveTestClick() {
        setTitle(getResources().getString(R.string.drivetest_tab));
    }

    private void onFollowPlanClick() {
        setTitle(getResources().getString(R.string.traceplan_tab));
    }

    private void onCustomerOrderClick() {
        setTitle(getResources().getString(R.string.customer_order_tab));
    }

    /**
     * 判断物理按键
     * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (((ViewPager) findViewById(R.id.base_info_pager)).getCurrentItem() > 0) {
                ((ViewPager) findViewById(R.id.base_info_pager)).setCurrentItem(0);
                setTitle(getResources().getString(R.string.oppotunity_info));
            } else {
                new BaseTask<Project>(ScOppoInfoActivity.this) {

                    @Override
                    protected Project doInBackground(String... params) {

                        try {
                            project = ((RoilandCRMApplication) ScOppoInfoActivity.this
                                .getApplication()).getCRMManager().getProjectInfo(
                                project.getCustomer().getProjectID(),
                                project.getCustomer().getCustomerID());
                            return project;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @SuppressWarnings("static-access")
                    @Override
                    protected void onPostExecute(Project result) {

                        if (project != null) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("oldprojectinfo", project);
                            intent.putExtras(bundle);
                            ScOppoInfoActivity.this.setResult(activity.RESULT_OK, intent);
                        }
                        finish();
                        super.onPostExecute(result);
                    }

                }.execute();
                return true;
            }
        }
        return true;
    }

    /**
     * 异步线程操作，获得销售线索详细信息
     * @extends BaseTask
     */
    private class OppoTask extends BaseTask<Project> {
        String projectId;
        String customerId;

        public OppoTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Project doInBackground(String... params) {
            Project project = null;
            try {
                RoilandCRMApplication application = (RoilandCRMApplication) activity
                    .getApplication();
                CRMManager manager = application.getCRMManager();
                Log.i("FollowPlanActivity", "getProjectID:" + params[0] + "   getCustomerID:"
                                            + params[1]);
                projectId = params[0];
                customerId = params[1];
                project = manager.getProjectInfo(params[0], params[1]);
                //如果存在有效订单 带入底盘号和发动机号
                Project mHasProjectActive = manager.isProjectActive(projectId);
                if(mHasProjectActive != null){
                    mainFragment.setmHasProjectActive(mHasProjectActive);
                }
            } catch (ResponseException e) {
                Log.e(tag, "Get project list failure.", e);
                responseException = e;
            }
            return project;
        }

        @Override
        protected void onPostExecute(Project result) {
            super.onPostExecute(result);
            Log.i(tag, "onPostExecute  => " + result);
            if (responseException != null) {
                Toast.makeText(getApplicationContext(), responseException.getMessage(),
                    Toast.LENGTH_SHORT).show();
                finish();//出错的情况下关闭本页面
            } else {
                if (result != null) {
                    if (bSelectProjectInfo) {
                        bSelectProjectInfo = false;
                    } else {
                        if (projectId != null) {
                            result.getCustomer().setProjectID(projectId);
                        }
                        if (customerId != null) {
                            result.getCustomer().setCustomerID(customerId);
                        }
                        mainFragment.setProject(result);
                        addFragment(mainFragment);
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            finish();
        }

    }

    @Override
    public void dataModify(String key, String value) {
        dataModify(key, value, null);
    }


    @Override
    public void dataModify(String key, String value, String pairKey) {
        mainFragment.dataModify(key, value, pairKey);
    }

}