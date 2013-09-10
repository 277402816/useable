package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScProjectListAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售线索列表
 * </pre>
 *
 * @author liuyu
 * @version $Id: ScOppoListFragment.java, v 0.1 2013-7-1 下午1:55:58 liuyu Exp $
 */
public class ScOppoListFragment extends BaseRefreshListFragment{
    private final static String tag = Log.getTag(ScOppoListFragment.class);
    boolean mDualPane;
    int mCurCheckPosition = 0;
    int currentPage = 0;
    
    //判断调用获取列表的接口
    public static String loadFlag;
    ScProjectListAdapter projectAdapter;
    List<Project> projectlist = null;
    String searchText = null;
    String searchColumns = "custName,custMobile,custOtherPhone";
    private int beginnum = 0;
    private int percount = DEFAULT_PAGE_SIZE;
    Map<String, List<Project>> projectMap = new HashMap<String, List<Project>>();
    List<String> sectionKeyList = new ArrayList<String>();
    OppoTask task = null;
    private boolean searchstatus = false;
    private boolean loadingbarviewable = false;
    
    //确定新添加状态
    private boolean flagIsNew = false;
   
    private ScProjectListAdapter listAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        if (task != null)
            task.cancel(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        //初期化检索
        if(flagIsNew)
            return;
        init();
    }

    @Override
    public void onResume() {
        
        super.onResume();
        //根据标记判断回调后是否重新加载
        Log.d(tag, "Fragment-->onResume");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(tag, "Fragment-->onActivityCreted");

        if (listAdapter == null) {
            listAdapter = new ScProjectListAdapter(activity);
            setListAdapter(listAdapter);
        }
        
        if (projectAdapter == null)
            projectAdapter = new ScProjectListAdapter(activity);

        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        
    }
    
    /**
     * 
     * <pre>
     * 初期检索
     * </pre>
     *
     * @param advancedSearch
     */
    public void init() {
        searchstatus = false;
        currentPage = 0;
        if (projectlist != null)
            projectlist.clear();
        if (projectMap != null)
            projectMap.clear();
        if (sectionKeyList != null)
            sectionKeyList.clear();
        listAdapter.removeAll();
        listAdapter.loadingbarViewable = false;
        setListAdapter(listAdapter);
        //开始条目
        beginnum = 0; 
        //检索关键字
        searchText = null; 
        if (task != null)
            task.cancel(true);
        task = new OppoTask(this.activity, false,loadFlag);
        task.execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (loadingbarviewable && position == getListView().getLastVisiblePosition() + 1)
            return;
        //回调item单击事件
        super.onListItemClick(l, v, position, id);
        //新建intent传递
        Intent intent = new Intent(((BaseActivity) activity), ScOppoInfoActivity.class);
        //新建Bundle传递
        Bundle bundle = new Bundle();
        Project item = (Project) listAdapter.getItem(position);
        bundle.putParcelable("projectinfo",item);
        //将传递的数据放入intent
        intent.putExtras(bundle);
        intent.putExtra("ShowBottonBar", true);
        if(item.getCustomer().getHasUnexePlan() != null && item.getCustomer().getHasUnexePlan().equals("false")){
            intent.putExtra("IsNeedUpdateList", true);
        }
        //标识为更新销售线索
        startActivityForResult(intent, 1); 
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Fragment-->onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //更新标示
        flagIsNew = false;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice", mCurCheckPosition);
    }
    
    /**
     * 
     * <pre>
     * 更新列表
     * </pre>
     *
     */
    public void updateOppListFragment(){
        //调用异步线程，链接API处理
        flagIsNew = true;
        OppoTask task = new OppoTask(this.activity,false,loadFlag);
        task.execute();
    }
    
    /**
     * 异步Task线程处理，调用API。
     * @extents BaseTask
     */
    
    private class OppoTask extends BaseTask<List<Project>> {
        String tag = Log.getTag(OppoTask.class);
        String flag;

        public OppoTask(Activity activity,Boolean displayLoadingDown,String flag) {
            super(activity, "", true, true,  displayLoadingDown);
            this.flag = flag;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // 如果下标重头开始并且现有列表有数据，要清空现有列表
            if (beginnum == 0 && projectlist != null && !searchstatus) {
                projectlist.clear();
            }
        }
        
        @Override
        protected List<Project> doInBackground(String... params) {
            List<Project> lp = null;
            searchstatus = true;
            //调用API
            try {
                RoilandCRMApplication application = (RoilandCRMApplication) activity.getApplication();
                CRMManager manager = application.getCRMManager();
                if("1".equals(flag)){
                    lp = manager.getTodayProjectList(searchText, searchColumns, beginnum, percount, "3");
//                    loadFlag = null;删掉是因为如果条目太多下拉刷新后就查询所有
                }else if("0".equals(flag)){
                    lp = manager.getProjectList(searchText, searchColumns,"1", beginnum, percount,null);
//                    loadFlag = null;
                }else{
                    lp = manager.getProjectList(searchText, searchColumns,null, beginnum, percount, null);
                }
                Log.d(tag, "doInBackground ==> manager.getProjectList finish.");
            } catch (ResponseException e) {
                Log.e(tag, "Get project list failure.", e);
                responseException = e;
            }
            return lp;
        }

