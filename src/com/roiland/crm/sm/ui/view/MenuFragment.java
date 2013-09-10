package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.EventReceiver;
import com.roiland.crm.sm.utils.PromissionController;

/**
 * 
 * <pre>
 * 菜单页面，显示左边隐藏菜单。
 * </pre>
 *
 * @author cjyy
 * @version $Id: MenuFragment.java, v 0.1 2013-5-13 下午05:35:25 cjyy Exp $
 */
public class MenuFragment extends ListFragment {
	private final static String tag = MenuFragment.class.getName();
	
	private ItemSelectedListener listener;
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (ItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw e;
		}
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//处理当滑动的时候不让其显示黑色背景
		getListView().setCacheColorHint(0); 
		 //隐藏分割线
		getListView().setDividerHeight(0);
		getListView().setSelector(R.color.transparent);
		MenusAdapter adapter = new MenusAdapter(getActivity());
		Log.d(tag, "=menuRoleName======" + PromissionController.menuRoleName);
		//判读左边菜单是否可显示
		if(PromissionController.menuRoleName.equals(getString(R.string.custSm)) || PromissionController.menuRoleName.equals((getString(R.string.SaleTreasurer)))){

	        if (PromissionController.isAvaliable(getString(R.string.menu_main))) {
	            adapter.add(new MenuItem(getString(R.string.menu_main), R.drawable.main_page_normal, R.drawable.main_page_focus));
	        }
            if (PromissionController.isAvaliable(getString(R.string.sm_menu_sales_oppo))) {
                adapter.add(new MenuItem(getString(R.string.sm_menu_sales_oppo), R.drawable.sales_oppo_normal, R.drawable.sales_oppo_focus));
            }	        
	        if (PromissionController.isAvaliable(getString(R.string.sm_menu_trace_plan))) {
	          adapter.add(new MenuItem(getString(R.string.sm_menu_trace_plan), R.drawable.trace_plan_normal, R.drawable.trace_plan_focus));
	        }

            if (PromissionController.isAvaliable(getString(R.string.sm_menu_cust_order))) {
              adapter.add(new MenuItem(getString(R.string.sm_menu_cust_order), R.drawable.customer_order_normal, R.drawable.customer_order_focus));
            }
	        if (PromissionController.isAvaliable(getString(R.string.sm_menu_cust_manage))) {
	          adapter.add(new MenuItem(getString(R.string.sm_menu_cust_manage), R.drawable.cust_info_normal, R.drawable.cust_info_focus));
	        }

            if (PromissionController.isAvaliable(getString(R.string.sm_menu_vehicle_search))) {
              adapter.add(new MenuItem(getString(R.string.sm_menu_vehicle_search), R.drawable.vehicle_search_normal, R.drawable.vehicle_search_focus));
            }
	        if (PromissionController.isAvaliable(getString(R.string.sm_menu_sales_director_funnel))) {
	            adapter.add(new MenuItem(getString(R.string.sm_menu_sales_director_funnel), R.drawable.director_funnel_normal, R.drawable.director_funnel_focus));
	        }

		}else if (PromissionController.menuRoleName.equals(getString(R.string.custSc))){
		      if (PromissionController.isAvaliable(getString(R.string.menu_main))) {
	                adapter.add(new MenuItem(getString(R.string.menu_main), R.drawable.main_page_normal, R.drawable.main_page_focus));
	            }
              if (PromissionController.isAvaliable(getString(R.string.sc_menu_sales_oppo))) {
                  adapter.add(new MenuItem(getString(R.string.sc_menu_sales_oppo), R.drawable.sc_sales_oppo_normal, R.drawable.sc_sales_oppo_focus));
                }

              if (PromissionController.isAvaliable(getString(R.string.sc_menu_cust_order))) {
                adapter.add(new MenuItem(getString(R.string.sc_menu_cust_order), R.drawable.sc_customer_order_normal, R.drawable.sc_customer_order_focus));
              }
	            if (PromissionController.isAvaliable(getString(R.string.sc_menu_trace_plan))) {
	              adapter.add(new MenuItem(getString(R.string.sc_menu_trace_plan), R.drawable.sc_trace_plan_normal, R.drawable.sc_trace_plan_focus));
	            }

                if (PromissionController.isAvaliable(getString(R.string.sc_menu_vehicle_search))) {
                  adapter.add(new MenuItem(getString(R.string.sc_menu_vehicle_search), R.drawable.vehicle_search_normal, R.drawable.vehicle_search_focus));
                }
	            if (PromissionController.isAvaliable(getString(R.string.sc_menu_cust_manage))) {
	              adapter.add(new MenuItem(getString(R.string.sc_menu_cust_manage), R.drawable.cust_info_normal, R.drawable.cust_info_focus));
	            }
		}else{
		    DialogUtils.alert(getActivity(), getString(R.string.customer_list_info), getString(R.string.opt_success), null).show();
		}
        
