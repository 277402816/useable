package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.Project.AdvancedSearch;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.EventReceiver;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 销售漏斗详细列表Fragment
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmSalesFunnelFragment.java, v 0.1 2013-5-24 下午2:51:09 shuang.gao Exp $
 */
public class SmSalesFunnelFragment extends SherlockFragment implements  OnScrollListener{

    private String tag = Log.getTag(SmSalesFunnelFragment.class);
    public boolean          flag              = false; //判断状态
    public MenuItem   viewFunnelItem ;
    private List<Project>        projectList          ; //客戶信息
    int lastposition =0;
    private ListView layout ;
    private SalesFunnelAdapter salesFunnelAdapter;
    private String StartTime;
    private String EndTime;
    private int beginnum = 0;
    private int DEFAULT_PAGE_SIZE = 20;
    private int percount = DEFAULT_PAGE_SIZE;
    private int currentPage = 0;
    SalesFunnelTask task;
    protected Boolean isDownloadThreadRunning = false;
    private boolean loadingbarviewable = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sm_oppo_funnel, container, false);
        Intent intent = getActivity().getIntent();
        StartTime = intent.getStringExtra("startTime");
        EndTime = intent.getStringExtra("endTime");
        //Search(StartTime,EndTime);
        task = new SalesFunnelTask(getActivity(), false, null,null);
        task.execute();
        layout = (ListView) view.findViewById(R.id.funnel_info_list);
        layout.setOnScrollListener(this);

        salesFunnelAdapter = new SalesFunnelAdapter(getActivity());
        layout.setAdapter(salesFunnelAdapter);
        return view;
    }
    public List<Project> getPjList() {
        return projectList;
    }

    public void setPjList(List<Project> projectList) {
        this.projectList = projectList;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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
        viewFunnelItem = menu.findItem(R.id.view_funnel);
        viewFunnelItem.setVisible(true);
      
    }
    
    /**
     * 菜单被选中事件
     * @param item 被选中菜单项
     * @return
     * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_funnel:
                //判断网络状况
                if (EventReceiver.isNetworkUnavailable()) {
                  Toast.makeText(getActivity(), getString(R.string.network_error),
                      Toast.LENGTH_SHORT).show();
                  break;
              }

                //新建intent传递
                  Intent intent = new Intent(getActivity(), SmSalesFunnelPicActivity.class);
                  intent.putExtra("startTime", StartTime);
                  intent.putExtra("endTime", EndTime);
                  startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
 

    /**
     * 
     * <pre>
     * 销售漏斗搜索方法Adapter
     * </pre>
     *
     * @author Administrator
     * @version $Id: SmSalesFunnelFragment.java, v 0.1 2013-6-8 下午4:44:56 Administrator Exp $
     */
    public class SalesFunnelAdapter extends BaseAdapter{
        
        Context context;
        public SalesFunnelAdapter(Context context){
            this.context = context;
            if(projectList == null){
                projectList = new ArrayList<Project>();
            }
        }
        @Override
        public int getCount() {
            return projectList != null ? projectList.size():0;
        }

        @Override
        public Object getItem(int position) {
            return projectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.sm_oppo_funnel_item,null);
            }
            if(convertView != null){
                TextView owner = (TextView)convertView.findViewById(R.id.owner);
                TextView custName = (TextView)convertView.findViewById(R.id.cust_name);
                TextView followStatus = (TextView)convertView.findViewById(R.id.follow_status);
                TextView purchaseCar = (TextView)convertView.findViewById(R.id.purchase_car);
                TextView dealPossibility = (TextView)convertView.findViewById(R.id.deal_possibility);
                TextView num = (TextView)convertView.findViewById(R.id.num);
                owner.setText(projectList.get(position).getPurchaseCarIntention().getEmployeeName());
                custName.setText(projectList.get(position).getCustomer().getCustName());
                followStatus.setText(projectList.get(position).getPurchaseCarIntention().getFlowStatus());
                purchaseCar.setText(projectList.get(position).getPurchaseCarIntention().getBrand());
                dealPossibility.setText(projectList.get(position).getPurchaseCarIntention().getDealPossibility());
                num.setText(projectList.get(position).getPurchaseCarIntention().getPreorderCount());
                
            }
            return convertView;
        }
        protected View getLoadingView(int position, View convertView, ViewGroup parent) {
            
                convertView = LayoutInflater.from(context).inflate(R.layout.loadingbar, null);
                convertView.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
                convertView.setBackgroundResource(R.color.transparent);
                ((TextView)(convertView.findViewById(R.id.loadingtext))).setText(R.string.wait_for_add);
            
            return convertView;
        }
        
    }

    /**
     * 异步Task线程处理，调用API。
     * @extents BaseTask
     */
    
    private class SalesFunnelTask extends BaseTask<List<Project>> {
        String tag = Log.getTag(SalesFunnelTask.class);

        public SalesFunnelTask(Activity activity,Boolean displayLoadingDown, AdvancedSearch advancedSearch,String flag) {
            super(activity, "", true, true,  displayLoadingDown);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // 如果下标重头开始并且现有列表有数据，要清空现有列表
            if (beginnum == 0 && projectList != null ) {
                projectList.clear();
            }
        }
        
        @Override
        protected List<Project> doInBackground(String... params) {
            List<Project> lp = null;
            //调用API
            try {
                RoilandCRMApplication application = (RoilandCRMApplication) activity.getApplication();
                CRMManager manager = application.getCRMManager();
                    lp = manager.searchSalesOppoFunncelList(DateFormatUtils.parseDateToLong(StartTime), DateFormatUtils.parseDateToLong(EndTime) +24*60*60*1000, beginnum, percount);
            
                Log.d(tag, "doInBackground ==> manager.searchSalesOppoFunncelList finish.");
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
            Log.i(tag, "onPostExecute  => " + result);
            if (result != null) {
                if (currentPage == 0 && projectList == null) {
                    projectList = result;
                    currentPage++;
                } else {
                    projectList.addAll(result);
                    currentPage++;
                    
                }
                loadingbarviewable = false;
                for (int i = 0; i < projectList.size(); i ++) {
                    int listSize = projectList.size();
                    if (i == listSize-1 && listSize % DEFAULT_PAGE_SIZE == 0 && listSize > 10) {
                        loadingbarviewable = true;
                    } else{
                        loadingbarviewable = false;
                    }
                }
                
                salesFunnelAdapter.notifyDataSetChanged();
               
            } else if (responseException != null) {
                result = new ArrayList<Project>();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else if(result == null || result.size() == 0){
                return;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

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
            lastposition = layout.getLastVisiblePosition();
    
            int count = currentPage * DEFAULT_PAGE_SIZE;
            if (lastposition == count-1) {
                Log.i(tag, "onScroll: " + "begin load ");
                if(beginnum==0){
                    beginnum = beginnum + DEFAULT_PAGE_SIZE;
                }else{
                    beginnum = beginnum + DEFAULT_PAGE_SIZE;
                  
                }
                if (task != null)
                    task.cancel(true);
                task = new SalesFunnelTask(getActivity(),false, null,null);
                task.execute();
            }
            break;

        }
    
    }
    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
    }
}
