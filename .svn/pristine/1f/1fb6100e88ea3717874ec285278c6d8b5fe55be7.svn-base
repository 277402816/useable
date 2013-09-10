package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.utils.StringUtils;
/**
 * 
 * <pre>
 * 联系人列表Adapter
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: SmContacterListAdapter.java, v 0.1 2013-8-6 上午9:16:31 shuang.gao Exp $
 */
public class SmContacterListAdapter extends BaseCacheListAdapter<Contacter> {

	public SmContacterListAdapter(Context context) {
		super(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent, R.layout.sm_contacter_list_item);
	}

	@Override
	protected boolean fillView(View view, Contacter item) {
		try {
			if (item == null) {
				return true;
			}
			
			((TextView)view.findViewById(R.id.info_contacter_name)).setText(!StringUtils.isEmpty(item.getContName()) ? item.getContName() : "");
			((TextView)view.findViewById(R.id.info_contacter_mobile)).setText(!StringUtils.isEmpty(item.getContMobile()) ? item.getContMobile() : "");
			((TextView)view.findViewById(R.id.info_contacter_relation)).setText(!StringUtils.isEmpty(item.getContRelation()) ? item.getContRelation() : "");
			((TextView)view.findViewById(R.id.info_contacter_type)).setText(!StringUtils.isEmpty(item.getContType()) ? item.getContType() : "");
			((TextView)view.findViewById(R.id.info_contacter_prime)).setText(!StringUtils.isEmpty(item.getIsPrimContanter()) && item.getIsPrimContanter().equals("true") ? "主联系人" : "非主联系人");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
