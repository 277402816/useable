package com.roiland.crm.sm.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem.DataChangeListener;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.EventReceiver;

/**
 * 
 * <pre>
 * 客戶管理详细信息Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScCustomerInfoActivity.java, v 0.1 2013-7-16 上午9:27:56 shuang.gao Exp $
 */
public class ScCustomerInfoActivity extends BaseInfoActivity implements
        BottomEventListener,DataChangeListener {
    private static final String tag = ScCustomerInfoActivity.class.getSimpleName();
    ScCustomerInfoFragment mainFragment;
    ScPurchaseCarListFragment purchaseCarListFragment;
    ScContacterListFragment contacterListFragment;
    ScOppoTracePlanFragment followplanfragment;
    ScAttachListFragment attachListFragment;
    int contacterPage = -1;
    int puchaseCarPage = -1;
    int attachPage = -1;
    int followInfoPage = -1;
    public CRMManager crmManager;
    public Customer editCustomer;
    private Customer customerInfo;
    private BottomBar bbar;
    private boolean isUpSuccess = false;//true更新成功
    public ScCustomerInfoActivity() {
        super();
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标题
        setTitle(getResources().getString(R.string.customer_info_title));
        crmManager = ((RoilandCRMApplication) getApplication()).getCRMManager();
        bbar = new BottomBar(this, findViewById(R.id.mybottom_bar));
        bbar.addVisiableBottom(BottomBar.PURCHASE_CAR_BOTTOM);
        bbar.addVisiableBottom(BottomBar.CONTACTER_BOTTOM);
        bbar.addVisiableBottom(BottomBar.TRACE_PLAN_BOTTOM);
        bbar.addVisiableBottom(BottomBar.ATTACHMENT_BOTTOM);
        bbar.setBottomEventListener(this);
        
        //ViewPager转换监听事件
        ((ViewPager) findViewById(R.id.base_info_pager))
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                        if (arg0 == 0) {
                            if (mainFragment.getCustomerInfoAdapter()
                                    .getEditable()) {
                                bbar.setVisible(false);
                            } else {
                                bbar.setVisible(true);
                            }
                            bbar.setCurrentTab(-1);
                            setTitle(getResources().getString(
                                    R.string.customer_info_title));
                        } else if (arg0 == contacterPage) {
                            bbar.setCurrentTab(BottomBar.TAB_INDEX_CONTACTER);
                            setTitle(getResources().getString(
                                    R.string.contacter_tab));
                            bbar.setVisible(true);
                            
                        } else if (arg0 == followInfoPage) {
                            bbar.setCurrentTab(BottomBar.TAB_INDEX_TRACEPLAN);
                            setTitle(getResources().getString(
                                    R.string.traceplan_tab));
                            bbar.setVisible(true);
                        }else if(arg0==puchaseCarPage){
                            bbar.setCurrentTab(BottomBar.TAB_INDEX_PURCHASE_CAR);
                            setTitle(getResources().getString(
                                    R.string.purchase_car_info_title));
                            bbar.setVisible(true);
                        }else if(arg0 == attachPage){
                            bbar.setCurrentTab(BottomBar.TAB_INDEX_ATTACHMENT);
                            setTitle(getResources().getString(
                                    R.string.attachment_tab));
                            bbar.setVisible(true);
                        }
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });
        
        if (mainFragment == null)
            mainFragment = new ScCustomerInfoFragment();
        mainFragment.crmManager = this.crmManager;
        mainFragment.setBbar(bbar);
        downloadCustomerInfo(getIntent().getStringExtra("CUSTOMERID"));
    }

    /**
     * 
     * <pre>
     * 返回时将Customer传回
     * </pre>
     *
     */
    public void responseData() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("editcustomerinfo", editCustomer);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
    }
    
    /**
     * 
     * <pre>
     * 保存客户信息方法
     * </pre>
     *
     */
    public void saveCustomer(){
        String errString = null;
        //网络连接异常 提示错误信息
        if (EventReceiver.isNetworkUnavailable()) {
            Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            mainFragment.setError(true);
            return;
        } 
        //保存校验
        errString = mainFragment.customInfoValidation();
        //校验未通过
        if (errString != null) {
            mainFragment.setError(true);
            Toast.makeText(getApplicationContext(), errString,
                    Toast.LENGTH_SHORT).show();
            return;
        }else { 
            mainFragment.setError(false);
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                //输入法窗口隐藏
                imm.hideSoftInputFromWindow(findViewById(R.id.customer_info_list).getWindowToken(), 0);
            }
            //异步操作 更新客户管理信息
            new BaseTask<Boolean>(ScCustomerInfoActivity.this) {
                @Override
                protected Boolean doInBackground(String... arg0) {
                    Boolean b = false;

                  try {
                      editCustomer = mainFragment.getUpdatedCustomer();
                      b = crmManager.updateCustomer(editCustomer);
                  } catch (ResponseException e) {
                      responseException = e;
                  }

                    return b;
                }
                
                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    //捕获异常提示错误信息
                    if (responseException != null) {
                        Toast.makeText(getApplicationContext(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
                        mainFragment.setError(true);
                        mainFragment.editFlag = true;
                        mainFragment.setItemIcon(true);
                    } else {
                        if(result){
                          //保存成功时提示
                            mainFragment.getCustomerInfoAdapter().setEditable(false);
                            //这个用dialog提示是正确的
                            DialogUtils.alert(activity, getString(R.string.customer_list_info), getString(R.string.opt_success),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        //将Customer返回客户管理列表
                                        responseData();
                                        activity.finish();
                                    }
                                });
                            bbar.setVisible(true);
                        }
                    }
                }
            }.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    
    /**
     * bottomTab监听事件
     * @param tabIndex 点击了那个bottomTab
     * @see com.roiland.crm.sm.ui.widget.BottomBar.BottomEventListener#bottomTabClick(int)
     */
    @Override
    public void bottomTabClick(int tabIndex) {
        if (customerInfo == null) return;
        
        switch (tabIndex) {
        //点击了购车信息
        case BottomBar.TAB_INDEX_PURCHASE_CAR:
            onPurchaseClick();
            if (purchaseCarListFragment == null) {
                purchaseCarListFragment = new ScPurchaseCarListFragment(customerInfo);
                addFragment(purchaseCarListFragment);
                puchaseCarPage = ((ViewPager) findViewById(R.id.base_info_pager))
                        .getCurrentItem();
            }
            else {
                ((ViewPager) findViewById(R.id.base_info_pager))
                .setCurrentItem(puchaseCarPage);
            }
            break;
        //点击了联系人
        case BottomBar.TAB_INDEX_CONTACTER:
            onContacterClick();
            if (contacterListFragment == null) {
                contacterListFragment = new ScContacterListFragment(mainFragment.getCustomer().getProjectID(), mainFragment.getCustomer().getCustomerID(),"fromCustomer");
                addFragment(contacterListFragment);
                contacterPage = ((ViewPager) findViewById(R.id.base_info_pager))
                        .getCurrentItem();
            } else {
                ((ViewPager) findViewById(R.id.base_info_pager))
                        .setCurrentItem(contacterPage);
            }
            break;
        //点击了跟踪计划
        case BottomBar.TAB_INDEX_TRACEPLAN:
            onTracePlanClick();
            if (followplanfragment == null) {
                followplanfragment = new ScOppoTracePlanFragment();
                followplanfragment.transCustomer(mainFragment.getCustomer());
                addFragment(followplanfragment);
                followInfoPage = ((ViewPager) findViewById(R.id.base_info_pager))
                        .getCurrentItem();
            } else {
                ((ViewPager) findViewById(R.id.base_info_pager))
                        .setCurrentItem(followInfoPage);
            }
            break;
        //点击了文档图片
        case BottomBar.TAB_INDEX_ATTACHMENT:
            onAttachmentClick();
            if (attachListFragment == null) {
                attachListFragment = new ScAttachListFragment(null, mainFragment.getCustomer().getCustomerID());
                addFragment(attachListFragment);
                attachPage = ((ViewPager) findViewById(R.id.base_info_pager))
                        .getCurrentItem();
            } else {
                ((ViewPager) findViewById(R.id.base_info_pager))
                        .setCurrentItem(attachPage);
            }
            break;
        }
    }
    
    /**
     * 点击购车信息后更改名称 图标等
     */
    private void onPurchaseClick() {
        setTitle(getResources().getString(R.string.purchase_car_info_title));
    }
    /**
     * 点击联系人后更改名称 图标等
     */
    private void onContacterClick() {
        setTitle(getResources().getString(R.string.contacter_info_title));
    }
    /**
     * 点击跟踪计划后更改名称 图标等
     */
    private void onTracePlanClick() {
        setTitle(getResources().getString(R.string.traceplan_tab));
    }
    /**
     * 点击文档图片后更改名称 图标等
     */
    private void onAttachmentClick() {
        setTitle(getResources().getString(R.string.attachment_list_title));
    }
    /**
     * 异步操作 调取API 获得当前获取客户详细信息列表
     * @param customerID 客户ID
     */
    public void downloadCustomerInfo(String customerID) {
        new BaseTask<Customer>(this) {

            @Override
            protected Customer doInBackground(String... arg0) {
                Customer order = null;
                try {
                    order = ((RoilandCRMApplication) getApplication())
                            .getCRMManager().getCustomerDetail(arg0[0]);
                } catch (ResponseException e) {
                    responseException = e;
                }
                return order;
            }

            @Override
            protected void onPostExecute(Customer result) {
                super.onPostExecute(result);
                //捕获异常提示异常信息
                if (responseException != null) {
                    Toast.makeText(getApplicationContext(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    addFragment(mainFragment);
                    customerInfo = result;
                    mainFragment.getDataState = true;
                    mainFragment.setCustomer(result);
                    //显示列表
                    mainFragment.displayCustomerInfo(false);
                }
            }
            //取消异步
            @Override
            protected void onCancelled() {
                finish();
                super.onCancelled();
            }
        }.execute(customerID);
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