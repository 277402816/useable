package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.utils.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class BaseCacheListAdapter<T> extends BaseAdapter {
	private static final String tag = Log.getTag(BaseCacheListAdapter.class);
	
	public static final int TYPE_CONTENT_VIEW = 0;
	public static final int TYPE_LOADING_VIEW = 1;
	public Context context;
	private List<T> caches;
	public Boolean loadingbarViewable = false;
	
	public BaseCacheListAdapter(Context context) {
		this(context, new ArrayList<T>());
	}
	
	public BaseCacheListAdapter(Context context, List<T> caches) {
		this.context = context;
		this.caches = caches;
	}

	protected abstract boolean fillView(View view, T item);
	
	
	public synchronized void addItem(T t) {
		if (caches == null) {
			caches = new ArrayList<T>();
		}
		caches.add(t);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		try {
			return caches == null ? 0 : (loadingbarViewable) ? caches.size() + 1 : caches.size();
		} catch (Exception e) {
			Log.e(tag, "getCount", e);
			return 0;
		}
	}

	@Override
	public synchronized T getItem(int position) {
		try {
			if (caches == null || position >= caches.size()) return null;
			
			return caches.get(position);
		} catch (Exception e) {
			Log.e(tag, "getItem --> " + position, e);
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public Context getContext() {
		return context;
	}
	
	public synchronized View getView(int position, View convertView, ViewGroup parent, int layout) {
		try {
			if (getItemViewType(position) == TYPE_LOADING_VIEW) {
				if (convertView == null) {
					convertView = LayoutInflater.from(context).inflate(R.layout.loadingbar, null);
					convertView.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
					((TextView)(convertView.findViewById(R.id.loadingtext))).setText(R.string.wait_for_add);
				}
			} else {
				if (convertView == null) {
					convertView = LayoutInflater.from(context).inflate(layout, null);
				}
			}
			T item = (T) getItem(position);
			if (item != null) {
				fillView(convertView, item);
			}
		} catch (Exception e) {
			Log.e(tag, "getView --> " + position, e);
		}
		return convertView;
	}

	public synchronized List<T> getCaches() {
		return caches;
	}

	public synchronized void setCaches(List<T> caches) {
	//	clearData();
		this.caches = caches;
		Log.e(tag, "setCaches --> ");
		this.notifyDataSetChanged();
	}
	
	public synchronized void clearData() {
		if(caches != null){
			Log.d(tag, "clear Data size == " + caches.size());
			caches.clear();
		}
		System.gc();
	}
	
	public synchronized void notifyDataChanged(List<T> c) {
	//	clearData();
		caches = c;
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return (loadingbarViewable && (position == caches.size())) ? TYPE_LOADING_VIEW : TYPE_CONTENT_VIEW;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
}
