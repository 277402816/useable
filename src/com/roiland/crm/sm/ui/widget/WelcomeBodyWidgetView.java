package com.roiland.crm.sm.ui.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter;
import com.roiland.crm.sm.ui.view.BaseActivity;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.PromissionController;

/**
 * 
 * <pre>
 * 自定义组件, 首页待办事项页面
 * </pre>
 *
 * @author cjyy
 * @version $Id: WelcomeBodyWidgetView.java, v 0.1 2013-5-17 下午02:12:21 cjyy Exp $
 */
public class WelcomeBodyWidgetView extends LinearLayout {
	
	public TextView todoListLabel;
	public ListView todoListItems;
	public WelcomeBodyAdapter adapter;
	public WelcomeBodyWidgetView(Context context) {
		super(context);
		initView(context);
	}

	public WelcomeBodyWidgetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public  WelcomeBodyAdapter getWelcomeBodyAdapter() {
		return adapter;
	}
	/**
	 * 
	 * <pre>
	 * 初始画面
	 * </pre>
	 *
	 * @param mContext
	 */
	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.welcome_body, this);

		todoListLabel = (TextView) findViewById(R.id.todo_list_label);
		todoListItems = (ListView) findViewById(R.id.todo_list_items);
		adapter = new WelcomeBodyAdapter(mContext);
		todoListItems.setDividerHeight(30);
		todoListItems.setAdapter(adapter);
		
	}
	/**
	 * 
	 * <pre>
	 * 待办事项显示画面Adapter
	 * </pre>
	 *
	 * @author cjyy
	 * @version $Id: WelcomeBodyWidgetView.java, v 0.1 2013-8-6 上午10:19:18 cjyy Exp $
	 */
	public class WelcomeBodyAdapter extends BaseCacheListAdapter<String[]> {

		public WelcomeBodyAdapter(Context context) {
			super(context);
		}

		@Override
		protected boolean fillView(View view, String[] item) {
			try {
				TextView contentText = (TextView) view.findViewById(R.id.item_label);
				TextView dataText = (TextView) view.findViewById(R.id.item_value);
				contentText.setText(item[0]);
				dataText.setText(item[1]);
				TextView itemLabel1 = (TextView) view.findViewById(R.id.item_text_label);
				TextView itemValue1 = (TextView) view.findViewById(R.id.item_text_value);
				itemLabel1.setText(item[2]);
				itemValue1.setText(item[3]);
				TextView itemLabel2 = (TextView) view.findViewById(R.id.item_text_label1);
				TextView itemValue2 = (TextView) view.findViewById(R.id.item_text_value1);
				itemLabel2.setText(item[4]);
				itemValue2.setText(item[5]);
				LinearLayout item_content_frame2 = (LinearLayout)view.findViewById(R.id.item_content_frame2);
				String str = item[0];
				if(str.replaceAll(":", "").trim().equals("跟踪计划")){
					item_content_frame2.setVisibility(View.VISIBLE);
				}
				if(str.replaceAll(":", "").trim().equals("客户订单")||str.replaceAll(":", "").trim().equals("客户管理")){
					item_content_frame2.setVisibility(View.GONE);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent, R.layout.welcome_body_list_item);
			view.findViewById(R.id.item_title_frame).setOnClickListener(new WelcomeBodyWidgetViewListener(position, 0, (BaseActivity)getContext()));
			view.findViewById(R.id.item_content_frame1).setOnClickListener(new WelcomeBodyWidgetViewListener(position, 1, (BaseActivity)getContext()));
			view.findViewById(R.id.item_content_frame2).setOnClickListener(new WelcomeBodyWidgetViewListener(position, 2, (BaseActivity)getContext()));
			return view;
		}
		/**
		 * 
		 * <pre>
		 * 待办事项列表点击事件
		 * </pre>
		 *
		 * @author cjyy
		 * @version $Id: WelcomeBodyWidgetView.java, v 0.1 2013-8-6 上午10:20:45 cjyy Exp $
		 */
		public class WelcomeBodyWidgetViewListener implements OnClickListener{
			int position;
			int subPosition;
			int selected = -1;
			String flag;
			BaseActivity mActivity;
			AlertDialog.Builder dialog = null;
			public WelcomeBodyWidgetViewListener(int position, int subPosition, BaseActivity activity) {
				super();
				this.position = position;
				this.subPosition = subPosition;
				this.mActivity = activity;
			}
			@Override
			public void onClick(View v) {
			    //角色为销售顾问或销售总监
			    if(PromissionController.menuRoleName.equals(mActivity.getString(R.string.custSm)) || PromissionController.menuRoleName.equals((mActivity.getString(R.string.SaleTreasurer)))){
			        if (position == 0)
	                    selected = 2;
	                else if (position == 1)
	                    selected = 1;
	                else if (position == 2)
	                    selected = 3;
	                else if (position == 3)
	                    selected = 4;
			        if(selected == 2){
			            //点击跟踪计划
	                    String[] valueListPlan = {mActivity.getString(R.string.todo_expired_trace_plan),mActivity.getString(R.string.todo_today_trace_plan)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueListPlan, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        //过期跟踪计划
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                    case 1:
	                                        //今日跟踪计划
	                                        flag = "1";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                }
	                            }
	                        });
	                    }
	                    dialog.create().show();
	                    
	                }else if(selected == 1){
	                    //点击销售线索
	                    String[] valueListProj = {mActivity.getString(R.string.todo_expired_sales_oppo),mActivity.getString(R.string.todo_three_day_oppo)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueListProj, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        //已过期销售线索
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                    case 1:
	                                        //3日内计划购车线索
	                                        flag = "1";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                }
	                            }
	                        });
	                        dialog.create().show();
	                    }
	                }else if(selected == 3){
	                    //点击客户订单
	                    String[] valueListOrder = {mActivity.getString(R.string.todo_three_day_order)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueListOrder, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        //3日到期订单
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                }
	                            }
	                        });
	                        dialog.create().show();
	                    }
	                }else if(selected == 4){
	                    //点击客户管理
	                    String[] valueCustManagerList = {mActivity.getString(R.string.todo_no_traceplan_customer)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueCustManagerList, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        //没有跟踪计划客户
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                        }
	                            }
	                        });
	                        dialog.create().show();
	                    }
	                } else{
	                    mActivity.mFrag.onAdvancedSelected(selected, subPosition,null);
	                }
		        }else if(PromissionController.menuRoleName.equals(mActivity.getString(R.string.custSc))){
		            //角色为销售顾问 列表点击事项同销售经理一样 此处不再陈述
		            if (position == 0)
	                    selected = 3;
	                else if (position == 1)
	                    selected = 1;
	                else if (position == 2)
	                    selected = 2;
	                else if (position == 3)
	                    selected = 5;
		            if(selected == 3){
	                    String[] valueListPlan = {mActivity.getString(R.string.todo_expired_trace_plan),mActivity.getString(R.string.todo_today_trace_plan)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueListPlan, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                    case 1:
	                                        flag = "1";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                }
	                            }
	                        });
	                    }
	                    dialog.create().show();
	                    
	                }else if(selected == 1){
	                    String[] valueListProj = {mActivity.getString(R.string.todo_expired_sales_oppo),mActivity.getString(R.string.todo_three_day_oppo)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueListProj, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                    case 1:
	                                        flag = "1";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                }
	                            }
	                        });
	                        dialog.create().show();
	                    }
	                }else if(selected == 2){
	                    String[] valueListOrder = {mActivity.getString(R.string.todo_three_day_order)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueListOrder, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                }
	                            }
	                        });
	                        dialog.create().show();
	                    }
	                }else if(selected == 5){
	                    String[] valueCustManagerList = {mActivity.getString(R.string.todo_no_traceplan_customer)};
	                    dialog = DialogUtils.selectedDialog(mActivity);
	                    if(dialog != null){
	                        dialog.setItems(valueCustManagerList, new DialogInterface.OnClickListener() {
	                            
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                switch(which){
	                                    case 0:
	                                        flag = "0";
	                                        mActivity.mFrag.onAdvancedSelected(selected, subPosition,flag);
	                                        break;
	                                        }
	                            }
	                        });
	                        dialog.create().show();
	                    }
	                } else{
	                    mActivity.mFrag.onAdvancedSelected(selected, subPosition,null);
	                }
		        }
				
				
			}
		}
	}
}