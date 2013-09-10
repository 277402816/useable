package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.Owner;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 分配销售顾问列表
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmCustomerOwenrListFragment.java, v 0.1 2013-6-1 下午12:50:10 shuang.gao Exp $
 */
public class SmCustomerOwenrListFragment extends SherlockFragment {
    protected BaseActivity activity;
    CustomerOwenrAdapter   customerOwenrAdapter;
    List<String>           mCustomerOwnerList;
    List<Owner>            listOwner;            //销售顾问列表
    Owner                  owner;               //销售顾问
    Customer               mCustomer;           //客户信息
    String                 ownerID;             //销售顾问ID（雇员ID）
    ListView               mListView;           //显示列表控件

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof BaseActivity) {
                this.activity = (BaseActivity) activity;
            }
        } catch (Exception e) {
            this.activity = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sm_customer_owner_select, container, false);
        mListView = (ListView) view.findViewById(R.id.customer_owner_list);
        Intent intent = getActivity().getIntent();
        //获取customerID 客户ID
        mCustomer = intent.getParcelableExtra("customer");
        downloadOwner();
        //列表点击事件
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ownerID = listOwner.get(arg2).getStrId();
                customerOwenrAdapter.setSelectItem(arg2);  
                customerOwenrAdapter.notifyDataSetInvalidated(); 

            }
        });
        return view;
    }

    /**
     * 
     * <pre>
     * 从数据字典获取销售顾问ID
     * </pre>
     *
     * @param position 所在条目位置
     */
    public void downloadOwner() {
        new BaseTask<List<Dictionary>>(getActivity()) {
            @Override
            protected List<Dictionary> doInBackground(String... params) {
                CRMManager manager = ((RoilandCRMApplication) this.activity.getApplication())
                    .getCRMManager();
                try {
                    return manager.getEmployeeList();
                } catch (ResponseException e) {
                    Log.i("-----------downloadEmployee----------", e.getMessage());
                    responseException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Dictionary> result) {
                super.onPostExecute(result);
                if (responseException != null) {
                    Toast.makeText(activity, responseException.getMessage(), Toast.LENGTH_LONG)
                        .show();
                }
                listOwner = new ArrayList<Owner>();
                for (int i = 0; i < result.size(); i++) {
                    owner = new Owner();
                    owner.setStrName(result.get(i).getDicValue());
                    owner.setStrId(result.get(i).getDicKey());
                    listOwner.add(owner);
                }
                customerOwenrAdapter = new CustomerOwenrAdapter(getActivity(), listOwner);
                mListView.setAdapter(customerOwenrAdapter);
            }

        }.execute();
    }

    /**
     * 
     * <pre>
     * 分配销售顾问适配
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: SmCustomerOwenrListActivity.java, v 0.1 2013-6-1 下午1:07:23 shuang.gao Exp $
     */
    private class CustomerOwenrAdapter extends BaseAdapter {

        List<Owner> customerOwnerList;
        Context     context;
        private int  selectItem=-1;  
        
        public CustomerOwenrAdapter(Context context, List<Owner> listOwner) {
            this.context = context;
            this.customerOwnerList = listOwner;
        }

        public void setSelectItem(int selectItem) {  
            this.selectItem = selectItem;  

        }

        @Override
        public int getCount() {
            return (customerOwnerList == null) ? 0 : customerOwnerList.size();
        }

        @Override
        public Object getItem(int position) {
            return customerOwnerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.sm_customer_owner_item,
                    null);
            }
            fillView(convertView, customerOwnerList.get(position).getStrName());
            if (position == selectItem) {
                convertView.setBackgroundColor(R.drawable.list_item_background);
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }

            return convertView;
        }

        private void fillView(View view, String value) {
            TextView textView = (TextView) view.findViewById(R.id.customer_owner_name);
            //            CheckBox checkBox = (CheckBox) view.findViewById(R.id.customer_owner_check);
            textView.setText(value);
        }

    }

    /**
     * 
     * <pre>
     * 保存分配
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: SmCustomerOwenrListFragment.java, v 0.1 2013-6-13 上午10:24:42 shuang.gao Exp $
     */
    public class saveOwnerTask extends BaseTask<Boolean> {

        private String tag = Log.getTag(saveOwnerTask.class);

        public saveOwnerTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            CRMManager manager = ((RoilandCRMApplication) this.activity.getApplication())
                .getCRMManager();
            try {
                String customerID = params[0];
                String ownerID = params[1];
                return manager.assignCustomer(customerID, ownerID);
            } catch (ResponseException e) {
                Log.i("-----------saveOwnerTask----------", e.getMessage());
                responseException = e;
            }
            return false;
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
                    Toast.makeText(getActivity(), R.string.opt_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("doSave", true);
                    getActivity().setResult(4, intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), R.string.opt_fail, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemCancle = menu.findItem(R.id.save);
        itemCancle.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if(StringUtils.isEmpty(ownerID)){
                    Toast.makeText(getActivity(), R.string.chooseCustSc, Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    new saveOwnerTask(getActivity()).execute(mCustomer.getCustomerID(), ownerID);
                    break;
                }
        }

        return super.onOptionsItemSelected(item);
    }

}
