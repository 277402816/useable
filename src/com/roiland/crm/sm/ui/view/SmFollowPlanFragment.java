package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter.Info.DataChangeListener;
import com.roiland.crm.sm.ui.widget.BottomBar;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.EventReceiver;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 銷售过程管理详细列表Fragment
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmFollowPlanFragment.java, v 0.1 2013-5-24 下午2:51:09 shuang.gao Exp $
 */
public class SmFollowPlanFragment extends SherlockFragment implements DataChangeListener {

    private String          tag               = Log.getTag(SmFollowPlanFragment.class);
    public boolean          flag              = false;                                 //判断状态
    public MenuItem         leaderItem;
    private static String[] infokeylist1;                                              //列表显示信息
    private LinearLayout    mFollowListView;                                           //显示详细信息画面
    private LinearLayout    oppoList;                                                  //显示线索信息
    private LinearLayout    oppoListLinearLayout;                                      //线索外层布局
    private BottomBar       mBottomBtn;                                                //底部按钮
    private boolean         bedit             = false;                                 //true 编辑状态
    private boolean         bshowcustomerinfo = true;                                  //显示销售线索信息
    private boolean         mComeFromOppo;                                             //true 从销售线索进入跟踪计划
    private boolean         isComeFromCustManager;                                     //true 根据是否是从客户管理进入 来控制是否显示领导批示
    private boolean         ShowBottonBar     = true;                                  //根据是否是从销售线索进入 来控制是否显示BottomBar
    private boolean         EditData;
    boolean                 b                 = false;
    private InfoListAdapter tracePlanInfoAdapter;                                      //跟踪计划适配
    private OppInfoAdapter  oppoinfoAdapter;                                           //销售线索适配
    private TracePlan       tracePlan         = null;                                  //跟踪计划信息
    private Project         project           = null;                                  //销售线索信息
    private Customer        customer          = null;                                  //客戶信息
    private String          projectId         = null;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        Log.d(tag, "onCreat");
    }

    /**
     * 生命周期方法 加載页面
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sm_follow_plan, container, false);
        infokeylist1 = new String[] { getStringId(R.string.custName),
                getStringId(R.string.custMobile), getStringId(R.string.custOtherPhone),
                getStringId(R.string.activitytype), getStringId(R.string.executetime),
                getStringId(R.string.executestatus), getStringId(R.string.activitycontent),
                getStringId(R.string.contactresult), getStringId(R.string.custfeedback),
                getStringId(R.string.leadercomment) };
        Intent intent = getActivity().getIntent();
        mFollowListView = (LinearLayout) view.findViewById(R.id.follow_allinfo_list);
        oppoList = (LinearLayout) view.findViewById(R.id.oppo_list);
        oppoListLinearLayout = (LinearLayout) view.findViewById(R.id.opp_list_linearlayout);
        ShowBottonBar = intent.getBooleanExtra("ShowBottonBar", true);
        EditData = intent.getBooleanExtra("EditData", true);
        mComeFromOppo = intent.getBooleanExtra("ComeFromOppo", false);
        isComeFromCustManager = intent.getBooleanExtra("ComeFromCustManager", false);
        mBottomBtn = new BottomBar(getActivity(), getActivity().findViewById(R.id.mybottom_bar));
        tracePlanInfoAdapter = new InfoListAdapter(getActivity());
        if (tracePlan == null) {
            //判断是否应该显示客户名称，移动电话其他电话等信息
            if (bshowcustomerinfo) {
                tracePlanInfoAdapter.setItem(getString(R.string.custName), "");
                tracePlanInfoAdapter.setItem(getString(R.string.custMobile), "");
                tracePlanInfoAdapter.setItem(getString(R.string.custOtherPhone), "");
            }
            tracePlanInfoAdapter.setItem(getString(R.string.activitytype), "");
            tracePlanInfoAdapter.setItem(getString(R.string.executetime), DataVerify.systemDate(),
                "");
            tracePlanInfoAdapter.setItem(getString(R.string.executestatus),
                getString(R.string.executestatus_1));
            tracePlanInfoAdapter.setItem(getString(R.string.activitycontent), "");
            tracePlanInfoAdapter.setItem(getString(R.string.contactresult), "");
            tracePlanInfoAdapter.setItem(getString(R.string.custfeedback), "");
            tracePlanInfoAdapter.setItem(getString(R.string.leadercomment), "");
        } else {
            //判断是否应该显示客户名称，移动电话其他电话等信息
            if (bshowcustomerinfo) {
                tracePlanInfoAdapter.setItem(getString(R.string.custName), tracePlan.getCustomer()
                    .getCustName());
                tracePlanInfoAdapter.setItem(getString(R.string.custMobile), tracePlan
                    .getCustomer().getCustMobile());
                tracePlanInfoAdapter.setItem(getString(R.string.custOtherPhone), tracePlan
                    .getCustomer().getCustOtherPhone() != null ? tracePlan.getCustomer()
                    .getCustOtherPhone().trim() : null);
            }
            if (projectId != null && !projectId.equals("null")) {
                oppoinfoAdapter = new OppInfoAdapter(getActivity());
                OppoTask task = new OppoTask(getActivity());
                task.execute();
            } else {
                oppoListLinearLayout.setVisibility(View.GONE);
            }
            tracePlanInfoAdapter
                .setItem(getString(R.string.activitytype), StringUtils.isEmpty(tracePlan
                    .getActivityType()) ? "" : tracePlan.getActivityType(), tracePlan
                    .getActivityTypeCode());
            tracePlanInfoAdapter.setItem(getString(R.string.executetime),
                DateFormatUtils.formatDate(tracePlan.getExecuteTime()));
            tracePlanInfoAdapter.setItem(getString(R.string.executestatus),
                tracePlan.getExecuteStatus(), tracePlan.getExecuteStatusCode());
            tracePlanInfoAdapter.setItem(getString(R.string.activitycontent),
                StringUtils.notNull(tracePlan.getActivityContent()));
            tracePlanInfoAdapter.setItem(getString(R.string.contactresult),
                StringUtils.notNull(tracePlan.getContactResult().trim()),
                tracePlan.getContactResultCode());
            tracePlanInfoAdapter.setItem(getString(R.string.custfeedback),
                StringUtils.notNull(tracePlan.getCustFeedback()));
            tracePlanInfoAdapter.setItem(getString(R.string.leadercomment),
                StringUtils.notNull(tracePlan.getLeaderComment()));
        }
        refreshFollowInfoList(true);
        bedit = intent.getBooleanExtra("EditMode", false);
        //判断是否为编辑状态
        if (bedit) {
            tracePlan = new TracePlan();
            if (project != null) {
                tracePlan.setCustomer(project.getCustomer());
            } else if (customer != null) {
                tracePlan.setCustomer(customer);
            }
            //判断是否应该显示客户名称，移动电话其他电话等信息
            if (bshowcustomerinfo) {
                tracePlanInfoAdapter.setItem(getString(R.string.custName), tracePlan.getCustomer()
                    .getCustName());
                tracePlanInfoAdapter.setItem(getString(R.string.custMobile), tracePlan
                    .getCustomer().getCustMobile());
                tracePlanInfoAdapter.setItem(getString(R.string.custOtherPhone), tracePlan
                    .getCustomer().getCustOtherPhone());
            }
            showEdit();
        }
        //销售线索进入不显示线索信息
        if (!ShowBottonBar) {
            oppoListLinearLayout.setVisibility(View.GONE);
        }
        return view;
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
        leaderItem = menu.findItem(R.id.approval);
        if (mComeFromOppo || isComeFromCustManager) {
            leaderItem.setVisible(false);
        } else {
            leaderItem.setVisible(true);
        }
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
            case R.id.approval:
                //判断网络状况
                if (EventReceiver.isNetworkUnavailable()) {
                    Toast.makeText(getActivity(), getString(R.string.network_error),
                        Toast.LENGTH_SHORT).show();
                    break;
                }
                showEdit();
                if (!StringUtils.isEmpty(tracePlanInfoAdapter.getDataList().get(9).value)) {
                    refreshFollowInfoList(false);
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
     * @param flag 判断显示领导批示还是所有数据
     *
     */
    private void refreshFollowInfoList(boolean flag) {
        if (mFollowListView != null && tracePlanInfoAdapter != null) {
            mFollowListView.removeAllViews();
            int count = 0;
            if (!b) {
                if(flag){
                    count = tracePlanInfoAdapter.getCount();
                    for (int i = 0; i < count; i++) {
                        mFollowListView.addView(tracePlanInfoAdapter.getView(i, null, null));
                        View dividerView = new View(getActivity());
                        dividerView.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, 1));
                        dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                        mFollowListView.addView(dividerView);
                    }
                }else{
                    count = 1;
                    for (int i = 0; i < count; i++) {
                        mFollowListView.addView(tracePlanInfoAdapter.getView(9, null, null));
                        View dividerView = new View(getActivity());
                        dividerView.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, 1));
                        mFollowListView.addView(dividerView);
                    }
                }
            } else {
                count = 1;
                for (int i = 0; i < count; i++) {
                    mFollowListView.addView(tracePlanInfoAdapter.getView(i, null, null));
                    View dividerView = new View(getActivity());
                    dividerView.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, 1));
                    mFollowListView.addView(dividerView);
                }
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
     * @version $Id: SmFollowPlanFragment.java, v 0.1 2013-5-24 上午10:50:05 shuang.gao Exp $
     */
    public class OppoTask extends BaseTask<Project> {
        public OppoTask(Activity activity) {
            super(activity);
        }

        String tag = Log.getTag(OppoTask.class);

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
                if (tracePlan.getCustomer().getProjectID() != null) {
                    lp = manager.getProjectInfo(tracePlan.getCustomer().getProjectID(), tracePlan
                        .getCustomer().getCustomerID());
                }
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
                    project.setProjectID(projectId);
                    oppoinfoAdapter.addView(getActivity().getString(R.string.brand_1), result
                        .getPurchaseCarIntention().getBrand());
                    oppoinfoAdapter.addView(getActivity().getString(R.string.model), result
                        .getPurchaseCarIntention().getModel());
                    oppoinfoAdapter.addView(getActivity().getString(R.string.flowStatus), result
                        .getPurchaseCarIntention().getFlowStatus());
                    oppoinfoAdapter.addView(getActivity().getString(R.string.preorderDate),
                        DateFormatUtils.formatDate(result.getPurchaseCarIntention()
                            .getPreorderDate()));
                    oppoinfoAdapter.addView(getActivity().getString(R.string.contacter_tab), result
                        .getCustomer().getCustName());
                    oppoinfoAdapter.addView(getActivity().getString(R.string.contacter_phone),
                        result.getCustomer().getCustMobile());
                }
                displayOppo();
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
     * @version $Id: SmFollowPlanFragment.java, v 0.1 2013-5-24 下午2:55:54 shuang.gao Exp $
     */
    public class UpdateTracePlanTask extends BaseTask<Boolean> {

        private String tag = Log.getTag(UpdateTracePlanTask.class);

        public UpdateTracePlanTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean b = null;
            RoilandCRMApplication application = (RoilandCRMApplication) activity.getApplication();
            try {
                CRMManager manager = application.getCRMManager();
                tracePlanInfoAdapter.updateTracePlan();
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
                    intent.putExtras(bundle);
                    getActivity().setResult(4, intent);
                    Toast.makeText(getActivity(), R.string.opt_success, Toast.LENGTH_SHORT).show();
                    tracePlanInfoAdapter.showEdit();
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), R.string.opt_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 
     * <pre>
     * 通过此方法判断应该进行编辑操作还是保存操作
     * EditData判断是否为编辑状态
     * flag判断是否为编辑状态
     * b判断是否为编辑状如果是编辑状态显示新建跟踪计划，如果不是进行保存操作
     * 在保存是如果跟踪计划执行状态为已完成，但是没建立新的跟踪计划给出相应的提示
     * </pre>
     *
     */
    public void showEdit() {
        b = false;
        if (flag == false) {
            if (EditData) {
                if (tracePlan != null) {
                    b = tracePlanInfoAdapter.showEdit();
                    tracePlanInfoAdapter.notifyDataSetChanged();
                    flag = b;
                } else {
                    b = !tracePlanInfoAdapter.editFlag;
                    flag = b;
                }
            } else {
                b = tracePlanInfoAdapter.showEdit();
                flag = b;
            }

            refreshFollowInfoList(false);
            if (b) {
                leaderItem.setIcon(R.drawable.oppoinfo_save_btn);
                oppoListLinearLayout.setVisibility(View.GONE);
            }
        }

        if (b) {
            flag = true;
            //编辑时更改按钮图片等
            mBottomBtn.setVisible(false);
            leaderItem.setIcon(R.drawable.oppoinfo_save_btn);
        } else {
            String followMessage = followInfoValidation();
            //保存前校验
            if (!StringUtils.isEmpty(followMessage)) {
                Toast.makeText(getActivity(), followMessage, Toast.LENGTH_SHORT).show();
                return;
            } else {
                refreshFollowInfoList(false);
                if (tracePlan.getActivityID() != null) {
                    //判断网络状况
                    if (EventReceiver.isNetworkUnavailable()) {
                        Toast.makeText(getActivity(), getString(R.string.network_error),
                            Toast.LENGTH_SHORT).show();
                       return;
                    }
                    tracePlanInfoAdapter.updateTracePlan();
                    new UpdateTracePlanTask(getActivity()).execute();
                   
                }
            }
        }
    }

    public void displayOppo() {
        for (int i = 0; i < oppoinfoAdapter.getCount(); i++) {
            oppoList.addView(oppoinfoAdapter.getView(i, null, null));
            View dividerView = new View(getActivity());
            dividerView
                .setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
            dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
            oppoList.addView(dividerView);
        }
    }

    /**
     * 
     * <pre>
     * 编辑跟踪计划部分
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: SmFollowPlanFragment.java, v 0.1 2013-5-24 下午2:56:45 shuang.gao Exp $
     */
    class InfoListAdapter extends BaseAdapter {
        ArrayList<BasicInfoListAdapter.Info> traceInfoList;
        Context                              context;
        int                                  size;
        public boolean                       editFlag      = false;
        TextView                             keyText       = null;
        TextView                             valueText     = null;
        EditText                             valueEdit     = null;
        ImageButton                          callButton    = null;
        LinearLayout                         value_display = null;
        LinearLayout                         value_edit    = null;

        // 为跟踪计划对象中添加信息
        public void updateTracePlan() {
            int i = 0;
            if (bshowcustomerinfo) {
                i = 3;
            }
            tracePlan.setActivityTypeCode(traceInfoList.get(0 + i).pairKey);
            tracePlan.setActivityType(traceInfoList.get(0 + i).value);
            tracePlan
                .setExecuteTime(DateFormatUtils.parseDateToLong(traceInfoList.get(1 + i).value));
            tracePlan.setExecuteStatusCode(traceInfoList.get(2 + i).pairKey);
            tracePlan.setExecuteStatus(traceInfoList.get(2 + i).value);
            tracePlan.setActivityContent(traceInfoList.get(3 + i).value);
            tracePlan.setContactResultCode(traceInfoList.get(4 + i).pairKey);
            tracePlan.setContactResult(traceInfoList.get(4 + i).value);
            tracePlan.setCustFeedback(traceInfoList.get(5 + i).value);
            tracePlan.setLeaderComment(traceInfoList.get(6 + i).value.trim());

            Log.i("InfoListAdapter", traceInfoList.get(0 + i).pairKey);
            Log.i("InfoListAdapter", traceInfoList.get(0 + i).value);
            Log.i("InfoListAdapter", traceInfoList.get(1 + i).value);
            Log.i("InfoListAdapter", traceInfoList.get(2 + i).pairKey);
            Log.i("InfoListAdapter", traceInfoList.get(2 + i).value);
            Log.i("InfoListAdapter", traceInfoList.get(3 + i).value);
            Log.i("InfoListAdapter", traceInfoList.get(4 + i).pairKey);
            Log.i("InfoListAdapter", traceInfoList.get(4 + i).value);
            Log.i("InfoListAdapter", traceInfoList.get(5 + i).value);
            Log.i("InfoListAdapter", traceInfoList.get(6 + i).value);
        }

        //改变当前的状态，是编辑状态还是查看状态
        public boolean showEdit() {
            editFlag = !editFlag;
            return editFlag;
        }

        public ArrayList<BasicInfoListAdapter.Info> getDataList() {
            return traceInfoList;
        }

        // 把页面中应该显示的信息添加到traceInfoList中
        private void initKeyList() {
            if (traceInfoList == null)
                this.traceInfoList = new ArrayList<BasicInfoListAdapter.Info>();
            String list[] = null;
            list = infokeylist1;
            for (int i = 0; i < list.length; i++) {
                BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
                info.key = list[i];
                info.value = "";
                info.pairKey = null;
                traceInfoList.add(info);
            }
        }

        public InfoListAdapter(Context context) {
            this(null, context);
        }

        public InfoListAdapter(ArrayList<BasicInfoListAdapter.Info> traceInfoList, Context context) {
            this.traceInfoList = traceInfoList;
            this.context = context;
            initKeyList();
        }

        public void setItem(String item, String data) {
            setItem(item, data, null);
        }

        //把页面中显示的数据添加到traceInfoList中
        public void setItem(String item, String data, String pairKey) {
            for (int i = 0; i < traceInfoList.size(); i++) {
                BasicInfoListAdapter.Info info = traceInfoList.get(i);
                if (info.key.equals(item.replaceAll(":", ""))) {
                    Log.i("InfoListAdapter setItem", "item=" + item.replaceAll(":", "")
                                                     + "  value=" + data + "  code=" + pairKey);
                    info.value = data;
                    info.pairKey = pairKey;
                }
            }
        }

        @Override
        public int getCount() {
            size = traceInfoList.size();
            return size;
        }

        @Override
        public Object getItem(int arg0) {
            if (arg0 < traceInfoList.size()) {
                return traceInfoList.get(arg0);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        /**
         * 查看信息加载页面调用次方法，把页面中应该显示的信息和对应的数据显示出来
         * @param arg0
         * @param convertView 
         * @param arg2
         * @return
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {

            if (!editFlag) {
                //显示页面
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                        R.layout.sm_follow_plan_item, null);
                    keyText = (TextView) convertView.findViewById(R.id.keyItem);
                    valueText = (TextView) convertView.findViewById(R.id.valueItem);
                }
                valueText.setVisibility(View.VISIBLE);
                keyText.setText(traceInfoList.get(arg0).key + ":");
                valueText.setText(traceInfoList.get(arg0).value);
            } else {
                //编辑页面
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                        R.layout.sm_follow_plan_item_edit, null);
                    keyText = (TextView) convertView.findViewById(R.id.keyItem);
                    valueEdit = (EditText) convertView.findViewById(R.id.editItem);
                    valueEdit.addTextChangedListener(textWatcher);
                }
                valueEdit.setVisibility(View.VISIBLE);
                keyText.setText(traceInfoList.get(9).key + ":");
                valueEdit.setText(traceInfoList.get(9).value.trim());

            }

            return convertView;
        }

        public void clearData() {
            for (int i = 0; i < traceInfoList.size(); i++) {
                traceInfoList.remove(i);
                this.notifyDataSetChanged();
            }
        }

        //编辑框改变监听事件
        private TextWatcher textWatcher = new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s,
                                                                          int start, int count,
                                                                          int after) {

                                                Log.d("TAG", "beforeTextChanged--------------->");
                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start,
                                                                      int before, int count) {
                                                String str = valueEdit.getText().toString();
                                                tracePlanInfoAdapter.setItem(keyText.getText()
                                                    .toString(), str.trim());
                                            }

                                            @Override
                                            public void afterTextChanged(Editable arg0) {

                                                Log.d("TAG", "afterTextChanged--------------->");
                                            }
                                        };
    }

    class OppInfoAdapter extends BaseAdapter {

        ArrayList<BasicInfoListAdapter.Info> followOppoInfoList;
        Context                              context;

        public OppInfoAdapter(Context context) {
            this.context = context;
            if (followOppoInfoList == null) {
                followOppoInfoList = new ArrayList<BasicInfoListAdapter.Info>();
            }
        }

        @Override
        public int getCount() {
            return followOppoInfoList != null ? followOppoInfoList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return followOppoInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.sm_follow_plan_item,
                    null);
            }
            if (convertView != null) {
                TextView key = (TextView) convertView.findViewById(R.id.keyItem);
                TextView value = (TextView) convertView.findViewById(R.id.valueItem);
                key.setText(followOppoInfoList.get(position).key + ":");
                value.setText(followOppoInfoList.get(position).value);
                value.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        public void addView(String key, String value) {
            BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
            info.key = key;
            info.value = value;
            followOppoInfoList.add(info);
        }

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
        ArrayList<BasicInfoListAdapter.Info> tracePlanList = tracePlanInfoAdapter.getDataList();
        String errString = null;
        BasicInfoListAdapter.Info info = tracePlanList.get(9);
        errString = DataVerify.infoValidation(info.key, info.value, null, null, null, null,
            getActivity(), false, EditData,false);
        if (!StringUtils.isEmpty(errString)) {
            return errString;
        }
        return null;
    }

    @Override
    public void chagedData(String key, String value) {
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
}
