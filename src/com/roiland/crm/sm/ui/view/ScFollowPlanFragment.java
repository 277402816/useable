package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
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
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.EventReceiver;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 跟踪计划详细信息Fragment
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScFollowPlanFragment.java, v 0.1 2013-6-21 下午2:40:20 shuang.gao Exp $
 */
public class ScFollowPlanFragment extends SherlockFragment{
    private String                          tag               = Log
                                                                  .getTag(ScFollowPlanFragment.class); //log标志
    private List<BasicInfoListAdapter.Info> followPlanInfo;                                           //跟踪计划部分
    private List<BasicInfoListAdapter.Info> oppoInfo;                                                 //线索信息部分
    private List<BasicInfoListAdapter.Info> newFollowPlanInfo;                                        //新建跟踪计划信息部分
    private BasicInfoListAdapter            followPlanInfoAdapter;                                    //跟踪计划适配
    private BasicInfoListAdapter            oppoInfoAdapter;                                          //跟踪计划适配
    private BasicInfoListAdapter            newFollowPlanInfoAdapter;                                 //新建跟踪计划适配
    private LinearLayout                    mFollowListView;                                          //跟踪计划部分
    private LinearLayout                    mNewFollowListView;                                       //显示新建跟踪计划信息部分
    private LinearLayout                    mOInfoListView;                                           //显示销售线索信息内容
    private LinearLayout                    oinfolayout;                                              //销售线索信息标题部分
    private LinearLayout                    newlayout;                                                //新建跟踪计划标题部分
    public MenuItem                         item;                                                     //编辑菜单
    private boolean                  bshowcustomerinfo = true;                                 //是否显示线索信息部分
    private String[]                 infokeylist1;                                             //跟踪计划内容
    private String[]                 infokeyListNew;                                           //新建跟踪计划
    private String[]                        infokey;                                                  //联系人内容
    private BottomBar                       mBottomBtn;                                               //底部按钮
    private ToggleButton                    newToggleBtn;                                             //是否显示新建跟踪计划
    private TextView                        title;                                                    //标题
    private TracePlan                       tracePlan         = null;                                 //跟踪计划信息
    private TracePlan                       newTracePlan      = null;                                 //新建跟踪计划信息
    private String                          projectId         = null;
    private Project                         project           = null;                                 //销售线索信息
    private Customer                        customer          = null;                                 //客户信息
    private Customer                        customerOppoFrom  = null;                                 //销售线索添加跟踪计划传入
    private boolean                         editData          = false;                                //是否编辑数据
    private boolean                         bshowbottombar    = true;                                 //是否显示BottomBar
    private boolean                         addFlag           = false;                                //销售线索进入编辑
    private boolean                         ComeFromOppo      = false;                                //从销售线索进入显示
    public CRMManager                       crmManager;

    /**
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * 
     * <pre>
     * 获取资源文件
     * </pre>
     *
     * @param id 资源文件id
     * @return
     */
    private String getStringId(int id) {
        return getActivity().getString(id);
    }

