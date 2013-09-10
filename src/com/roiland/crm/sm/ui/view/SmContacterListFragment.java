package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.SmContacterListAdapter;
import com.roiland.crm.sm.utils.DialogUtils;

/**
 * 
 * <pre>
 * 联系人列表Fragment
 * </pre>
 * @extends BaseListFragment
 * @author liuyu
 * @version $Id: SmContacterListFragment.java, v 0.1 2013-5-22 上午10:21:25 liuyu Exp $
 */
public class SmContacterListFragment extends BaseListFragment {
	List<Contacter> contacterList;
	SmContacterListAdapter listAdapter;
	String mCustomerID;
	String mProjectID;
	
	public SmContacterListFragment()
	{
		super();
	}
	public SmContacterListFragment(String prjID, String custID) {
		super();
		mCustomerID = custID;
		mProjectID = prjID;
	}

	/**
	 * 
	 * @param o0    o0
	 */
	/**
	 * 当Fragment被创建时候被调用
	 * @param savedInstanceState
	 * @see com.roiland.crm.sm.ui.view.BaseListFragment#onActivityCreated(android.os.Bundle)
	 */
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        this.getListView().setBackgroundResource(R.drawable.list_item_background);
        listAdapter = new SmContacterListAdapter(this.getActivity());
		setListAdapter(listAdapter);
		
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
		new BaseTask<List<Contacter>>(getActivity(), "", true) {

			@Override
			protected List<Contacter> doInBackground(String... params) {
				CRMManager manager = mApplication.getCRMManager();
				
				try {
					contacterList = manager.getContacterList(mProjectID, mCustomerID);
				} catch (ResponseException e) {
					responseException = e;
					contacterList = new ArrayList<Contacter>();
				} catch (Exception e) {
					e.printStackTrace();
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
		                   if (responseException.getStatusCode() == StatusCodeConstant.UNAUTHORIZED) {
		                        DialogUtils.overMinuteDo(getActivity(),responseException);
		                    } else {
						Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
					}
				} else {
					displayList();
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
		Intent intent = new Intent(getActivity(), SmContacterInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("CONTACTER", contacterList.get(position));
		intent.putExtra("editable", false);
		intent.putExtra("prjID", mProjectID);
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
