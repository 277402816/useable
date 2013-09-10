package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 客户管理高级搜索
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: smCustomerSearchFragment.java, v 0.1 2013-5-29 下午5:27:29 shuang.gao Exp $
 */
public class SmCustomerSearchFragment extends SherlockFragment {

    public List<Dictionary>             downloadedResult;
    protected BaseActivity              activity;
    private SmCustomerSearchListAdapter adapter;
    private int                         selectedIndex;   //选定的搜索项的索引
    private String                      custOwer = null;        //客户归属
    private String                      ownerId = null;          //销售顾问ID
    private String                      custStatus = null;      //客户状态

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
        View view = inflater.inflate(R.layout.sm_oppo_search_info, container, false);
        final String[] valueList1 = { getString(R.string.cust_rebuyStore), getString(R.string.cust_recommend) };
        final String[] valueList2 = { getString(R.string.cust_dormancy),
                getString(R.string.cust_purchasecar), getString(R.string.cust_latency), };
        if (adapter == null) {
            adapter = new SmCustomerSearchListAdapter(getActivity());
        }
        adapter.clearData();
        ListView listView = (ListView) view.findViewById(R.id.oppo_info_list);
        ImageButton searchStart = (ImageButton) view.findViewById(R.id.oppo_search_start_button);
        //设置开始查询事件监听
        searchStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (adapter.getCustOwer() != null) {
                    if (adapter.getCustOwer().equals(getString(R.string.cust_rebuyStore))) {
                        //本公司下发
                        custOwer = "0";
                    }
                    if (adapter.getCustOwer().equals(getString(R.string.cust_recommend))) {
                        //推荐客户
                         custOwer = "2";
                    }
                }
                if (!StringUtils.isEmpty(adapter.getCustStatus())) {
                    if (adapter.getCustStatus().equals(getString(R.string.cust_dormancy))) {
                        //休眠客户
                        custStatus = "0";
                    }
                    if (adapter.getCustStatus().equals(getString(R.string.cust_purchasecar))) {
                        //购车客户
                        custStatus = "1";
                    }
                    if (adapter.getCustStatus().equals(getString(R.string.cust_latency))) {
                        //潜在客户
                        custStatus = "2";
                    }
                }
                ownerId = adapter.getOwerID().getDicKey();
                //搜索操作
                doSearch(custOwer,custStatus,ownerId);
            }
        });
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        getOrderType(arg2, valueList1);
                        break;
                    case 1:
                        if(getString(R.string.cust_rebuyStore).equals(adapter.getCustOwer())){
                            downloadOwnerID(arg2);
                        }
                        break;
                    case 2:
                        if(getString(R.string.cust_rebuyStore).equals(adapter.getCustOwer())){
                        getOrderType(arg2, valueList2);
                        }
                        break;
                }
            }
        });

        return view;
    }

    /**
     * 创建菜单
     * @param menu 菜单项
     * @param inflater
     * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem itemCancle = menu.findItem(R.id.cancel);
        itemCancle.setVisible(true);
    }

    /**
     * 菜单选择项
     * @param item 菜单项
     * @return
     * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cancel:
                activity.onItemSelected(4, -1,null);
                break;
            case android.R.id.home:
                activity.toggle();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * 
     * <pre>
     * 从数据字典获取销售顾问ID
     * </pre>
     *
     * @param position 所在条目位置
     */
    public void downloadOwnerID(int position) {

        new BaseTask<List<Dictionary>>(activity) {
            List<Dictionary> downloadEmployee;

            @Override
            protected List<Dictionary> doInBackground(String... params) {
                selectedIndex = Integer.parseInt(params[0]);
                CRMManager manager = ((RoilandCRMApplication) getActivity().getApplication())
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
                if (result != null) {
                    downloadEmployee = result;
                    String[] valueList = new String[result.size()];
                    for (int i = 0; i < result.size(); i++) {
                        valueList[i] = result.get(i).getDicValue();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(adapter.getCondition(selectedIndex));
                    builder.setItems(valueList, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dictionary employee = downloadEmployee.get(which);
                            adapter.setOwerID(employee);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.create().show();
                }
            }

        }.execute(String.valueOf(position));
    }

    /**
     *  
     * <pre>
     * 高级查询适配器
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: smCustomerSearchFragment.java, v 0.1 2013-5-30 上午9:54:33 shuang.gao Exp $
     */
    public class SmCustomerSearchListAdapter extends BaseAdapter {
        String     custOwer   = null;                                   //客户归属
        String     custStatus = null;                                   //客户状态
        Dictionary owerID     = null;                                   //销售顾问ID
        List<View> viewList;
        Context    context;
        String[]   condition  = { getString(R.string.cust_owner), getString(R.string.ownerId),
                                      getString(R.string.cust_status) };

        public SmCustomerSearchListAdapter(Context context) {
            this.context = context;
            try {
                viewList = new ArrayList<View>();
                for (int i = 0; i < condition.length; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.sm_oppo_search_item,
                        null);
                    viewList.add(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public CharSequence getCondition(int selectedIndex) {
            return condition[selectedIndex];
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * 向列表添加数据
         * @param position 位置
         * @param convertView 视图
         * @param parent
         * @return
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                case 0:
                    fillView(viewList.get(position), condition[0], custOwer);
                    break;
                case 1:
                    if(!getResources().getString(R.string.cust_rebuyStore).equals(custOwer)){
                        fillView(viewList.get(1), condition[1], "");
                        this.setOwerID(new Dictionary());
                    }else
                    fillView(viewList.get(position), condition[1], owerID);
                    break;
                case 2:
                    if(!getResources().getString(R.string.cust_rebuyStore).equals(custOwer)){
                        fillView(viewList.get(2), condition[2], "");
                        this.setCustStatus("");
                    }else
                    fillView(viewList.get(position), condition[2], custStatus);
                    break;
            }
            return viewList.get(position);
        }

        private void fillView(View view, String condition, String content) {
            ((TextView) view.findViewById(R.id.txt_title)).setText(condition);
            if (content != null) {
                ((TextView) view.findViewById(R.id.txt_content)).setText(content);
            } else {
                ((TextView) view.findViewById(R.id.txt_content)).setText("");
            }
        }

        public void fillView(View view, String condition, Dictionary dic) {
            ((TextView) view.findViewById(R.id.txt_title)).setText(condition);
            if (dic != null) {
                ((TextView) view.findViewById(R.id.txt_content)).setText(dic.getDicValue());
            } else {
                ((TextView) view.findViewById(R.id.txt_content)).setText("");
            }
        }

        /**
         * 
         * <pre>
         * 清空数据
         * </pre>
         *
         */
        public void clearData() {

            this.setCustOwer("");
            this.setCustStatus("");
            this.setOwerID(new Dictionary());
            this.notifyDataSetChanged();
        }

        public String getCustOwer() {
            return custOwer;
        }

        public void setCustOwer(String custOwer) {
            this.custOwer = custOwer;
        }

        public String getCustStatus() {
            return custStatus;
        }

        public void setCustStatus(String custStatus) {
            this.custStatus = custStatus;
        }

        public Dictionary getOwerID() {
            return owerID;
        }

        public void setOwerID(Dictionary owerID) {
            this.owerID = owerID;
        }

    }

    /**
     * 
     * <pre>
     * 点击排序方式
     * </pre>
     * @param valueList 列表显示数据
     *
     */
    public void getOrderType(final int arg2, final String[] valueList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(adapter.getCondition(arg2));

        builder.setItems(valueList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (arg2) {
                    case 0:
                        adapter.setCustOwer(valueList[item]);
                        break;
                    case 2:
                        adapter.setCustStatus(valueList[item]);
                        break;
                    default:
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    /**
     * 
     * <pre>
     * 开始查询任务
     * </pre>
     *
     */

    private void doSearch(String custOwer, String custStatus, String ownerId) {
        Bundle bundle = new Bundle();
        bundle.putString("custOwer", custOwer);
        bundle.putString("custStatus", custStatus);
        bundle.putString("ownerId", ownerId);
        clearData();
        activity.onItemSelected(4, -1, bundle,null);
    }

    private void clearData() {
        custOwer = null;
        custStatus = null;
        ownerId = null;
    }
}