		setListAdapter(adapter);
		changeSelectMenu(0);
	}
	
	/**
	 * 菜单项点击事件。
	 * 
	 * @param l            菜单列表
	 * @param v            菜单项
	 * @param position     选中项位置
	 * @param id           选中项ID
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	    //判断网络状况
        if (EventReceiver.isNetworkUnavailable()) {
            Toast.makeText(getActivity(), getString(R.string.network_error),
                Toast.LENGTH_SHORT).show();
            return;
        }
		changeSelectMenu(position);
	}

	public void onAdvancedSelected(int position, int subPosition,String flag) {
	    //判断网络状况
        if (EventReceiver.isNetworkUnavailable()) {
            Toast.makeText(getActivity(), getString(R.string.network_error),
                Toast.LENGTH_SHORT).show();
            return;
        }
	    if(PromissionController.menuRoleName.equals(getString(R.string.custSm)) || 
	            PromissionController.menuRoleName.equals((getString(R.string.SaleTreasurer)))){
	        for (int i = 0; i < 7; i++) {
	            View view = getListAdapter().getView(i, null, null);
	            ImageView iv = (ImageView)view.findViewById(R.id.row_icon);
	            MenuItem item = (MenuItem)getListAdapter().getItem(i);
	            if (i == position)
	                iv.setBackgroundResource(item.iconResSelected);
	            else
	                iv.setBackgroundResource(item.iconRes);
	        }
	    }else if(PromissionController.menuRoleName.equals(getString(R.string.custSc))){
	        for (int i = 0; i < 6; i++) {
                View view = getListAdapter().getView(i, null, null);
                ImageView iv = (ImageView)view.findViewById(R.id.row_icon);
                MenuItem item = (MenuItem)getListAdapter().getItem(i);
                if (i == position)
                    iv.setBackgroundResource(item.iconResSelected);
                else
                    iv.setBackgroundResource(item.iconRes);
            }
	    }
		
		
		listener.onItemSelected(position, subPosition,flag);
	}
	
	public void changeSelectMenu(int position) {
	    int size = 0;
	    if(PromissionController.menuRoleName.equals(getString(R.string.custSm)) || PromissionController.menuRoleName.equals((getString(R.string.SaleTreasurer)))){
	        size = 7;
	    }else if(PromissionController.menuRoleName.equals(getString(R.string.custSc))){
	        size = 6;
	    }
		for (int i = 0; i < size; i++) {
			View view = getListAdapter().getView(i, null, null);
			ImageView iv = (ImageView)view.findViewById(R.id.row_icon);
			MenuItem item = (MenuItem)getListAdapter().getItem(i);
			if (i == position)
				iv.setBackgroundResource(item.iconResSelected);
			else
				iv.setBackgroundResource(item.iconRes);
		}
		listener.onItemSelected(position, -1,null);
	}
	
	/**
	 * 
	 * <pre>
	 * 菜单项信息
	 * </pre>
	 *
	 * @author cjyy
	 * @version $Id: MenuFragment.java, v 0.1 2013-5-14 下午04:23:10 cjyy Exp $
	 */
	private class MenuItem {
		public String tag;
		public int iconRes;
		public int iconResSelected;
		public MenuItem(String tag, int iconRes, int iconResSelected) {
			this.tag = tag; 
			this.iconRes = iconRes;
			this.iconResSelected = iconResSelected;
		}
	}

	/**
	 * 
	 * <pre>
	 * 菜单被选中监听
	 * </pre>
	 *
	 * @author cjyy
	 * @version $Id: MenuFragment.java, v 0.1 2013-5-14 下午04:23:49 cjyy Exp $
	 */
	public interface ItemSelectedListener {
	    public void onItemSelected(int position, int otherFlags,String flag);
        public void onItemSelected(int position, int otherFlags, Bundle bundle,String flag);
	}
	
	/**
	 * 
	 * <pre>
	 * 菜单列表适配
	 * </pre>
	 *
	 * @author cjyy
	 * @version $Id: MenuFragment.java, v 0.1 2013-5-14 下午04:24:46 cjyy Exp $
	 */
	public class MenusAdapter extends ArrayAdapter<MenuItem> {
		List<View> viewList;
		
		public MenusAdapter(Context context) {
			super(context, 0);
			viewList = new ArrayList<View>();
		}
		
		@Override
		public void add(MenuItem item) {
			super.add(item);
			View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, null);
			ImageView icon = (ImageView) view.findViewById(R.id.row_icon);
			icon.setBackgroundResource(getItem(getCount() - 1).iconRes);
			viewList.add(view);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, null);
			}			
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setBackgroundResource(getItem(position).iconRes);
			
			return viewList.get(position);
		}

	}
}
