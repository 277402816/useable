package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 购车信息Activity
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmPurchaseCarInfoActivity.java, v 0.1 2013-5-30 下午4:48:50 shuang.gao Exp $
 */
public class ScPurchaseCarInfoFragment extends SherlockFragment {
    private final static String             tag                  = Log
                                                                     .getTag(ScPurchaseCarInfoFragment.class);
    private BasicInfoListAdapter            PurchasecarInfoAdapter;
    private BasicInfoListAdapter            followPlanAdapter;
    private List<BasicInfoListAdapter.Info> purchasecarInfo;
    private List<BasicInfoListAdapter.Info> followPlanInfo;
    private Project                         project              = null;                                      //销售线索信息
    private Customer                        customer             = null;                                       //带入的客户信息
    private TracePlan                       tracePlan;                                                         //新建跟踪计划
    private LinearLayout                    mFollowPlanLayout;                                                //新建跟踪计划画面
    private LinearLayout                    mPurchasecarLayout;                                               //购车信息画面
    private LinearLayout                    mFollowPlanTitle;                                                 //跟踪计划标题
    private TextView                        title;                                                             //购车标题
    private RelativeLayout                  mButtonLayout;                                                    //跟踪计划显示按钮layout
    private ToggleButton                    mToggleButton;                                                    //显示跟踪计划按钮
    MenuItem                                item;                                                             //菜单按钮
    private boolean                         addFlag;                                                           //添加标记
    private CRMManager                      crmManager;
    private String[]                        carInfoStrList;
    private String[]                        followinfokeylist;
    private String                          newPairKey;  
    private static boolean                  newToggleBtnClick    = false;                                     // true 显示新建跟踪计划

