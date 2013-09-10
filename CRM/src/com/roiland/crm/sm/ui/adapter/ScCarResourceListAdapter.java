package com.roiland.crm.sm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 车辆资源列表信息Adapter
 * </pre>
 *
 * @author Zhao.jiaqi
 * @version $Id: ScCarResourceListAdapter.java, v 0.1 2013-8-2 下午4:46:33 Zhao.jiaqi Exp $
 */
public class ScCarResourceListAdapter extends BaseCacheListAdapter<Vehicle> {
	public ScCarResourceListAdapter(Context context) {
		super(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position > 20){
			 return super.getView(position , null, parent, R.layout.sm_car_resource_list_item);  
		}else 
		return super.getView(position, convertView, parent, R.layout.sm_car_resource_list_item);
	}
	@Override
	protected boolean fillView(View view, Vehicle item) {
		try {
			((TextView)view.findViewById(R.id.car_resource_list_chasis_no)).setText(StringUtils.notNull(item.getChassisNo().trim()));
			((TextView)view.findViewById(R.id.car_resource_list_vehicle_config)).setText(StringUtils.notNull(item.getVehiConfig().trim()));
			((TextView)view.findViewById(R.id.car_resource_list_interior_color)).setText(StringUtils.notNull(item.getInsideColor().trim()));
			((TextView)view.findViewById(R.id.car_resource_list_car_status)).setText(StringUtils.notNull(item.getVehiStatus().trim()));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
