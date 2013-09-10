package com.roiland.crm.sm.ui.widget;


import com.roiland.crm.sc.R;
import com.roiland.crm.sm.ui.adapter.BaseCacheListAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * <pre>
 * 自定义组件，首页--客户信息页面
 * </pre>
 *
 * @author cjyy
 * @version $Id: WelcomeHeaderWidgetView.java, v 0.1 2013-5-17 下午02:11:33 cjyy Exp $
 */
public class WelcomeHeaderWidgetView extends LinearLayout {
	private static final String tag = WelcomeHeaderWidgetView.class.getName();
	
	private ListView headerListView;
	private WelcomeHeaderAdapter adapter;
	public ImageView peopleImage;
	
	public WelcomeHeaderWidgetView(Context context) {
		super(context);
		initView(context);
	}

	public WelcomeHeaderWidgetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		
		
	}
	
	private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.welcome_header, this);
        
        peopleImage = (ImageView) findViewById(R.id.people_image);
        headerListView = (ListView) findViewById(R.id.list_header);
        headerListView.setDividerHeight(0);
        headerListView.setEnabled(false);
        adapter = new WelcomeHeaderAdapter(context);
        headerListView.setAdapter(adapter);
	}
	
	public WelcomeHeaderAdapter getWelcomeHeaderAdapter() {
		return adapter;
	}
	
	public class WelcomeHeaderAdapter extends BaseCacheListAdapter<String[]> {
		public WelcomeHeaderAdapter(Context context) {
			super(context);
		}


		@Override
		protected boolean fillView(View view, String[] item) {

			try {
				TextView contentText = (TextView)view.findViewById(R.id.text_view_label);
				TextView dataText = (TextView)view.findViewById(R.id.text_view_value);
				contentText.setText(item[0]);
				dataText.setText(item[1]);
			} catch (Exception e) {
				Log.e(tag, "getView", e);
				return false;
			}
			return true;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, convertView, parent, R.layout.welcome_header_list_item);
			
		}
	}
}
