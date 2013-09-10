package com.roiland.crm.sm.ui.view;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScCustomerInfoListAdapter;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 客户选择列表Fragment
 * </pre>
 * extends SherlockFragment
 * @author liuyu
 * @version $Id: ScCustomerInfoListFragment.java, v 0.1 2013-7-3 下午4:27:15 liuyu Exp $
 */
public class ScCustomerInfoListFragment extends SherlockFragment implements OnQueryTextListener, OnScrollListener{
    
    private final static String tag = ScCustomerInfoListFragment.class.getName();
    private int DEFAULT_PAGE_SIZE = 20; 
    private int start = 0;
    private int currentPage = 0;
    private String searchWord ;
    private List<Customer> customerList;
    private ListView custList;
    private ScCustomerInfoListAdapter adapter;
    private SearchView searchView;
    private boolean isQuerySearch = false;// true 模糊搜索异步执行完毕
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(tag, "-----onCreatView-----");
        View view = inflater.inflate(R.layout.sc_customer_selecte, container, false);
        custList = (ListView)view.findViewById(R.id.customer_list);
        custList.setOnScrollListener(this);
        searchCustomer();
        if(adapter == null){
            adapter = new ScCustomerInfoListAdapter(getActivity());
        }
        custList.setAdapter(adapter);
        custList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i(tag, "----------"+arg2+"----------");
                if(getString(R.string.dormancy).equals(customerList.get(arg2).getCustStatus())){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.dormancy_customer), Toast.LENGTH_LONG).show();
                }else{
                    
                    if(getActivity().getString(R.string.buycar).equals(customerList.get(arg2).getCustStatus())){
                        customerList.get(arg2).setRebuyStoreCustTag(true);
                    }
                    Intent intent = new Intent(getActivity(), ScOppoInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("customer", customerList.get(arg2));
                    intent.putExtra("isOldCust", true);
                    intent.putExtra("oldCust", true);
                    intent.putExtras(bundle);
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }
            
        });
        return view;
    }
    
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(tag, "-----onActivityCreated-----");
        this.setHasOptionsMenu(true);
    }


    

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        searchView = new SearchView(getSherlockActivity().getSupportActionBar().getThemedContext());
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchWord = null;
                currentPage = 0;
                start = 0;
                if(customerList != null)
                customerList.clear();
                searchCustomer();
                return false;
            }});
        MenuItem item = menu.findItem(R.id.search);
        item.setActionView(searchView);
        item.setVisible(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    /**
     * 
     * <pre>
     * 获取老客户方法
     * </pre>
     *
     */

    public void searchCustomer(){
        isQuerySearch = false;
        new BaseTask<List<Customer>>(getActivity()) {

            @Override
            protected List<Customer> doInBackground(String... arg0) {
                RoilandCRMApplication application = (RoilandCRMApplication)getActivity().getApplication();
                CRMManager manager = application.getCRMManager();
                List<Customer> customerList = null;
                try {
                    currentPage++;
                    customerList = manager.getCustomerList(searchWord, null, start, DEFAULT_PAGE_SIZE, null, null, "3", null);
                } catch (ResponseException e) {
                    responseException = e;
                    Log.e(tag, e.getMessage());
                }
                return customerList;
            }
            
            @Override
            protected void onPostExecute(List<Customer> result) {
                super.onPostExecute(result);
                
                if(responseException != null){
                    Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_LONG).show();
                    return ;
                }
                if ((currentPage-1) == 0 && customerList == null) {
                    customerList = result;
                } else {
                    customerList.addAll(result);
                }
                displayCustomer();
                isQuerySearch = true;
            }
            
            @Override
            protected void onCancelled() {
                isQuerySearch = true;
                super.onCancelled();
            }
        }.execute();
    }
    
    
    public void displayCustomer(){
        Log.i(tag, "-----display------");
        adapter.setCustomerList(customerList);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
    }


    /**
     * 实现上拉加载
     * @param view
     * @param scrollState
     * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        
        switch (scrollState) {

            case OnScrollListener.SCROLL_STATE_IDLE:
                int lastposition = custList.getLastVisiblePosition();
                int count = currentPage * DEFAULT_PAGE_SIZE;
                if (lastposition == count-1) {
                    Log.i(tag, "onScroll: " + "begin load ");
                    if(start==0){
                        start = start + DEFAULT_PAGE_SIZE;
                    }else{
                        start = start + DEFAULT_PAGE_SIZE;
                    }
                    searchCustomer();
                }
                break;

            }
    }


    /**
     * 搜索功能
     * @param query 搜索的关键字
     * @return
     * @see com.actionbarsherlock.widget.SearchView.OnQueryTextListener#onQueryTextSubmit(java.lang.String)
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(tag, "----------query="+query+"-----------");
        if(!isQuerySearch){
            return false;
        }
        searchWord = query;
        if(customerList != null)
        customerList.clear();
        searchCustomer();
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    

}
