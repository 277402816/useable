package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 客户管理下购车信息Fragment
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ScPurchaseCarListFragment.java, v 0.1 2013-7-16 上午9:35:03 shuang.gao Exp $
 */
public class ScPurchaseCarListFragment extends BaseListFragment {
    private final static String tag = ScPurchaseCarListFragment.class.getSimpleName();
    List<Project>               purchaseCarList;
    private MyListAdapter       adapter;
    public Activity             activity;
    private String              customerID;
    private Customer            customerInfo;

    public ScPurchaseCarListFragment() {
        super();
    }

    public ScPurchaseCarListFragment(Customer c) {
        customerInfo = c;
        customerID = customerInfo.getCustomerID();
    }

    public void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        PurchaseCarTask task = new PurchaseCarTask(activity);
        task.execute();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        purchaseCarList = new ArrayList<Project>();
        displayList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getListView().setBackgroundResource(R.drawable.list_item_background);
        this.setHasOptionsMenu(true);
    }

    /**
     * 
     * <pre>
     * 异步方法，获取通过客户id获取销售线索信息
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: ScPurchaseCarListFragment.java, v 0.1 2013-7-16 上午9:35:34 shuang.gao Exp $
     */
    private class PurchaseCarTask extends BaseTask<List<Project>> {
        public PurchaseCarTask(Activity activity) {
            super(activity);
        }

        private String tag = Log.getTag(PurchaseCarTask.class);

        @Override
        protected List<Project> doInBackground(String... arg0) {
            List<Project> tp = null;
            try {
                RoilandCRMApplication application = (RoilandCRMApplication) activity
                    .getApplication();
                CRMManager manager = application.getCRMManager();
                tp = manager.getCustomerProjectList(customerID);
            } catch (ResponseException e) {
                Log.e(tag, "Get FollowPlan list failure.", e);
                responseException = e;
            }
            return tp;
        }

        @Override
        protected void onPostExecute(List<Project> r) {

            Log.i(tag, "onPostExecute  => " + r);
            super.onPostExecute(r);
            if (responseException != null) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), responseException.getMessage(),
                        Toast.LENGTH_SHORT).show();
                }
            } else if (r != null) {
                purchaseCarList = r;
                Log.i(tag, "onPostExecute  => " + String.valueOf(purchaseCarList.size()));

                displayList();
            }
        }
    }
    /**
     * 创建菜单
     * @param menu
     * @param inflater
     * @see com.roiland.crm.sm.ui.view.BaseListFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        MenuItem addItem = menu.findItem(R.id.add);
        addItem.setVisible(true);
    }
    /**
     * 菜单点击事件
     * @param item 点击那个菜单项
     * @return
     * @see com.actionbarsherlock.app.SherlockListFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(getActivity(), ScPurchaseCarInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("customer", customerInfo);
                intent.putExtras(bundle);
                intent.putExtra("addFlag", true);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 
     * <pre>
     * 将销售线索中的购车信息显示出来
     * </pre>
     *
     */
    public void displayList() {
        adapter = new MyListAdapter(activity);
        adapter.setCaches(purchaseCarList);
        setListAdapter(adapter);
    }

    /**
     * item单击事件
     * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
            Intent intent = new Intent(activity, ScPurchaseCarInfoActivity.class);
            Project p = purchaseCarList.get(position);
            Bundle bundle = new Bundle();
            bundle.putParcelable("purchaseProject", p);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    /**
     * 
     * <pre>
     * 显示购车列表Adapter
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: ScPurchaseCarListFragment.java, v 0.1 2013-8-6 上午10:09:46 shuang.gao Exp $
     */
     class MyListAdapter extends BaseCacheListAdapter<Project> {
        public MyListAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            return super.getView(arg0, arg1, arg2, R.layout.sc_purchase_car_info_list_item);
        }

        /**
         * 信息添加
         * @see com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter#fillView(android.view.View, java.lang.Object)
         */
        protected boolean fillView(View view, Project item) {
            try {
                ((TextView) view.findViewById(R.id.purchase_car_info_list_vehicle_type))
                    .setText(item.getPurchaseCarIntention().getBrand());
                ((TextView) view.findViewById(R.id.purchase_car_info_list_vehicle_model))
                    .setText(item.getPurchaseCarIntention().getModel() !="null" ? item.getPurchaseCarIntention().getModel() : null);
                ((TextView) view.findViewById(R.id.purchase_car_info_list_date))
                    .setText(DateFormatUtils.formatDate(item.getPurchaseCarIntention()
                        .getPreorderDate()));
                ((TextView) view.findViewById(R.id.purchase_car_info_list_purchasestatus))
                    .setText(item.getPurchaseCarIntention().getFlowStatus());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

