package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.roiland.crm.sc.R;
/**
 * 
 * <pre>
 * 列表基础类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SeparatedListAdapter.java, v 0.1 2013-8-2 下午4:54:00 shuang.gao Exp $
 */
public class SeparatedListAdapter<T> extends BaseAdapter {
	
	public static final int TYPE_SECTION_HEADER = 0;
	public static final int TYPE_SECTION_CONTENT = 2;
	public static final int TYPE_LOADINGVIEW = 1;

	private Context mContext;
	private int mContentResId;
	public boolean loadingbarViewable = false;

	private ArrayList<String> headers = new ArrayList<String>();
	private Map<String, List<T>> sections = new HashMap<String, List<T>>();

	public SeparatedListAdapter(Context context) {
		mContext = context;
	}
	
	public SeparatedListAdapter(Context context, int contentResId) {
		mContext = context;
		mContentResId = contentResId;
	}
	
	public synchronized void addSection(String section, List<T> data) {
		if (sections.get(section) == null) {
			headers.add(section);
			sections.put(section, data);
		}
	}
	
	public synchronized void removeAll() {
		this.headers.clear();
		this.sections.clear();
	}

	public int getHeaderCount() {
		return headers.size();
	}
	
	public Object getItem(int position) {
		try {
			for(int i = 0; i < headers.size(); i ++) {
				String section = headers.get(i);
				List<T> data = sections.get(section);
				int size = data.size() + 1;

				if (position == 0) {
					return section;
				}

				if (position < size) {
					return data.get(position - 1);
				}

				position -= size;
			}
		} catch (Exception e) {
			Log.e("SeparatedListAdapter", "getItem error : ", e);
		}
		return null;   
	}   

	@Override
	public int getCount() {
		int total = 0;
		for (List<T> data : this.sections.values())
			total += data.size() + 1;

		if (loadingbarViewable)
			total ++;

		return total;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		if (loadingbarViewable && position == getCount() - 1) {
			return TYPE_LOADINGVIEW;
		}

		for(int i = 0; i < headers.size(); i ++) {
			String section = headers.get(i);
			List<T> data = sections.get(section);
			int size = data.size() + 1;

			if(position == 0) return TYPE_SECTION_HEADER;
			if(position < size) return TYPE_SECTION_CONTENT;

			position -= size;
		}

		return -1;
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	@Override  
	public View getView(int position, View convertView, ViewGroup parent) {      
		try {
			if (loadingbarViewable && position == getCount() - 1) {
				return getLoadingView(position, convertView, parent);
			}

			for (int i = 0; i < headers.size(); i ++) {
				String key = headers.get(i);
				List<T> data = sections.get(key);
				int size = data.size() + 1;
				if (position == 0) {
					if (i < headers.size()) {
						return getHeaderView(i, convertView, parent, key);
					} else {
						return new View(mContext);
					}
				}
				if (position < size) {
					return getContentView(i, position-1, convertView, parent, data.get(position-1));
				}
				position -= size;
			}
		} catch (Exception e) {
			Log.e("SeparatedListAdapter", "getView error : ", e);
		}
		return new View(mContext);
	}   

	@Override  
	public long getItemId(int position) {
		return position;
	}

	protected View getHeaderView(int position, View convertView, ViewGroup parent, String header) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_header, parent, false);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.list_header_title);
		textView.setText(header);
		return convertView;
	}

	protected View getContentView(int headerPosition, int position, View convertView, ViewGroup parent, T item) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mContentResId, parent, false);
		}
		return convertView;
	}

	protected View getLoadingView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.loadingbar, null);
			convertView.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
			((TextView)(convertView.findViewById(R.id.loadingtext))).setText(R.string.wait_for_add);
		}
		return convertView;
	}
}