        @Override
        protected void onPostExecute(List<Project> result) {
            //对调用API返回结果的处理
            super.onPostExecute(result);
            listAdapter.loadingbarViewable = false;
            Log.i(tag, "onPostExecute  => " + result);
            if(responseException != null){
                result = new ArrayList<Project>();
                //当时间超过30分钟则跳转到首页
                if (responseException.getStatusCode() == StatusCodeConstant.UNAUTHORIZED) {
                    DialogUtils.overMinuteDo(getActivity(),responseException);
                } else {
                    Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show(); 
                }
            
                return;
            }
            completeRefresh();
            if (result != null) {
                if (currentPage == 0 && projectlist == null) {
                    projectlist = result;
                    currentPage++;
                } else {
                    projectlist.addAll(result);
                    currentPage++;
                }
            } else if(result == null || result.size() == 0){
                return;
            }
            //对销售线索列表处理
            displayContent(projectlist,false);
            isDownloadThreadRunning = false;
            searchstatus = false;
        }

        @Override
        protected void onCancelled() {
            isDownloadThreadRunning = false;
            searchstatus = false;
            super.onCancelled();
        }

    }
    
    /**
     * 
     * <pre>
     * 加载销售线索列表
     * </pre>
     *
     * @param projectList 销售线索列表list
     * @param isNew 判断状态
     */
    public synchronized void displayContent(List<Project> projectList,boolean flagIsNew) {
        Log.i(tag, "--------"+projectList.size()+"----------");
        if (sectionKeyList == null || projectMap == null)
            return;
        sectionKeyList.clear();
        projectMap.clear();

        if (projectList == null || projectList.size() == 0){
            listAdapter.removeAll();
            return;
        }
    
        //遍历销售线索列表数据
        for (int i = 0; i < projectList.size(); i ++) {
            Project p = projectList.get(i);
            String key = "";
            //获取每条数据的预购日期并且判断，如果已经废弃则预购日期为空。
            if ((("0").equals(p.getPurchaseCarIntention().getAbandonFlag())||null ==p.getPurchaseCarIntention().getAbandonFlag())) {
                key = DateFormatUtils.formatDate(p.getPurchaseCarIntention().getCreateDate());
            }else{
                key = "";
            }
            //将销售线索按照预购日期分区域表示
            List<Project> subList = projectMap.get(key);
            if (subList == null) {
                subList = new ArrayList<Project>();
                projectMap.put(key, subList);
                sectionKeyList.add(key);
            }
            subList.add(p);
        }
        loadingbarviewable = false;
        listAdapter.removeAll();
        
        //列表下拉到低端之后读取效果处理
        for (int i = 0; i < sectionKeyList.size(); i ++) {
            int listSize = flagIsNew ? projectList.size() - 1 : projectList.size();
            if (i == sectionKeyList.size() - 1 && listSize % DEFAULT_PAGE_SIZE == 0 && listSize > 10) {
                listAdapter.loadingbarViewable = true;
                loadingbarviewable = true;
            } else
                listAdapter.loadingbarViewable = false;
                if(flagIsNew){
                    if(i==0){
                        List<Project> list = new ArrayList<Project>();
                        list.add(projectList.get(0));
                        listAdapter.addSection(getString(R.string.nearest_add), list);
                    }else{
                        listAdapter.addSection(sectionKeyList.get(i), projectMap.get(sectionKeyList.get(i)));
                    }
                }else{
                    listAdapter.addSection(sectionKeyList.get(i), projectMap.get(sectionKeyList.get(i)));
                }
        }
        listAdapter.notifyDataSetChanged();
    }
    /**
     * 每页显示固定条目，当列表拉到底部后调用。继续加载新条目。
     * 
     * @param view AbsListView
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {

        case OnScrollListener.SCROLL_STATE_IDLE:
            int lastposition = getListView().getLastVisiblePosition();
            if (flagIsNew) {
                lastposition = lastposition - 1;
            }
            int keysize = projectMap.keySet().size();
            int count = currentPage * DEFAULT_PAGE_SIZE;
            if (lastposition - keysize == count) {
                Log.i(tag, "onScroll: " + "begin load ");
//                if(beginnum==0){
//                    beginnum = beginnum + DEFAULT_PAGE_SIZE + 1;
//                }else{
                    beginnum = beginnum + DEFAULT_PAGE_SIZE;
//                }
                if (searchstatus)
                    break;
                if (task != null)
                    task.cancel(true);
                task = new OppoTask(this.activity,true,loadFlag);
                task.execute();
            }
            break;

        }
    }
    
    /**
     * 点击检索标签，根据关键字检索
     * @param query 检索关键字
     * @return 是否成功
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onQueryTextSubmit(java.lang.String)
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (searchstatus)
            return false;
        if(StringUtils.isEmpty(query)){
            listAdapter.removeAll();
            listAdapter.notifyDataSetChanged();
            listAdapter.loadingbarViewable = false;
        }else{
            searchText = query.trim();
            beginnum = 0;
            currentPage = 0;
            if (projectlist != null)
                projectlist.clear();
            listAdapter.loadingbarViewable = false;
            listAdapter.removeAll();
            if (task != null)
                task.cancel(true);
            task = new OppoTask(this.activity,false,loadFlag);
            task.execute();
        }
        return true;
    }
    
    /**
     * 初期检索
     * 
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#defaultSearch()
     */
    @Override
    public void defaultSearch() {
        if (searchstatus)
            return;
        searchText = null;
        beginnum = 0;
        currentPage = 0;
        if (projectlist != null)
            projectlist.clear();
        listAdapter.loadingbarViewable = false;
        listAdapter.removeAll();
        if (task != null)
            task.cancel(true);
        if(isRefreshing){
            task = new OppoTask(this.activity, true,loadFlag);
        }else{
            task = new OppoTask(this.activity, false,loadFlag);
        }
        
        task.execute();
    }
    