    /**
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        Log.d(tag, "onCreat");
    }

    /**
     * 生命周期方法
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_follow_plan, container, false);
        infokeylist1 = new String[] { getStringId(R.string.custName),
                getStringId(R.string.custMobile), getStringId(R.string.custOtherPhone),
                getStringId(R.string.activitytype), getStringId(R.string.executetime),
                getStringId(R.string.executestatus), getStringId(R.string.activitycontent),
                getStringId(R.string.contactresult), getStringId(R.string.custfeedback),
                getStringId(R.string.leadercomment) };
        infokeyListNew = new String[]{getStringId(R.string.activitytype), getStringId(R.string.executetime_new),
                                      getStringId(R.string.executestatus), getStringId(R.string.activitycontent),
                                      getStringId(R.string.contactresult), getStringId(R.string.custfeedback),
                                      getStringId(R.string.leadercomment)};
        infokey = new String[] { getStringId(R.string.brand_1), getStringId(R.string.model),
                getStringId(R.string.flowStatus), getStringId(R.string.preorderDate),
                getStringId(R.string.tracePlan_contacter), getStringId(R.string.contacter_phone), };
        this.crmManager = ((RoilandCRMApplication) getActivity().getApplication()).getCRMManager();
        Intent intent = getActivity().getIntent();
        addFlag = intent.getBooleanExtra("addFlag", false);
        ComeFromOppo = intent.getBooleanExtra("ComeFromOppo", false);
        customerOppoFrom = intent.getParcelableExtra("customerOppoFrom");
        mBottomBtn = new BottomBar(getActivity(), getActivity().findViewById(R.id.mybottom_bar));
        newToggleBtn = (ToggleButton) view.findViewById(R.id.toggleButton_new);
        mFollowListView = (LinearLayout) view.findViewById(R.id.follow_allinfo_list);
        mNewFollowListView = (LinearLayout) view.findViewById(R.id.follow_oppoinfo_list);
        newlayout = (LinearLayout) view.findViewById(R.id.follow_oppoinfo_list_layout);
        mOInfoListView = (LinearLayout) view.findViewById(R.id.oppoinfo_list);
        oinfolayout = (LinearLayout) view.findViewById(R.id.oppoinfo_list_layout);
        title = (TextView) view.findViewById(R.id.follow_info_title);
        if (followPlanInfoAdapter == null)
            followPlanInfoAdapter = new BasicInfoListAdapter(this.getActivity());
        followPlanInfoAdapter.crmManager = this.crmManager;
        if (oppoInfoAdapter == null) {
            oppoInfoAdapter = new BasicInfoListAdapter(this.getActivity());
            oppoInfoAdapter.crmManager = this.crmManager;
        }
        if (newFollowPlanInfoAdapter == null) {
            newFollowPlanInfoAdapter = new BasicInfoListAdapter(this.getActivity());
            newFollowPlanInfoAdapter.crmManager = this.crmManager;
        }
        setNewListHidden();
        //修改刚进入activity时ScrollView不置顶问题
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
        title.requestFocus();
        if (tracePlan != null && tracePlan.getCustomer() != null) {
            projectId = tracePlan.getCustomer().getProjectID();
        }
        if (tracePlan != null && bshowcustomerinfo != false) {
            if (!StringUtils.isEmpty(projectId)) {
                newlayout.setVisibility(View.GONE);
                oinfolayout.setVisibility(View.VISIBLE);
                mOInfoListView.setVisibility(View.VISIBLE);
            }
        }
        newToggleBtn.setVisibility(View.GONE);
        //开关按钮的单击事件
        newToggleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//开
                    newlayout.setVisibility(View.VISIBLE);
                    newToggleBtn.setBackgroundResource(R.drawable.toggle_sliding2);
                    setNewListdisplay();
                } else {//关
                    newToggleBtn.setBackgroundResource(R.drawable.toggle_sliding1);
                    newlayout.setVisibility(View.GONE);
                    setNewListHidden();
                }
            }
        });
        if (addFlag) {
            tracePlan = new TracePlan();
            tracePlan.setExecuteTime(DateFormatUtils.systemDate());
        }
        if (ComeFromOppo || addFlag) {
            mBottomBtn.setVisible(false);
            bshowcustomerinfo = false;
        }
        displayfollowPlanInfo();
        displayNewFollowInfo();
        if (!addFlag) {
            tracePlan = getUpdatedTracePlan();
        }
        if (!ComeFromOppo) {
            if (!StringUtils.isEmpty(projectId)) {
                OppoTask task = new OppoTask(getActivity());
                task.execute();
            }

        } else {
            oinfolayout.setVisibility(View.GONE);
            setOInfoListHidden();
        }
        if (addFlag) {
            editData = true;
            showEdit();
        }
        return view;
    }

    /**
     * 
     * <pre>
     * 跟踪计划信息部分 加载页面信息
     * </pre>
     *
     */
    public void displayfollowPlanInfo() {
        followPlanInfo = new ArrayList<BasicInfoListAdapter.Info>();
        followPlanInfo.clear();
        if (bshowcustomerinfo) {
            followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[0],
                BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
                (customer != null && customer.getCustName() != null ? customer.getCustName() : ""),
                false, false));
            followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[1], BaseInfoRowViewItem.MOBILETEXT_TYPE, null,(customer != null && customer.getCustMobile() != null ? customer.getCustMobile(): ""), false));
            followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[2],
                BaseInfoRowViewItem.PSTNTEXT_TYPE, null,
                (customer != null && customer.getCustOtherPhone() != null ? customer
                    .getCustOtherPhone() : ""), false));
        }
        followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[3],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getActivityType() != null ? tracePlan.getActivityType()
                : ""), true, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[4],
            BaseInfoRowViewItem.DATE_TYPE, null, (tracePlan != null && tracePlan.getExecuteTime() != 0 ? String.valueOf(tracePlan.getExecuteTime()) : null), true));
        if(addFlag){
            followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[5],
                BaseInfoRowViewItem.SELECTION_TYPE, null,
                (tracePlan != null && tracePlan.getExecuteStatus() != null ? tracePlan
                    .getExecuteStatus() : getResources().getString(R.string.executestatus_1)), true,
                false));
        } else{
            followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[5],
                BaseInfoRowViewItem.SELECTION_TYPE, null,
                (tracePlan != null && tracePlan.getExecuteStatus() != null ? tracePlan
                    .getExecuteStatus() : getResources().getString(R.string.executestatus_1)), true,
                true));
        }
        followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[6],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (tracePlan != null && tracePlan.getActivityContent() != null ? tracePlan
                .getActivityContent() : ""), true, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[7],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (tracePlan != null && tracePlan.getContactResult() != null ? tracePlan
                .getContactResult().trim() : ""), false, true));
        followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[8],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (tracePlan != null && tracePlan.getCustFeedback() != null ? tracePlan.getCustFeedback()
                : ""), false, true));
        if(!addFlag){
            followPlanInfo.add(new BasicInfoListAdapter.Info(infokeylist1[9],
                BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
                (tracePlan != null && tracePlan.getLeaderComment() != null ? tracePlan
                    .getLeaderComment() : ""), false, false));
        }
        followPlanInfoAdapter.setContentList(followPlanInfo);
        followPlanInfoAdapter.notifyDataSetChanged();
        refreshFollowInfoList();
    }

    /**
     * 
     * <pre>
     * 线索信息部分 加载页面信息
     * </pre>
     *
     */
    public void displayOppoInfo() {
        oppoInfo = new ArrayList<BasicInfoListAdapter.Info>();
        oppoInfo.add(new BasicInfoListAdapter.Info(infokey[0], BaseInfoRowViewItem.SIMPLETEXT_TYPE,
            null,
            (project != null && project.getPurchaseCarIntention().getBrand() != null ? project
                .getPurchaseCarIntention().getBrand() : ""), false));
        oppoInfo.add(new BasicInfoListAdapter.Info(infokey[1], BaseInfoRowViewItem.SIMPLETEXT_TYPE,
            null,
            (project != null && project.getPurchaseCarIntention().getModel() != null ? project
                .getPurchaseCarIntention().getModel() : ""), false));
        oppoInfo.add(new BasicInfoListAdapter.Info(infokey[2], BaseInfoRowViewItem.SIMPLETEXT_TYPE,
            null,
            (project != null && project.getPurchaseCarIntention().getFlowStatus() != null ? project
                .getPurchaseCarIntention().getFlowStatus() : ""), false));
        oppoInfo.add(new BasicInfoListAdapter.Info(infokey[3], BaseInfoRowViewItem.SIMPLETEXT_TYPE,
            null, (project != null
                   && DateFormatUtils.formatDate(project.getPurchaseCarIntention()
                       .getPreorderDate()) != null ? DateFormatUtils.formatDate(project
                .getPurchaseCarIntention().getPreorderDate()) : ""), false));
        oppoInfo.add(new BasicInfoListAdapter.Info(infokey[4], BaseInfoRowViewItem.SIMPLETEXT_TYPE,
            null, (project != null && project.getCustomer().getCustName() != null ? project
                .getCustomer().getCustName() : ""), false));
        oppoInfo.add(new BasicInfoListAdapter.Info(infokey[5], BaseInfoRowViewItem.SIMPLETEXT_TYPE,
            null, (project != null && project.getCustomer().getCustMobile() != null ? project
                .getCustomer().getCustMobile() : ""), false));
        oppoInfoAdapter.setContentList(oppoInfo);
        oppoInfoAdapter.notifyDataSetChanged();
        refreshOppoInfoList();
    }

    /**
     * 
     * <pre>
     * 新建跟踪计划信息部分 加载页面信息
     * </pre>
     *
     */
    public void displayNewFollowInfo() {
        newFollowPlanInfo = new ArrayList<BasicInfoListAdapter.Info>();
        newFollowPlanInfo.clear();
        newFollowPlanInfo.add(new BasicInfoListAdapter.Info(infokeyListNew[0],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (newTracePlan != null && newTracePlan.getActivityType() != null ? newTracePlan
                .getActivityType() : ""), true));
        newFollowPlanInfo.add(new BasicInfoListAdapter.Info(" "+infokeyListNew[1],
            BaseInfoRowViewItem.DATE_TYPE, null, (newTracePlan != null ? (String.valueOf(newTracePlan
                .getExecuteTime()) != "0" ? String.valueOf(newTracePlan.getExecuteTime()) : String
                .valueOf(DateFormatUtils.getSystemDate())) : String.valueOf(DateFormatUtils
                .getSystemDate())), true));
        newFollowPlanInfo.add(new BasicInfoListAdapter.Info(infokeyListNew[2],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (newTracePlan != null && newTracePlan.getExecuteStatus() != null ? newTracePlan
                .getExecuteStatus() : getResources().getString(R.string.executestatus_1)), true,false));
        newFollowPlanInfo.add(new BasicInfoListAdapter.Info(infokeyListNew[3],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (newTracePlan != null && newTracePlan.getActivityContent() != null ? newTracePlan
                .getActivityContent() : ""), true));
        newFollowPlanInfo.add(new BasicInfoListAdapter.Info(infokeyListNew[4],
            BaseInfoRowViewItem.SELECTION_TYPE, null,
            (newTracePlan != null && newTracePlan.getContactResult() != null ? newTracePlan
                .getContactResult().trim() : ""), false));
        newFollowPlanInfo.add(new BasicInfoListAdapter.Info(infokeyListNew[5],
            BaseInfoRowViewItem.SIMPLETEXT_TYPE, null,
            (newTracePlan != null && newTracePlan.getCustFeedback() != null ? newTracePlan
                .getCustFeedback() : ""), false));
        newFollowPlanInfoAdapter.setContentList(newFollowPlanInfo);
        newFollowPlanInfoAdapter.notifyDataSetChanged();
        refreshNewFollowInfoList();
    }

    /**
     * 创建菜单
     * @param menu
     * @param inflater
     * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        if (addFlag) {
            item = menu.findItem(R.id.save);
        } else {
            item = menu.findItem(R.id.edit);
        }
        item.setVisible(true);
    }

    /**
     * 菜单被选中事件
     * @param item 被选中菜单项
     * @return
     * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                //判断网络状况
                if (EventReceiver.isNetworkUnavailable()) {
                    Toast.makeText(getActivity(), getString(R.string.network_error),
                        Toast.LENGTH_SHORT).show();
                    break;
                }
                newlayout.setVisibility(View.VISIBLE);
                editData = !editData;
                showEdit();
                refreshFollowInfoList();
                refreshNewFollowInfoList();      
                item.setIcon(R.drawable.save_btn2);
                break;
            case R.id.save:
                showEdit();
                if (!followPlanInfoAdapter.getEditable()) {
                    item.setIcon(R.drawable.edit_icon);
                } else {
                    item.setIcon(R.drawable.save_btn2);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 
     * <pre>
     * 更新跟踪计划方法，把页面上的数据更新到原始list中
     * </pre>
     *
     */
    private void refreshFollowInfoList() {
        if (mFollowListView != null && followPlanInfoAdapter != null) {
            mFollowListView.removeAllViews();
            for (int i = 0; i < followPlanInfoAdapter.getCount(); i++) {
                mFollowListView.addView(followPlanInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mFollowListView.addView(dividerView);
            }

        }
    }

    /**
     * 
     * <pre>
     * 更新线索方法，把页面上的数据更新到原始list中
     * </pre>
     *
     */
    private void refreshOppoInfoList() {
        if (mOInfoListView != null && oppoInfoAdapter != null) {
            mOInfoListView.removeAllViews();
            for (int i = 0; i < oppoInfoAdapter.getCount(); i++) {
                mOInfoListView.addView(oppoInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mOInfoListView.addView(dividerView);
            }

        }
    }

    /**
     * 
     * <pre>
     * 更新新建跟踪计划方法，把页面上的数据更新到原始list中
     * </pre>
     *
     */
    private void refreshNewFollowInfoList() {
        if (mNewFollowListView != null && newFollowPlanInfoAdapter != null) {
            mNewFollowListView.removeAllViews();
            for (int i = 0; i < newFollowPlanInfoAdapter.getCount(); i++) {
                mNewFollowListView.addView(newFollowPlanInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                mNewFollowListView.addView(dividerView);
            }

        }
    }

    /**
     * 
     * <pre>
     * 取得销售线索异步操作
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: ScFollowPlanFragment.java, v 0.1 2013-6-21 下午3:00:48 shuang.gao Exp $
     */
    private class OppoTask extends BaseTask<Project> {
        String tag = Log.getTag(OppoTask.class);

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
                Log.i("FollowPlanActivity", "getProjectID:"
                                            + tracePlan.getCustomer().getProjectID()
                                            + "   getCustomerID:"
                                            + tracePlan.getCustomer().getCustomerID()
                                            + "     getCustName:"
                                            + tracePlan.getCustomer().getCustName());
                lp = manager.getProjectInfo(tracePlan.getCustomer().getProjectID(), tracePlan
                    .getCustomer().getCustomerID());
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
                Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            } else {
                if (result != null) {
                    project = result;
                    project.getCustomer().setProjectID(tracePlan.getCustomer().getProjectID());
                    displayOppoInfo();
                }
            }
        }
    }

    /**
     * 
     * <pre>
     * 更新跟踪计划异步操作
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: ScFollowPlanFragment.java, v 0.1 2013-6-21 下午3:01:17 shuang.gao Exp $
     */
    private class UpdateTracePlanTask extends BaseTask<Boolean> {

        private String tag = Log.getTag(NewTracePlanTask.class);

        public UpdateTracePlanTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean b = null;
            RoilandCRMApplication application = (RoilandCRMApplication) activity.getApplication();
            try {
                CRMManager manager = application.getCRMManager();
                tracePlan = getUpdatedTracePlan();
                //如果跟踪计划没有获得执行状态代码,从数据字典里取
                if (StringUtils.isEmpty(tracePlan.getExecuteStatusCode())) {
                    List<Dictionary> dicArray = manager.getDictionariesByType("dicExecuteStatus");
                    for (int i = 0; i < dicArray.size(); i++) {
                        if (dicArray.get(i).getDicValue().equals(tracePlan.getExecuteStatus())) {
                            tracePlan.setExecuteStatus(dicArray.get(i).getDicValue());
                            tracePlan.setExecuteStatusCode(dicArray.get(i).getDicKey());
                            break;
                        }
                    }
                }
                String pid = projectId;
                if (StringUtils.isEmpty(pid)) {
                    pid = tracePlan.getCustomer().getProjectID();
                }
                TracePlan newtrace = null;
                if (getNewUpdatedTracePlan() != null && newToggleBtn.isChecked()) {
                    Log.i(tag, "createTracePlan");
                    newtrace = getNewUpdatedTracePlan();
                    newtrace.setCustomer(tracePlan.getCustomer());
                    //如果跟踪计划没有获得执行状态代码,从数据字典里取
                    if (StringUtils.isEmpty(newtrace.getExecuteStatusCode())) {
                        List<Dictionary> dicArray = manager
                            .getDictionariesByType("dicExecuteStatus");
                        for (int i = 0; i < dicArray.size(); i++) {
                            if (dicArray.get(i).getDicValue().equals(tracePlan.getExecuteStatus())) {
                                newtrace.setExecuteStatus(dicArray.get(i).getDicValue());
                                newtrace.setExecuteStatusCode(dicArray.get(i).getDicKey());
                                break;
                            }
                        }
                    }
                    String activityID = manager.createTracePlan(pid, tracePlan.getCustomer()
                        .getCustomerID(), newtrace);
                }
                b = manager
                    .updateTracePlan(pid, tracePlan.getCustomer().getCustomerID(), tracePlan);
            } catch (ResponseException e) {
                responseException = e;
            }
            return b;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.i(tag, "onPostExecute  => " + result);
            super.onPostExecute(result);
            if (responseException != null) {
                Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            } else {
                // 如果更新成功将新的跟踪计划返回
                if (result) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("tracePlan", tracePlan);
                    intent.putExtra("editFlag", true);
                    intent.putExtras(bundle);
                    getActivity().setResult(4, intent);
                    Toast.makeText(getActivity(), R.string.opt_success, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }

    /**
     * 
     * <pre>
     * 新建跟踪计划异步操作
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: ScFollowPlanFragment.java, v 0.1 2013-6-21 下午3:02:45 shuang.gao Exp $
     */
    public class NewTracePlanTask extends BaseTask<String> {

        private String tag = Log.getTag(NewTracePlanTask.class);

        public NewTracePlanTask(Activity activity) {
            super(activity);
        }

        @Override
        protected String doInBackground(String... arg0) {
            String b = null;
            System.out.println("NewTracePlanTask ------run------");
            RoilandCRMApplication application = (RoilandCRMApplication) activity.getApplication();
            try {
                if (!addFlag) {
                    tracePlan = getNewUpdatedTracePlan();
                } else {
                    tracePlan = getUpdatedTracePlan();
                }
                CRMManager manager = application.getCRMManager();
                //如果跟踪计划没有获得执行状态代码,从数据字典里取
                if (StringUtils.isEmpty(tracePlan.getExecuteStatusCode())) {
                    List<Dictionary> dicArray = manager.getDictionariesByType("dicExecuteStatus");
                    for (int i = 0; i < dicArray.size(); i++) {
                        if (dicArray.get(i).getDicValue().equals(tracePlan.getExecuteStatus())) {
                            tracePlan.setExecuteStatus(dicArray.get(i).getDicValue());
                            tracePlan.setExecuteStatusCode(dicArray.get(i).getDicKey());
                            break;
                        }
                    }
                }
                if (addFlag) {
                    b = manager.createTracePlan(customerOppoFrom.getProjectID(),
                        customerOppoFrom.getCustomerID(), tracePlan);
                } else {
                    b = manager.createTracePlan(tracePlan.getCustomer().getProjectID(), tracePlan
                        .getCustomer().getCustomerID(), tracePlan);
                }
            } catch (ResponseException e) {
                responseException = e;
            }
            return b;
        }

        protected void onPostExecute(String result) {
            Log.i(tag, "onPostExecute  => " + result);
            super.onPostExecute(result);

            if (result != null) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.add_success),
                    Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else if (responseException != null) {
                Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_LONG)
                    .show();
            }
        }

    }

    /**
     * 
     * <pre>
     * 编辑和保存事件
     * </pre>
     *
     */
    public void showEdit() {
        //编辑时
        if (editData) {
                newFollowPlanInfoAdapter.setEditable(true);
            if(tracePlan != null && getString(R.string.executestatus_2).equals(tracePlan.getExecuteStatus())){
                followPlanInfoAdapter.setEditable(false);
            }else{
                followPlanInfoAdapter.setEditable(true);
            }
            if (!addFlag) {
                newlayout.setVisibility(View.VISIBLE);
                newToggleBtn.setVisibility(View.VISIBLE);
                mNewFollowListView.setVisibility(View.VISIBLE);
                setNewListdisplay();//显示新建  
            }else{
                editData = !editData;
            }
            mBottomBtn.setVisible(false);
            oinfolayout.setVisibility(View.GONE);
            setOInfoListHidden();//隐藏线索
        } else {
            //保存时
            String followMessage = "";
            if (!addFlag) {
                if (tracePlan.getExecuteStatus().equals(
                    getResources().getString(R.string.executestatus_1))) {
                    followMessage = followInfoValidation();
                }
            } else {
                followMessage = followInfoValidation();
            }
            if (!StringUtils.isEmpty(followMessage)) {
                Toast.makeText(getActivity(), followMessage, Toast.LENGTH_SHORT).show();
                if(!newToggleBtn.isChecked()){
                    setNewListHidden();
                }
                editData = !editData;
                return;
            } else {
                if (!addFlag) {
                    if (newToggleBtn.isChecked() && newlayout.getVisibility() == View.VISIBLE) {
                        String newFollowMessage = newFollowInfoValidation();
                        if (!StringUtils.isEmpty(newFollowMessage)) {
                            Toast.makeText(getActivity(), newFollowMessage, Toast.LENGTH_SHORT)
                                .show();
                            editData = !editData;
                            return;
                        }
                    }
                }
                newFollowPlanInfoAdapter.setEditable(false);
                followPlanInfoAdapter.setEditable(false);
                if (tracePlan.getActivityID() != null) {
                    tracePlan = getUpdatedTracePlan();
                    //弹出提示框 提示没有新建跟踪计划
                    if (!newToggleBtn.isChecked()
                        && tracePlan.getExecuteStatus().equals(
                            getResources().getString(R.string.executestatus_2))) {
                        new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.alert)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage(getResources().getString(R.string.prompts_message))
                            .setPositiveButton(getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new UpdateTracePlanTask(getActivity()).execute();
                                        dialog.dismiss();
                                    }
                                })
                            .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        followPlanInfoAdapter.setEditable(true);
                                        newFollowPlanInfoAdapter.setEditable(true);
                                        editData = !editData;
                                        item.setIcon(R.drawable.oppoinfo_save_btn);
                                        mBottomBtn.setVisible(false);
                                        oinfolayout.setVisibility(View.GONE);
                                        newToggleBtn.setVisibility(View.VISIBLE);
                                        newToggleBtn.setChecked(true);
                                        newlayout.setVisibility(View.VISIBLE);
                                        setOInfoListHidden();
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else {
                        //更新跟踪计划
                        new UpdateTracePlanTask(getActivity()).execute();
                    }
                } else {
                    //新建跟踪计划
                    new NewTracePlanTask(getActivity()).execute();
                }
                setOInfoListdisplay();
                displayOppoInfo();
                if(!newToggleBtn.isChecked()){
                  newlayout.setVisibility(View.GONE);
                  newToggleBtn.setVisibility(View.GONE);  
                }
                if (!bshowbottombar) {
                    mBottomBtn.setVisible(true);
                    oinfolayout.setVisibility(View.VISIBLE);
                    setNewListHidden();
                }
            }
        }
    }

    /**
     * 
     * <pre>
     * 隐藏新建跟踪计划
     * </pre>
     *
     */
    private void setNewListHidden() {
        newlayout.setVisibility(View.GONE);
        mOInfoListView.setVisibility(View.GONE);
        oinfolayout.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = mNewFollowListView.getLayoutParams();
        params.height = 0;
        mNewFollowListView.setLayoutParams(params);
    }

    /**
     * 
     * <pre>
     * 显示新建跟踪计划
     * </pre>
     *
     */
    private void setNewListdisplay() {
        ViewGroup.LayoutParams params = mNewFollowListView.getLayoutParams();
        params.height = params.WRAP_CONTENT;
        mNewFollowListView.setLayoutParams(params);
    }

    /**
     * 
     * <pre>
     * 隐藏销售线索
     * </pre>
     *
     */
    private void setOInfoListHidden() {
        ViewGroup.LayoutParams params = mOInfoListView.getLayoutParams();
        params.height = 0;
        mOInfoListView.setLayoutParams(params);
    }

    /**
     * 
     * <pre>
     * 显示销售线索
     * </pre>
     *
     */
    private void setOInfoListdisplay() {
        ViewGroup.LayoutParams params = mOInfoListView.getLayoutParams();
        params.height = params.WRAP_CONTENT;
        mOInfoListView.setLayoutParams(params);
    }

    /**
     * 
     * <pre>
     * 跟踪计划验证
     * </pre>
     *
     * @return 提示信息errString
     */
    private String followInfoValidation() {
        ArrayList<BasicInfoListAdapter.Info> tracePlanList = (ArrayList<Info>) followPlanInfo;
        String errString = null;
        for (int i = 0; i < tracePlanList.size() - 1; i++) {
            BasicInfoListAdapter.Info info = tracePlanList.get(i);
            errString = DataVerify.infoValidation(info.key, info.value, null, null, null, null,
                getActivity(), false, false);
            if (!StringUtils.isEmpty(errString)) {
                break;
            }
        }
        return errString;
    }

    /**
     * 
     * <pre>
     *  新建跟踪计划验证
     * </pre>
     *
     * @return 提示信息errString
     */
    private String newFollowInfoValidation() {
        ArrayList<BasicInfoListAdapter.Info> tracePlanList = (ArrayList<Info>) newFollowPlanInfo;
        String errString = null;
        for (int i = 0; i < tracePlanList.size(); i++) {
            BasicInfoListAdapter.Info info = tracePlanList.get(i);
            errString = DataVerify.infoValidation(info.key.replaceAll(":", ""), info.value, null,
                null, null, null, getActivity(), false, false);
            if (!StringUtils.isEmpty(errString)) {
                break;
            }
        }
        return errString;
    }

    /**
     * 获得最新跟踪计划详细信息
     * @return result 客户信息
     */
    public TracePlan getUpdatedTracePlan() {
        TracePlan result = new TracePlan();
        result = tracePlan;

        for (BasicInfoListAdapter.Info info : followPlanInfo) {
            if (info.key.equals(infokeylist1[0])) {
                result.getCustomer().setCustName(info.value);
            } else if (info.key.equals(infokeylist1[1]))
                result.getCustomer().setCustMobile(info.value);
            else if (info.key.equals(infokeylist1[2]))
                result.getCustomer().setCustOtherPhone(info.value);
            else if (info.key.equals(infokeylist1[3]) && info.pairKey != null) {
                result.setActivityTypeCode(info.pairKey);
                result.setActivityType(info.value);
            } else if (info.key.equals(infokeylist1[4]) && info.value != null) {
                result.setExecuteTime(Long.valueOf(info.value));
            } else if (info.key.equals(infokeylist1[5]) && info.pairKey != null) {
                result.setExecuteStatusCode(info.pairKey);
                result.setExecuteStatus(info.value);
            } else if (info.key.equals(infokeylist1[6])) {
                result.setActivityContent(info.value);
            } else if (info.key.equals(infokeylist1[7])) {
                result.setContactResultCode(info.pairKey);
                result.setContactResult(info.value);
            } else if (info.key.equals(infokeylist1[8])) {
                result.setCustFeedback(info.value);
            } else if (info.key.equals(infokeylist1[9])) {
                result.setLeaderComment(info.value);
            }
        }
        return result;
    }
    
    public TracePlan getUpdatedNewTracePlan(){
        TracePlan result = new TracePlan();
        result = tracePlan;

        for (BasicInfoListAdapter.Info info : newFollowPlanInfo) {
            if (info.key.equals(infokeyListNew[1]) && info.pairKey != null) {
                result.setActivityTypeCode(info.pairKey);
                result.setActivityType(info.value);
            } else if (info.key.equals(" "+infokeyListNew[2])) {
                result.setExecuteTime(Long.valueOf(info.value));
            } else if (info.key.equals(infokeyListNew[3]) && info.pairKey != null) {
                result.setExecuteStatusCode(info.pairKey);
                result.setExecuteStatus(info.value);
            } else if (info.key.equals(infokeyListNew[4])) {
                result.setActivityContent(info.value);
            } else if (info.key.equals(infokeyListNew[5])) {
                result.setContactResultCode(info.pairKey);
                result.setContactResult(info.value);
            } else if (info.key.equals(infokeyListNew[6])) {
                result.setCustFeedback(info.value);
            }
        }
        return result;
    
        
    }

    /**
     * 获得最新新建跟踪计划详细信息
     * @return result 客户信息
     */
    public TracePlan getNewUpdatedTracePlan() {
        TracePlan result = new TracePlan();

        for (BasicInfoListAdapter.Info info : newFollowPlanInfo) {
            if (info.key.equals(infokeylist1[0])) {
                result.getCustomer().setCustName(info.value);
            } else if (info.key.equals(infokeylist1[1]))
                result.getCustomer().setCustMobile(info.value);
            else if (info.key.equals(infokeylist1[2]))
                result.getCustomer().setCustOtherPhone(info.value);
            else if (info.key.equals(infokeylist1[3]) && info.pairKey != null) {
                result.setActivityTypeCode(info.pairKey);
                result.setActivityType(info.value);
            } else if (info.key.equals(" "+infokeylist1[4])) {
                result.setExecuteTime(Long.valueOf(info.value));
            } else if (info.key.equals(infokeylist1[5]) && info.pairKey != null) {
                result.setExecuteStatusCode(info.pairKey);
                result.setExecuteStatus(info.value);
            } else if (info.key.equals(infokeylist1[6])) {
                result.setActivityContent(info.value);
            } else if (info.key.equals(infokeylist1[7]) && info.pairKey != null) {
                result.setContactResultCode(info.pairKey);
                result.setContactResult(info.value);
            } else if (info.key.equals(infokeylist1[8])) {
                result.setCustFeedback(info.value);
            }
        }

        newTracePlan = result;
        return result;
    }

    public TracePlan getTracePlan() {
        return tracePlan;
    }

    public void setTracePlan(TracePlan tracePlan) {
        this.tracePlan = tracePlan;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void dataModify(String key, String value, String pairKey) {
        
        Log.i(tag, "-------"+key+"------"+value+"-----"+pairKey+"-----");
        BaseInfoRowViewItem item ;
        if(getString(R.string.executestatus).equals(key)){
            if(getString(R.string.executestatus_2).equals(value) || getString(R.string.executestatus_3).equals(value)){
                for(BasicInfoListAdapter.Info info : followPlanInfo){
                    if(getString(R.string.executetime).equals(info.key)){
                        item = (BaseInfoRowViewItem)mFollowListView.findViewWithTag(getString(R.string.executetime));
                        item.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                        item.setEditable(false);
                        info.setValue(String.valueOf(DateFormatUtils.getSystemDate()));
                        break;
                    }
                }
            }else{
                for(BasicInfoListAdapter.Info info : followPlanInfo){
                    if(getString(R.string.executetime).equals(info.key)){
                        item = (BaseInfoRowViewItem)mFollowListView.findViewWithTag(getString(R.string.executetime));
                        item.setValue(String.valueOf(tracePlan.getExecuteTime()));
                        item.setEditable(true);
                        info.setValue(String.valueOf(tracePlan.getExecuteTime()));
                        break;
                    }
                }
            }
        }
        followPlanInfoAdapter.notifyDataSetChanged();
    }

}