    /**
     * 创建
     * @param savedInstanceState
     * @see com.roiland.crm.sm.ui.view.BaseInfoActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        carInfoStrList = new String[] { getString(R.string.brand_1), getString(R.string.model),
                getString(R.string.outsideColor), getString(R.string.insideColor),
                getString(R.string.carConfiguration), getString(R.string.salesQuote),
                getString(R.string.preorderCount), getString(R.string.preorderDate),
                getString(R.string.flowStatus), getString(R.string.dealPossibility) };
        followinfokeylist = new String[] { getString(R.string.activitytype),
                getString(R.string.executetime), getString(R.string.executestatus),
                getString(R.string.activitycontent), getString(R.string.contactresult),
                getString(R.string.custfeedback) };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sc_purchase_car_list, container, false);
        this.crmManager = ((RoilandCRMApplication) getActivity().getApplication()).getCRMManager();
        mPurchasecarLayout = (LinearLayout) view.findViewById(R.id.follow_allinfo_list);
        mFollowPlanLayout = (LinearLayout) view.findViewById(R.id.follow_oppoinfo_list);
        mToggleButton = (ToggleButton) view.findViewById(R.id.turn_button);
        mFollowPlanTitle = (LinearLayout) view.findViewById(R.id.plan_title_layout);
        mButtonLayout = (RelativeLayout) view.findViewById(R.id.turn_button_layout);
        title = (TextView)view.findViewById(R.id.purchase_car_title);
        mFollowPlanTitle.setVisibility(View.INVISIBLE);
        mFollowPlanLayout.setVisibility(View.INVISIBLE);
        mButtonLayout.setVisibility(View.INVISIBLE);
        Intent intent = getActivity().getIntent();
        project = intent.getParcelableExtra("purchaseProject");
        customer = intent.getParcelableExtra("customer");
        addFlag = intent.getBooleanExtra("addFlag", false);
        //请求获得焦点 使画面进入就置顶
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
        title.requestFocus();
        if (PurchasecarInfoAdapter == null) {
            PurchasecarInfoAdapter = new BasicInfoListAdapter(this.getActivity());
            PurchasecarInfoAdapter.crmManager = this.crmManager;
        }
        if (followPlanAdapter == null) {
            followPlanAdapter = new BasicInfoListAdapter(this.getActivity());
            followPlanAdapter.crmManager = this.crmManager;
        }
        //为跟踪计划开关按钮设置监听事件
        mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //根据状态设置相应控件状态
                if (isChecked) {
                    newToggleBtnClick = true;
                    mFollowPlanTitle.setVisibility(View.VISIBLE);
                    mFollowPlanLayout.setVisibility(View.VISIBLE);
                    setNewListdisplay();
                    mToggleButton.setBackgroundResource(R.drawable.toggle_sliding2);
                } else {
                    newToggleBtnClick = false;
                    mFollowPlanTitle.setVisibility(View.GONE);
                    mFollowPlanLayout.setVisibility(View.GONE);
                    setNewListHidden();
                    mToggleButton.setBackgroundResource(R.drawable.toggle_sliding1);
                }
            }

        });
        if (addFlag) {
            displayCarInfo();
            mFollowPlanTitle.setVisibility(View.VISIBLE);
            mFollowPlanLayout.setVisibility(View.VISIBLE);
            mButtonLayout.setVisibility(View.VISIBLE);
            displayfollowPlanInfo();
            followPlanAdapter.setEditable(true);
            newToggleBtnClick = true;
        }else{
            searchTask();
        }
        return view;

    }
    /**
     * 
     * <pre>
     * 显示购车信息
     * </pre>
     *
     * @param isDetail 是否显示更多
     */
    public void displayCarInfo() {
        
        if (purchasecarInfo == null){
            purchasecarInfo = new ArrayList<BasicInfoListAdapter.Info>();
        }
        if(project == null){
            project = new Project();
            project.setPurchaseCarIntention(new PurchaseCarIntention());
        }
            purchasecarInfo.clear();
            if(addFlag){
                purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.brand_1), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getBrand() : null),true));
                purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),true));
            }else{
                purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.brand_1), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getBrand() : null),true,false));
                purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.model), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getModel() : null),true,false));
            }
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.outsideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getOutsideColor() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColor), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getInsideColor() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.insideColorCheck), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? String
                .valueOf(project.getPurchaseCarIntention().isInsideColorCheck()) : "false"),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.carConfiguration), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                .getPurchaseCarIntention().getCarConfiguration() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.salesQuote), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention().getSalesQuote() : null),false,InputType.TYPE_CLASS_NUMBER));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.dealPriceInterval), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                .getPurchaseCarIntention().getDealPriceInterval() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.payment), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPayment() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderCount), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPreorderCount() : "1"),false,InputType.TYPE_CLASS_NUMBER));
            if(project != null && project.getPurchaseCarIntention() != null){
                if(project.getPurchaseCarIntention().getFlowStatus() != null && 
                        getString(R.string.flowStatus_1).equals(project.getPurchaseCarIntention().getFlowStatus())){
                    purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.finish_preorderDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true));
                }else if(project.getPurchaseCarIntention().isGiveupTag() !=null && project.getPurchaseCarIntention().isGiveupTag()){
                    purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.lose_date), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true));
                }else{
                    purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null
                            && project.getPurchaseCarIntention().getPreorderDate() != 0 ? String.valueOf(project.getPurchaseCarIntention().getPreorderDate()) : null)),true));
                }
            }
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.flowStatus), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getFlowStatus() : "1"),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.dealPossibility), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getDealPossibility() : "0.05"),false,false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.purchMotivation), BaseInfoRowViewItem.SELECTION_TYPE,null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPurchMotivation() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.chassisNo), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getChassisNo() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.engineNo), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getEngineNo() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.licensePlate), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getLicensePlate() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.licenseProp), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getLicenseProp() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.pickupDate), BaseInfoRowViewItem.DATE_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? 
                ("0".equals(project.getPurchaseCarIntention().getPickupDate()) 
                        || StringUtils.isEmpty(project.getPurchaseCarIntention().getPickupDate()) 
                        ? null: project.getPurchaseCarIntention().getPickupDate()) : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.preorderTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getPreorderTag() : null),false));
            //新建时不能放弃销售机会
            if(addFlag){
                purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? String
                    .valueOf(project.getPurchaseCarIntention().isGiveupTag()) : "false"),false,false));
                purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupReason), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                        .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                            .getGiveupReason() : null),false,false));   
            }else{
                purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupTag), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? String
                    .valueOf(project.getPurchaseCarIntention().isGiveupTag()) : "false"),false));
                if(project != null && project.getPurchaseCarIntention() != null && project.getPurchaseCarIntention().isGiveupTag()){
                    purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupReason), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getGiveupReason() : null),true)); 
                }else{
                    purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.giveupReason), BaseInfoRowViewItem.SELECTION_TYPE, null, ((project != null && project
                            .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                                .getGiveupReason() : null),false,false)); 
                }
                
            }
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.invoiceTitle), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project
                    .getPurchaseCarIntention() != null) ? project.getPurchaseCarIntention()
                        .getInvoiceTitle() : null),false));
            purchasecarInfo.add(new BasicInfoListAdapter.Info(getString(R.string.comment), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, ((project != null && project.getPurchaseCarIntention() != null) ? project
                .getPurchaseCarIntention().getProjectComment() : null),false));

        PurchasecarInfoAdapter.setContentList(purchasecarInfo);
        PurchasecarInfoAdapter.notifyDataSetChanged();
        refreshCarList();
        if(addFlag){
            PurchasecarInfoAdapter.setEditable(true);
        }
}
    /**
     * 
     * <pre>
     * 刷新购车信息
     * </pre>
     *
     */
    private void refreshCarList() {
        if (mPurchasecarLayout != null && PurchasecarInfoAdapter != null) {
            mPurchasecarLayout.removeAllViews();
            for (int i = 0; i < PurchasecarInfoAdapter.getCount(); i++) {
                mPurchasecarLayout.addView(PurchasecarInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mPurchasecarLayout.addView(dividerView);
            }
            if(project != null && project.getPurchaseCarIntention() != null && !StringUtils.isEmpty(project.getPurchaseCarIntention().getBrand())){
                for(int i=1;i<5;i++){
                    BaseInfoRowViewItem item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(carInfoStrList[i]);
                    if(item != null)
                        item.setParentKey(project.getPurchaseCarIntention().getBrandCode());
                    
                }
            }
            if(project != null && project.getPurchaseCarIntention() != null && !StringUtils.isEmpty(project.getPurchaseCarIntention().getModel())){
                BaseInfoRowViewItem item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(carInfoStrList[4]);
                if(item != null)
                    item.setParentKey2(project.getPurchaseCarIntention().getModelCode());
            }
        }
    }
    /**
     * 
     * <pre>
     * 跟踪计划信息部分 加载页面信息
     * </pre>
     *
     */
    public void displayfollowPlanInfo() {
        if (followPlanInfo == null) {
            followPlanInfo = new ArrayList<BasicInfoListAdapter.Info>();
        }
        followPlanInfo.clear();
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[0],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getActivityType() != null ? tracePlan.getActivityType()
                : ""), true, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[1],
            BaseInfoRowViewItem.DATE_TYPE, null, (tracePlan != null ? (String.valueOf(tracePlan
                .getExecuteTime() != 0 ? tracePlan.getExecuteTime() : DateFormatUtils
                .getSystemDate())) : String.valueOf(DateFormatUtils.getSystemDate())), true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[2],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getExecuteStatus() != null ? tracePlan
                .getExecuteStatus() : getResources().getString(R.string.executestatus_1)), true,
            false));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[3],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (tracePlan != null && tracePlan.getActivityContent() != null ? tracePlan
                .getActivityContent() : ""), true, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[4],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getContactResult() != null ? tracePlan
                .getContactResult().trim() : ""), false, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(followinfokeylist[5],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (tracePlan != null && tracePlan.getCustFeedback() != null ? tracePlan.getCustFeedback()
                : ""), false, true));

        followPlanAdapter.setContentList(followPlanInfo);
        followPlanAdapter.notifyDataSetChanged();
        refreshFollowInfoList();
    }

    /**
     * 
     * <pre>
     * 更新跟踪计划方法，把页面上的数据更新到原始list中
     * </pre>
     *
     */
    private void refreshFollowInfoList() {
        if (mFollowPlanLayout != null && followPlanAdapter != null) {
            mFollowPlanLayout.removeAllViews();
            for (int i = 0; i < followPlanAdapter.getCount(); i++) {
                mFollowPlanLayout.addView(followPlanAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mFollowPlanLayout.addView(dividerView);
            }
        }
    }
    
    public void searchTask(){
        OppoTask task = new OppoTask(getActivity());
        task.execute(project.getCustomer().getProjectID(),project.getCustomer().getCustomerID());
    }
    
    /**
     * 
     * <pre>
     * 获得Project创建购车信息
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: ScPurchaseCarListFragment.java, v 0.1 2013-7-16 上午9:36:30 shuang.gao Exp $
     */
    private class OppoTask extends BaseTask<Project> {
        public OppoTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Project doInBackground(String... params) {
            Project lp = null;
            try {
                RoilandCRMApplication application = (RoilandCRMApplication) activity
                    .getApplication();
                CRMManager manager = application.getCRMManager();
                Log.i(tag, "getProjectID:" + params[0] + "   getCustomerID:" + params[1]);
                lp = manager.getProjectInfo(params[0], params[1]);
                lp.setProjectID(params[0]);
                lp.getCustomer().setProjectID(params[0]);
            } catch (ResponseException e) {
                Log.e(tag, "Get project list failure.", e);
                responseException = e;
            }
            return lp;
        }

        @Override
        protected void onPostExecute(Project result) {
            super.onPostExecute(result);
            Log.i(tag, "onPostExecute  => " + result);
            if (responseException != null) {
                Toast.makeText(getActivity(), responseException.getMessage(),
                    Toast.LENGTH_SHORT).show();
                return;
            }
           if(result != null){
               project = result;
               displayCarInfo();
           }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    public void dataModify(String key, String value, String pairKey) {

        Log.i(tag, "-----"+key+"-----"+value+"-----"+pairKey+"-----");
        BaseInfoRowViewItem item;
        
        /** 品牌，车型，内饰颜色，外室颜色，配置联动 **/
        //品牌
        if(getString(R.string.brand_1).equals(key)){
            for(int i=1;i<5;i++){
                item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(carInfoStrList[i]);
                if(item != null){
                    item.setValue("");
                    item.setParentKey(pairKey);
                    item.setParentKey2(null);
                    if(project != null && project.getPurchaseCarIntention() != null){
                        project.getPurchaseCarIntention().setModel("");
                        project.getPurchaseCarIntention().setModelCode("");
                        project.getPurchaseCarIntention().setOutsideColor("");
                        project.getPurchaseCarIntention().setOutsideColorCode("");
                        project.getPurchaseCarIntention().setInsideColor("");
                        project.getPurchaseCarIntention().setInsideColorCode("");
                        project.getPurchaseCarIntention().setCarConfiguration("");
                        project.getPurchaseCarIntention().setCarConfigurationCode("");
                    }
                    for(BasicInfoListAdapter.Info info : purchasecarInfo){
                        for(int j=1;j<5;j++){
                            if(carInfoStrList[j].equals(info.key)){
                                info.value = "";
                                info.pairKey = "";
                                break;
                            }
                        }
                        
                    }
                }
            }
        }
        
        //车型
        if(getString(R.string.model).equals(key)){
            item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(carInfoStrList[4]);
            if(item != null){
                item.setValue("");
                item.setParentKey2(pairKey);
                project.getPurchaseCarIntention().setCarConfiguration("");
                project.getPurchaseCarIntention().setCarConfigurationCode("");
            }
            for(BasicInfoListAdapter.Info info : purchasecarInfo){
                if(carInfoStrList[4].equals(info.key)){
                    info.value = "";
                    info.pairKey = "";
                    break;
                }
            }
        }
        
        //监听流程状态改变
        if(getString(R.string.flowStatus).equals(key) ){
            //流程状态为签订单
            if(getString(R.string.flowStatus_4).equals(value)){
                if(StringUtils.isEmpty(project.getPurchaseCarIntention().getBrand()) ||
                        StringUtils.isEmpty(project.getPurchaseCarIntention().getModel()) ||
                        StringUtils.isEmpty(project.getPurchaseCarIntention().getOutsideColor()) ||
                        StringUtils.isEmpty(project.getPurchaseCarIntention().getInsideColor()) ||
                        StringUtils.isEmpty(project.getPurchaseCarIntention().getCarConfiguration()) ||
                        project == null){
                    Toast.makeText(getActivity(), getString(R.string.dataverify_hasorder), Toast.LENGTH_LONG).show();
                    item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.flowStatus));
                    item.setValue(project.getPurchaseCarIntention().getFlowStatus());
                    return ;
                }
            }
            
            //流程状态为递交新车
            if(getString(R.string.flowStatus_1).equals(value)){
                String errString = null;
                errString = DataVerify.checkOrder(project.getPurchaseCarIntention().getOrderStatus(),project.getPurchaseCarIntention().isHasActiveOrder());
                if(!StringUtils.isEmpty(errString)){
                    Toast.makeText(getActivity(), errString, Toast.LENGTH_SHORT).show();
                    item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.flowStatus));
                    if (item != null) {
                        item.setValue(project.getPurchaseCarIntention().getFlowStatus());
                    }
                    return;
                }else{
                    item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.preorderDate));
                    item.setKey(getString(R.string.finish_preorderDate));
                    item.setEditable(false);
                    item.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                    for(BasicInfoListAdapter.Info info : purchasecarInfo){
                        if(getString(R.string.preorderDate).equals(info.key)){
                            info.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                        }
                        
                    }
                    for(int i=0;i<5;i++){
                        if(i != 3){
                            item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(carInfoStrList[i]);
                            item.setEditable(false);
                        }
                        
                    }
                    return;
                }
            }else{
                
            }
            
            item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.dealPossibility));
            newPairKey = pairKey;
            getDicDealPossibility(item, true);
        }
        
        //放弃销售机会相应操作
        if(getString(R.string.giveupTag).equals(key)){
            if("true".equals(value)){
                item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.giveupReason));
                if(item != null){
                    for(BasicInfoListAdapter.Info info : purchasecarInfo){
                        if(getString(R.string.giveupReason).equals(info.key)){
                            info.required = true;
                        }
                    }
                    item.setEditable(true);
                    item.setRequired(true);
                    item.btnDown.setEnabled(true);
                    item.txtValue.setEnabled(true);
                    item.setRedColor();
                }
               
                item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.preorderDate));
                if(item != null){
                    item.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                    item.setKey(getString(R.string.lose_date));
                    item.setEditable(false);
                    for(BasicInfoListAdapter.Info info : purchasecarInfo){
                        if(getString(R.string.preorderDate).equals(info.key)){
                            info.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                        }
                    }  
                }
                
                item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.flowStatus));
                item.setEditable(false);
                
                DialogUtils.confirm(getActivity(), "", getString(R.string.dormancy_warning), 
                    new Runnable() {
                        
                        @Override
                        public void run() {
                            if (project == null) {
                                project = new Project();
                                project.setCustomer(new Customer());
                                project.setPurchaseCarIntention(new PurchaseCarIntention());
                            }
                            project.getCustomer().setDormancy(true);
                        }
                    }, null);
            }else{
                item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.giveupReason));
                item.btnDown.setEnabled(false);
                item.txtValue.setEnabled(false);
                item.setValue("");
                item.setBlackColor();
                
                item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.preorderDate)); 
                if(item == null){
                    item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.finish_preorderDate));
                }
                if(item == null){
                    item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.lose_date));
                }
                item.btnDown.setEnabled(true);
                item.txtValue.setEnabled(true);
                item.setKey(getString(R.string.preorderDate));
                
                item = (BaseInfoRowViewItem)mPurchasecarLayout.findViewWithTag(getString(R.string.flowStatus));
                item.btnDown.setEnabled(true);
                item.txtValue.setEnabled(true);
            }
            
        }
    }
    /**
     * 
     * <pre>
     * 根据流程状态设置成交可能性，异步线程调用API
     * </pre>
     *
     * @param item 传入列表item
     * @param flag
     */
    public void getDicDealPossibility(final BaseInfoRowViewItem item,final Boolean flag){
        new BaseTask<List<Dictionary>>(this.getActivity()) {
            @Override
            protected List<Dictionary> doInBackground(String... arg0) {
                List<Dictionary> resultList = null;
                TextView textView =new TextView(activity);
                textView.setText(carInfoStrList[9]);
                if (crmManager == null)
                    crmManager = ((RoilandCRMApplication)((Activity) activity).getApplication()).getCRMManager();
                try {
                    resultList = crmManager.getDictionariesByType(BaseInfoRowViewItem.getDicName(textView));
                } catch (ResponseException e) {
                    responseException = e;
                }
                return resultList;
            }
            @Override
            protected void onPostExecute(List<Dictionary> result) {
                super.onPostExecute(result);
                Log.i(tag, "saveNewProjectInfo: "+String.valueOf(result));

                if (responseException != null) {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    for(int k=0;k<result.size();k++){
                        if(flag){
                            if(result.get(k).getDicKey().equals(newPairKey)){
                                if (item != null)
                                    item.setValue(result.get(k).getDicValue());
                                project.getPurchaseCarIntention().setDealPossibility(result.get(k).getDicValue());
                            }
                        }else{
                            if (item != null)
                                item.setValue("");  
                        }
                    }
                }
            }
        }.execute();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        if (!addFlag) {
            item = menu.findItem(R.id.edit);//显示状态下按钮显示编辑
            item.setVisible(true);
        } else {
            item = menu.findItem(R.id.save);//添加状态下按钮显示保存
            item.setVisible(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(tag, "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.edit:
                PurchasecarInfoAdapter.setEditable(!PurchasecarInfoAdapter.getEditable());
                showEdit(item);
                break;
            case R.id.save:
                showEdit(item);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEdit(MenuItem item) {
        if (!addFlag) {
            if (PurchasecarInfoAdapter.getEditable()) {
                item.setIcon(R.drawable.save_selector);
            } else {
                String purchaseCarMessage = carInfoValidation();
                if (!StringUtils.isEmpty(purchaseCarMessage)) {
                    Toast.makeText(getActivity(), purchaseCarMessage, Toast.LENGTH_SHORT).show();
                    PurchasecarInfoAdapter.setEditable(true);
                    return;
                }
                saveProjectInfo();
                item.setIcon(R.drawable.edit_btn_selector);
            }
        } else {
            String purchaseCarMessage = carInfoValidation();
            if (!StringUtils.isEmpty(purchaseCarMessage)) {
                Toast.makeText(getActivity(), purchaseCarMessage, Toast.LENGTH_SHORT).show();
                return;
            }
            if (newToggleBtnClick) {
                String followMessage = followInfoValidation();
                if (!StringUtils.isEmpty(followMessage)) {
                    Toast.makeText(getActivity(), followMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            saveNewProjectInfo();
        }
    }

    /**
     * 
     * <pre>
     * 编辑更新购车信息，调用API
     * </pre>
     *
     */
    public void saveProjectInfo() {

        new BaseTask<Boolean>(this.getActivity()) {
            @Override
            protected Boolean doInBackground(String... arg0) {
                Boolean b = false;
                try {
                    PurchaseCarIntention p = getUpdatedPurchaseCar();
                    //获取销售线索中默认流程状态对应的code.
                    if (StringUtils.isEmpty(p.getFlowStatusCode())) {
                        List<Dictionary> dicArray = crmManager
                            .getDictionariesByType("dicFlowStatus");
                        for (int i = 0; i < dicArray.size(); i++) {
                            if (dicArray.get(i).getDicValue().equals(p.getFlowStatus())) {
                                p.setFlowStatus(dicArray.get(i).getDicValue());
                                p.setFlowStatusCode(dicArray.get(i).getDicKey());
                                break;
                            }
                        }
                    }
                    //获取放弃销售原因对应code
                    if (StringUtils.isEmpty(p.getGiveupReasonCode())) {
                        List<Dictionary> dicArray = crmManager
                            .getDictionariesByType("dicGiveupReason");
                        for (int i = 0; i < dicArray.size(); i++) {
                            if (dicArray.get(i).getDicValue().equals(p.getGiveupReason())) {
                                p.setGiveupReason(dicArray.get(i).getDicValue());
                                p.setGiveupReasonCode(dicArray.get(i).getDicKey());
                                break;
                            }
                        }
                    }
                    project.setPurchaseCarIntention(p);
                    b = crmManager.updateProjectInfo(project);
                } catch (ResponseException e) {
                    responseException = e;
                }

                return b;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                Log.e(tag, "UpdateProjectInfo: " + String.valueOf(result));
                if(responseException != null){
                    Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if (result != null && result.booleanValue()) {
                    DialogUtils.alert(activity, getString(R.string.purchase_car_info_title),
                        getString(R.string.opt_success), new Runnable() {
                            @Override
                            public void run() {
                                getActivity().finish();
                            }
                        }).show();
                } else {
                    Toast.makeText(activity, responseException.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    /**
     * 
     * <pre>
     * 新建购车信息，调用API
     * </pre>
     *
     */
    public void saveNewProjectInfo() {
        new BaseTask<Boolean>(this.getActivity()) {
            @Override
            protected Boolean doInBackground(String... arg0) {
                Boolean b = false;
                try {
                    PurchaseCarIntention p = getUpdatedPurchaseCar();
                    TracePlan t = null;
                    if (mToggleButton.isChecked()) {
                        t = getUpdatedTracePlan();
                        customer.setUpdateCustInfo(true); //true:更新现有客户信息， false:添加新客户信息
                        t.setCustomer(customer);
                        //获取跟踪计划中默认执行状态对应的code.
                        if (StringUtils.isEmpty(t.getExecuteStatusCode())) {
                            List<Dictionary> dicArray = crmManager
                                .getDictionariesByType("dicExecuteStatus");
                            for (int i = 0; i < dicArray.size(); i++) {
                                if (getResources().getString(R.string.executestatus_1).equals(
                                    dicArray.get(i).getDicValue())) {
                                    t.setExecuteStatus(dicArray.get(i).getDicValue());
                                    t.setExecuteStatusCode(dicArray.get(i).getDicKey());
                                    break;
                                }
                            }
                        }
                    }
                    Project project = new Project();
                    project.setPurchaseCarIntention(p);
                    project.setCustomer(customer);
                    //获取销售线索中默认流程状态对应的code.
                    if (StringUtils.isEmpty(p.getFlowStatusCode())) {
                        List<Dictionary> dicArray = crmManager
                            .getDictionariesByType("dicFlowStatus");
                        for (int i = 0; i < dicArray.size(); i++) {
                            if (dicArray.get(i).getDicValue().equals(p.getFlowStatus())) {
                                p.setFlowStatus(dicArray.get(i).getDicValue());
                                p.setFlowStatusCode(dicArray.get(i).getDicKey());
                                break;
                            }
                        }
                    }

                    b = crmManager.createProject(project, t);
                    if (b) {
                        String projectId = project.getCustomer().getProjectID();
                        project = crmManager.getProjectInfo(project.getCustomer().getProjectID(),
                            project.getCustomer().getCustomerID());
                        project.setProjectID(projectId);
                        project.getCustomer().setProjectID(projectId);
                    }
                } catch (ResponseException e) {
                    responseException = e;
                    Log.e(tag, "create project failure.", e);
                }

                return b;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                Log.i(tag, "saveNewProjectInfo: " + String.valueOf(result));
                if (result.booleanValue()) {
                    DialogUtils.alert(activity, "提示", getString(R.string.opt_success),
                        new Runnable() {
                            @Override
                            public void run() {
                                refreshCarList();
                                getActivity().finish();
                            }
                        });

                } else {
                    DialogUtils.alert(activity, getString(R.string.oppotunity_info),
                        responseException.getMessage(), null);
                    PurchasecarInfoAdapter.setEditable(true);
                    if (mToggleButton.isChecked()) {
                        followPlanAdapter.setEditable(true);
                    }
                }

            }
        }.execute();
    }

    /**
     * 
     * <pre>
     * 显示新建跟踪计划
     * </pre>
     *
     */
    private void setNewListdisplay() {
        ViewGroup.LayoutParams params = mFollowPlanLayout.getLayoutParams();
        params.height = params.MATCH_PARENT;
        mFollowPlanLayout.setLayoutParams(params);
    }

    /**
     * 
     * <pre>
     * 隐藏新建跟踪计划
     * </pre>
     *
     */
    private void setNewListHidden() {
        ViewGroup.LayoutParams params = mFollowPlanLayout.getLayoutParams();
        params.height = 0;
        mFollowPlanLayout.setLayoutParams(params);
    }

    /**
     * 
     * <pre>
     * 对project赋值
     * </pre>
     * 在执行页面跳转时候条用此方法
     * @param p 销售线索
     */
    public void setProject(Project p) {
        project = p;
    }

    /**
     * 获得最新客户购车信息
     * @return result 客户购车信息
     */
    public PurchaseCarIntention getUpdatedPurchaseCar() {
        PurchaseCarIntention result = new PurchaseCarIntention();
        if (project != null && project.getPurchaseCarIntention() != null) {
            result = project.getPurchaseCarIntention();
        }
        for (BasicInfoListAdapter.Info info : purchasecarInfo) {
            if (info.key.equals(getString(R.string.brand_1)) && info.pairKey != null) {
                result.setBrand(info.value);
                result.setBrandCode(info.pairKey);
            } else if (info.key.equals(getString(R.string.model)) && info.pairKey != null) {
                result.setModel(info.value);
                result.setModelCode(info.pairKey);
            } else if (info.key.equals(getString(R.string.outsideColor)) && info.pairKey != null) {
                result.setOutsideColor(info.value);
                result.setOutsideColorCode(info.pairKey);
            } else if (info.key.equals(getString(R.string.insideColor)) && info.pairKey != null) {
                result.setInsideColorCode(info.pairKey);
                result.setInsideColor(info.value);
            } else if (info.key.equals(getString(R.string.insideColorCheck))) {
                result.setInsideColorCheck(Boolean.parseBoolean(info.value));
            } else if (info.key.equals(getString(R.string.carConfiguration))
                       && info.pairKey != null) {
                result.setCarConfigurationCode(info.pairKey);
                result.setCarConfiguration(info.value);
            }else if(info.key.equals(getString(R.string.dealPriceInterval))){
                if(info.pairKey != null){
                    result.setDealPriceIntervalCode(info.pairKey);
                }
                result.setDealPriceInterval(info.value);
                
            }else if(info.key.equals(getString(R.string.payment))){
                if(info.pairKey != null){
                    result.setPaymentCode(info.pairKey);
                }
                result.setPayment(info.value);
            }else if (info.key.equals(getString(R.string.salesQuote))) {
                result.setSalesQuote((info.value));
            } else if (info.key.equals(getString(R.string.preorderCount)))
                result.setPreorderCount(info.value);
            else if (info.key.equals(getString(R.string.preorderDate))) {
                result.setPreorderDate(StringUtils.getDateTrimNullLong(info.value));
            } else if (info.key.equals(getString(R.string.flowStatus)) && info.pairKey != null) {
                result.setFlowStatus(info.value);
                result.setFlowStatusCode(info.pairKey);
            } else if (info.key.equals(getString(R.string.dealPossibility))) {
                result.setDealPossibility(info.value);
            } else if (info.key.equals(getString(R.string.purchMotivation)) && info.pairKey != null) {
                result.setPurchMotivation(info.value);
                result.setPurchMotivationCode(info.pairKey);
            } else if (info.key.equals(getString(R.string.chassisNo))) {
                result.setChassisNo(info.value);
            } else if (info.key.equals(getString(R.string.engineNo))) {
                result.setEngineNo(info.value);
            } else if (info.key.equals(getString(R.string.licensePlate))) {
                result.setLicensePlate(info.value);
            } else if (info.key.equals(getString(R.string.licenseProp)) && info.pairKey != null) {
                result.setLicenseProp(info.value);
                result.setLicensePropCode(info.pairKey);
            } else if (info.key.equals(getString(R.string.pickupDate)))
                result.setPickupDate(info.value);
            else if (info.key.equals(getString(R.string.preorderTag)))
                result.setPreorderTag(info.value);
            else if (info.key.equals(getString(R.string.giveupTag))) {
                result.setGiveupTag(("true").equals(info.value));
            } else if (info.key.equals(getString(R.string.giveupReason)) && info.pairKey != null) {
                result.setGiveupReasonCode(info.pairKey);
                result.setGiveupReason(info.value);
            } else if (info.key.equals(getString(R.string.invoiceTitle)))
                result.setInvoiceTitle(info.value);
            else if (info.key.equals(getString(R.string.comment))) {
                result.setProjectComment(info.value);
            }
        }

        return result;
    }

    /**
     * 
     * <pre>
     * 获得最新跟踪计划详细信息
     * </pre>
     *
     * @return result 跟踪计划信息
     */
    public TracePlan getUpdatedTracePlan() {
        TracePlan result = new TracePlan();
        if (tracePlan != null) {
            result = tracePlan;
        }

        for (BasicInfoListAdapter.Info info : followPlanInfo) {
            if (info.key.equals(followinfokeylist[0]) && info.pairKey != null) {
                result.setActivityTypeCode(info.pairKey);
                result.setActivityType(info.value);
            } else if (info.key.equals(followinfokeylist[1])) {
                result.setExecuteTime(StringUtils.getDateTrimNullLong(info.value));
            } else if (info.key.equals(followinfokeylist[2]) && info.pairKey != null) {
                result.setExecuteStatusCode(info.pairKey);
                result.setExecuteStatus(info.value);
            } else if (info.key.equals(followinfokeylist[3])) {
                result.setActivityContent(info.value);
            } else if (info.key.equals(followinfokeylist[4])) {
                result.setContactResultCode(info.pairKey);
                result.setContactResult(info.value);
            } else if (info.key.equals(followinfokeylist[5])) {
                result.setCustFeedback(info.value);
            }
        }
        return result;
    }

    /**
     * 
     * <pre>
     * 跟踪计划校验
     * </pre>
     *
     * @return errString 提示信息
     */
    private String followInfoValidation() {
        String errString = null;
        for (int i = 0; i < followPlanInfo.size(); i++) {
            String item = followPlanInfo.get(i).key;
            errString = DataVerify.infoValidation(item, followPlanInfo.get(i).value, null, null,
                null, null, getActivity(), false, false);
            if (!StringUtils.isEmpty(errString)) {
                break;
            }
        }
        return errString;
    }

    /**
     * 购车信息校验
     * @return errString 提示信息
     */
    private String carInfoValidation() {
        String errString = null;
        String errString2 = null;
        String strTemp = null;
        boolean isGiveUpChecked = getIsGiveUpChecked();
        for (int i = 0; i < purchasecarInfo.size(); i++) {
            String item = purchasecarInfo.get(i).key;
            String value = purchasecarInfo.get(i).value;
            if (item.contains(getString(R.string.giveupTag))) {
                strTemp = purchasecarInfo.get(i).value;
            }
            //Long型日期转成“yyyy-MM-dd”型
            if (item.contains(getString(R.string.preorderDate))
                || item.contains(getString(R.string.finish_preorderDate))
                || item.contains(getString(R.string.pickupDate))) {
                value = DateFormatUtils.formatDate(purchasecarInfo.get(i).value);
            }
          //添加车型必填
            if(addFlag){
                if(getString(R.string.cust_model).equals(item)){
                    if(StringUtils.isEmpty(value)){
                        errString =  getString(R.string.dataverify_bemust);
                        break;
                    }
                }     
            }
            errString = DataVerify.infoValidation(item, value, null, null,
                strTemp, null, getActivity(), isGiveUpChecked, false);
            if (getString(R.string.orderStatus_finish).equals(errString)) {
                for (int k = 0; k < purchasecarInfo.size(); k++) {

                    String items = purchasecarInfo.get(k).key;
                    String values = purchasecarInfo.get(k).value;
                    String orderStatus = getString(R.string.orderStatus_finish);
                    errString2 = DataVerify.infoValidationCars(items, values, orderStatus);
                    if (!StringUtils.isEmpty(errString2)
                        && errString.equals(getString(R.string.orderStatus_finish))) {
                        break;
                    }
                }
            }
            if (!StringUtils.isEmpty(errString2)) {
                errString = errString2;
                break;
            } else if (!StringUtils.isEmpty(errString)
                       && !getString(R.string.orderStatus_finish).equals(errString)) {
                break;
            }

        }
        return errString;
    }

    private boolean getIsGiveUpChecked() {
        return false;
    }
}
