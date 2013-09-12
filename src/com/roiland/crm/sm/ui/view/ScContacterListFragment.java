package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScContacterListAdapter;

/**
 * 
 * <pre>
 * 联系人列表Fragment
 * </pre>
 * @extends BaseListFragment
 * @author liuyu
 * @version $Id: ScContacterListFragment.java, v 0.1 2013-7-2 下午1:34:18 liuyu Exp $
 */
public class ScContacterListFragment extends BaseListFragment {
	List<Contacter> contacterList;
	ScContacterListAdapter listAdapter;
	String mCustomerID;
	String mProjectID;
	String comeFrom;
	
	public ScContacterListFragment()
	{
		super();
	}
	public ScContacterListFragment(String prjID, String custID,String comeFrom) {
		super();
		mCustomerID = custID;
		mProjectID = prjID;
		this.comeFrom = comeFrom;
	}
	
	
	
	@Override
    public void onCreate() {
        super.onCreate();
        
    }
    /**
	 * 当Fragment被创建时候被调用
	 * @param savedInstanceState
	 * @see com.roiland.crm.sm.ui.view.BaseListFragment#onActivityCreated(android.os.Bundle)
	 */
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        this.getListView().setBackgroundResource(R.drawable.list_item_background);
        listAdapter = new ScContacterListAdapter(this.getActivity());
		setListAdapter(listAdapter);
		this.setHasOptionsMenu(true);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		search();
	}
	/**
	 * 
	 * <pre>
	 * 异步线程，调用API获得联系人列表
	 * </pre>
	 * 查询操作和页面加载时候都调用此方法
	 */
	public void search() {
	    Log.d(ScContacterListFragment.class.getSimpleName(), "调用位置>> ", new Exception());
		new BaseTask<List<Contacter>>(getActivity(), "", true) {

			@Override
			protected List<Contacter> doInBackground(String... params) {
				CRMManager manager = mApplication.getCRMManager();
				
				try {
				    if("fromCustomer".equals(comeFrom)){
				        contacterList = manager.getContacterList(null, mCustomerID);
				    }else if("fromProject".equals(comeFrom)){
				        contacterList = manager.getContacterList(mProjectID, null);
				    }
				} catch (ResponseException e) {
					responseException = e;
					contacterList = new ArrayList<Contacter>();
				} 
				if (contacterList == null) {
					contacterList = new ArrayList<Contacter>();
				}
				return contacterList;
			}
			
			@Override
			protected void onPostExecute(List<Contacter> result) {
				super.onPostExecute(result);
				if (responseException != null) {
					if (getActivity() != null) {
						Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} else if (result != null) {
				    if (getActivity() != null) {
				        displayList();
				    }
				}
			}
		}.execute();
	}
	
	public void displayList() {
		listAdapter.notifyDataChanged(contacterList);
	}
	
	/**
	 * 联系人列表单击事件，将数据以Intent形式传送给Activity
	 * @param l 页面的ListView
	 * @param v ListView的item
	 * @param position 单击时候item的位置
	 * @param id 点击listview中的item id
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), ScContacterInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("contacter", contacterList.get(position));
		intent.putExtra("editable", false);
		intent.putExtra("prjID", mProjectID);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	/**
	 * 设置ActionBar的菜单项
	 * @param menu
	 * @param inflater
	 * @see com.roiland.crm.sm.ui.view.BaseListFragment#onCreateOptionsMenu(com.actionbarsherlock.view.Menu, com.actionbarsherlock.view.MenuInflater)
	 */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        MenuItem item = menu.findItem(R.id.add);
        item.setVisible(true);
    }
    
    /**
     * 监听菜单项单击事件
     * @param item
     * @return
     * @see com.actionbarsherlock.app.SherlockListFragment#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(getActivity(),ScContacterInfoActivity.class);
                intent.putExtra("addFlag", true);
                intent.putExtra("prjID", mProjectID);
                intent.putExtra("customerID", mCustomerID);
                intent.putExtra("comeFrom", comeFrom);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
	
	

}
