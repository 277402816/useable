package com.roiland.crm.sm.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.ui.adapter.SmContacterInfoAdapter;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 联系人详细信息Fragment
 * </pre>
 * @extends Fragment
 * @author liuyu
 * @version $Id: SmContacterInfoFragment.java, v 0.1 2013-5-20 下午2:14:28 liuyu Exp $
 */
public class SmContacterInfoFragment extends Fragment {
	private SmContacterInfoAdapter contacterInfoAdapter;
	private Contacter contacter = new Contacter();
	private LinearLayout contacterInfoList;

	public SmContacterInfoFragment(Contacter contacter)
	{
		super();
		this.contacter = contacter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.sm_contacter_info, null, true);
		contacterInfoList = (LinearLayout) view.findViewById(R.id.contacter_info_list);
		
		if (contacterInfoAdapter == null) {
			contacterInfoAdapter = new SmContacterInfoAdapter(getActivity());
		}
		displayContacterInfo();
		for(int i=0;i<contacterInfoAdapter.getCount();i++){
		    contacterInfoList.addView(contacterInfoAdapter.getView(i, null, null));
		}
		
        return view;
	}
	
	//向联系人Adapter中载入信息
	public void displayContacterInfo() {
		if (contacterInfoAdapter == null) return;
		
		contacterInfoAdapter.addView("联系人",(contacter != null ? contacter.getContName() : null));
		contacterInfoAdapter.addView("联系人手机",(contacter != null ? contacter.getContMobile() : null));
		contacterInfoAdapter.addView("其他电话",(contacter != null ? (contacter.getContOtherPhone() != null ? contacter.getContOtherPhone().trim() :null) : null));
		contacterInfoAdapter.addView("主联系人",(contacter != null ? contacter.getIsPrimContanter() : null));
		contacterInfoAdapter.addView("性别",(contacter != null ? contacter.getContGender() : null));
		contacterInfoAdapter.addView("生日",(contacter != null ? StringUtils.isEmpty(contacter.getContBirthday()) ? "": DateFormatUtils.formatDate(contacter.getContBirthday()): null));
		contacterInfoAdapter.addView("身份证号码",(contacter != null ? contacter.getIdNumber() : null));
		contacterInfoAdapter.addView("年龄范围",(contacter != null ? contacter.getAgeScope() : null));
		contacterInfoAdapter.addView("联系人类型",(contacter != null ? contacter.getContType() : null));
		contacterInfoAdapter.addView("与客户关系",(contacter != null ? contacter.getContRelation() : null));
		contacterInfoAdapter.addView("驾驶证有效期",((contacter != null && contacter.getLicenseValid()!=null) ? DateFormatUtils.formatDate(contacter.getLicenseValid()) : null));
		View dividerView = new View(getActivity());
        dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
        dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
        contacterInfoList.addView(dividerView);
	}
}
