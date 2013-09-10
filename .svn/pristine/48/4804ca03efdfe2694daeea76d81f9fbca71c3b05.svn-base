package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Attach;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.ScAttachListAdapter;

/**
 * 
 * <pre>
 * 显示文档图片列表的fragment
 * </pre>
 * @extends BaseListFragment
 * @author liuyu
 * @version $Id: ScAttachListFragment.java, v 0.1 2013-7-5 上午8:59:33 liuyu Exp $
 */

@SuppressLint("ValidFragment")
public class ScAttachListFragment extends BaseListFragment {
	List<Attach> attachList; //文档图片列表
	ScAttachListAdapter listAdapter; //adapter of listview
	String projectID; //销售线索id，供查询使用
	String customerID; //客户id，供查询使用
	
	/**
	 * 构造方法
	 * @param projectID  销售线索ID
	 * @param customerID 客户ID
	 */
	public ScAttachListFragment(String projectID, String customerID) {
		super();
		this.projectID = projectID;
		this.customerID = customerID;
	}

	/**
	 * 在Activity被创建时调用此方法
	 * @param savedInstanceState
	 * @see com.roiland.crm.sm.ui.view.BaseListFragment#onActivityCreated(android.os.Bundle)
	 */
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		this.getListView().setBackgroundResource(R.drawable.list_item_background);
		listAdapter = new ScAttachListAdapter(this.getActivity());
		setListAdapter(listAdapter);
		
		/**
		 * 通过api获取文档图片信息
		 */
		new BaseTask<List<Attach>>(getActivity(), "", true) {

			@Override
			protected List<Attach> doInBackground(String... params) {
				CRMManager manager = mApplication.getCRMManager();
				
				try {
					attachList = manager.getAttachmentList(projectID, customerID);
				} catch (ResponseException e) {
					responseException = e;
					attachList = new ArrayList<Attach>();
				} catch (Exception e) {
					e.printStackTrace();
					attachList = new ArrayList<Attach>();
				}
				if (attachList == null) {
					attachList = new ArrayList<Attach>();
				}
				return attachList;
			}
			
			@Override
			protected void onPostExecute(List<Attach> result) {
				super.onPostExecute(result);
				if (responseException != null) {
					if (getActivity() != null) {
						Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} else {
					displayList();
				}
			}
		}.execute();
	}
	/**
	 * 
	 * <pre>
	 * 显示文档图片列表
	 * </pre>
	 * 
	 */
	public void displayList() {
		listAdapter.notifyDataChanged(attachList);
	}
	
	/**
	 * 单击列表中的item时候调用此方法
	 * @param l 点击页面中的listview
	 * @param v 点击listView中的item
	 * @param position item在列表中的位置
	 * @param id 点击listview中的item id
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), ScAttachInfoActivity.class);
		if (position >=0 && position < attachList.size()) {
			Attach attach = attachList.get(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable("ATTACH", attach);
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	/**
	 * 
	 * <pre>
	 * 这个方法被第三方程序调用显示图片
	 * </pre>
	 *
	 * @param url 路径
	 */
	public void showCustomImage(Uri url) {

        Intent insertimageIntent = new Intent();
        insertimageIntent.setData(url);
        insertimageIntent.setAction(Intent.ACTION_GET_CONTENT);

		activity.startActivityForResult(Intent.createChooser(insertimageIntent, getResources().getString(R.string.attach_info_title)), 1);
	}
}
