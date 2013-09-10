package com.roiland.crm.sm.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Attach;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 文档图片列表Adapter
 * </pre>
 * @extends BaseCacheListAdapter
 * @author liuyu
 * @version $Id: ScAttachListAdapter.java, v 0.1 2013-7-5 上午9:00:57 liuyu Exp $
 */
public class ScAttachListAdapter extends BaseCacheListAdapter<Attach> {

	public ScAttachListAdapter(Context context) {
		super(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent, R.layout.sc_photo_list_item);
	}

	@Override
	protected boolean fillView(View view, Attach item) {
		try {
			if (item == null) {
				return true;
			}
			String uploadDateString = null;
			try {
				if (item.getUploadDate() != null) {
					uploadDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(item.getUploadDate()))); 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String attachName = !StringUtils.isEmpty(getName(item.getAttachName())) ? getName(item.getAttachName()) : "";
			String attachType = !StringUtils.isEmpty(getType(item.getAttachName())) ? getType(item.getAttachName()) : "";
			((TextView)view.findViewById(R.id.info_photo_name)).setText(attachName);
			((TextView)view.findViewById(R.id.info_photo_time)).setText(!StringUtils.isEmpty(uploadDateString) ? uploadDateString : "");
			((TextView)view.findViewById(R.id.info_photo_type)).setText(attachType);
			((TextView)view.findViewById(R.id.info_photo_comment)).setText(!StringUtils.isEmpty(item.getComment()) ? item.getComment() : "");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public String getName(String attachName){
	    return attachName.substring(0,attachName.indexOf("."));
	}
	public String getType(String attachName){
        return attachName.substring(attachName.indexOf(".") + 1,attachName.length());
	}
}