    /**
     * 下拉刷新时调用此方法
     * @param view 显示视图
     * @see com.roiland.crm.sm.ui.view.BaseRefreshListFragment#onRefresh(android.view.View)
     */
    @Override
    public void onRefresh(View view) {
       
        super.onRefresh(view);
        beginnum = 0;
        currentPage = 0;
        flagIsNew = false;
        if (task != null)
            task.cancel(true);
        task = new OppoTask(this.activity, true,null);
        task.execute();
    
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item1 = menu.findItem(R.id.search);
        MenuItem item2 = menu.findItem(R.id.add);
        item1.setVisible(true);
        item2.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Log.i(tag, ""+item.getItemId());
                mSearchMode1 = true;
                setSearchMode(true);
                break;

            case R.id.add:
                Log.i(tag, ""+item.getItemId());
                Intent intent = new Intent(getActivity(),ScOppoInfoActivity.class);
                intent.putExtra("addFlag", true);
                startActivityForResult(intent, 1);
                return true;
            case R.id.cancel:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void setFlag(String flag) {
        this.loadFlag = flag;
    }
    
    /**
     * 接收编辑或新建的返回数据 用于更新列表
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data 返回数据
     * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(tag, "==================onActivityResult== resultCode==" + String.valueOf(resultCode) + "  requestCode==" + String.valueOf(requestCode));
              if (resultCode == 10) {
                  Project project = data.getParcelableExtra("newprojectinfo");
                  if(project != null){
                      addProjectInfo(project);
                  }  
              }else if(resultCode == 11){
                  Project project = data.getParcelableExtra("newprojectinfo");
                  setCurrentItem(project);
                  updateOppListFragment();
              }
    }
    /**
     * 
     * <pre>
     * 将新建销售线索添加到列表第一列
     * </pre>
     *
     * @param p 销售线索信息实体类
     */
    public void addProjectInfo(Project p) {
        List<Project> pp = new ArrayList<Project>();
        pp.add(p);
        for (int i = 0; i < projectlist.size(); i++) {
            pp.add(projectlist.get(i));
        }
        projectlist = pp;
        Log.i(tag, "addProjectInfo:" + String.valueOf(projectlist.size()));
        flagIsNew = true;
        //新建销售线索后列表处理
        displayContent(projectlist,true);
    }
    /**
     * 
     * <pre>
     * 更新编辑后的销售线索列表信息
     * </pre>
     *
     * @param peoject 销售线索信息实体类
     */
    public void setCurrentItem(Project peoject) {
        String editCustId = peoject.getCustomer().getCustomerID();
        for (Project pj : projectlist) {
            if (!pj.getCustomer().getCustomerID().equals(editCustId))
                continue;
            pj.getCustomer().setCustName(peoject.getCustomer().getCustName());
            pj.getCustomer().setCustMobile(peoject.getCustomer().getCustMobile());
            pj.getCustomer().setHasUnexePlan(peoject.getCustomer().getHasUnexePlan());
            pj.getPurchaseCarIntention().setBrand(peoject.getPurchaseCarIntention().getBrand());
            pj.getPurchaseCarIntention().setModel(peoject.getPurchaseCarIntention().getModel());
            pj.getPurchaseCarIntention().setFlowStatus(peoject.getPurchaseCarIntention().getFlowStatus());
            break;
        }
        //编辑销售线索后列表处理
        displayContent(projectlist,false);
    }
}
