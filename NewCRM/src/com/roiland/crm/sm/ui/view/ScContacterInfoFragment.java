package com.roiland.crm.sm.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.roiland.crm.sm.GlobalConstant;
import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.controller.CRMManager;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.ui.adapter.BasicInfoListAdapter;
import com.roiland.crm.sm.ui.widget.BaseInfoRowViewItem;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.DataVerify.IdcardValidator;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.DialogUtils;
import com.roiland.crm.sm.utils.StringUtils;


/**
 * 
 * <pre>
 * 联系人详细信息Fragment
 * </pre>
 * @extends Fragment
 * @author liuyu
 * @version $Id: ScContacterInfoFragment.java, v 0.1 2013-7-2 上午9:21:21 liuyu Exp $
 */
public class ScContacterInfoFragment extends SherlockFragment {
    private BasicInfoListAdapter contacterInfoAdapter;
    private List<BasicInfoListAdapter.Info> contacterInfo;
    private Contacter contacter = new Contacter();
    private LinearLayout contacterInfoList;
    
    private boolean initEditable = false;
    private boolean newMode = false;
    private boolean addFlag;
    
    private String prjID = null;
    private String customerId = null;
    
    
    BasicInfoListAdapter.Info idnumberInfo;
    BasicInfoListAdapter.Info birthdayInfo;
    MenuItem item;

    public ScContacterInfoFragment(){
        super();
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        addFlag = intent.getBooleanExtra("addFlag", false);
        contacter = intent.getParcelableExtra("contacter");
        prjID = intent.getStringExtra("prjID");
        customerId = intent.getStringExtra("customerID");
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.sc_contacter_info, null, true);
        contacterInfoList = (LinearLayout) view.findViewById(R.id.contacter_info_list);
        
        if (contacterInfoAdapter == null) {
            contacterInfoAdapter = new BasicInfoListAdapter(getActivity());
            contacterInfoAdapter.crmManager = ((RoilandCRMApplication)getActivity().getApplication()).getCRMManager();
        }
        displayContacterInfo();
        contacterInfoAdapter.setEditable(false);
        if (newMode)
            contacterInfoAdapter.setAction("add");
        contacterInfoAdapter.setPrjID(prjID);
        if(initEditable || newMode){
            contacterInfoAdapter.setEditable(true);
        }
        if(addFlag){
            getContacterInfoAdapter().setEditable(!getContacterInfoAdapter().getEditable());
        }
        return view;
    }
    
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setHasOptionsMenu(true);
    }


    //向联系人Adapter中载入信息
    public void displayContacterInfo() {
        if (contacterInfoAdapter == null) return;
        
        contacterInfo = new ArrayList<BasicInfoListAdapter.Info>();
        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.contacter), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (contacter != null ? contacter.getContName() : null),true));
        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.contacter_mobiel), BaseInfoRowViewItem.MOBILETEXT_TYPE, null, (contacter != null ? contacter.getContMobile() : null),false, InputType.TYPE_CLASS_NUMBER));
        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.contacter_other_phone), BaseInfoRowViewItem.PSTNTEXT_TYPE, null, (contacter != null ? contacter.getContOtherPhone() : null),false, InputType.TYPE_CLASS_NUMBER));
        if (contacter != null && StringUtils.isEmpty(contacter.getProjectID())) {
            contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.lead_contacter), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (contacter != null && contacter.getIsPrimContanter() != null && contacter.getIsPrimContanter().equals("true")? String.valueOf(true) : String.valueOf(false)),false));  
        } else {
            contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.lead_contacter), BaseInfoRowViewItem.BOOLEAN2_TYPE, null, (contacter != null && contacter.getIsPrimContanter() != null && contacter.getIsPrimContanter().equals("true")? String.valueOf(true) : String.valueOf(false)),false, false));
        }       
        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.sex), BaseInfoRowViewItem.SELECTION_TYPE
                , (contacter != null ? contacter.getContGenderCode() : null), (contacter != null ? contacter.getContGender() : null),false));
        contacterInfo.add(birthdayInfo = new BasicInfoListAdapter.Info(getString(R.string.birthday), BaseInfoRowViewItem.DATE_TYPE, null, (contacter != null ? contacter.getContBirthday() : null),false));
        contacterInfo.add(idnumberInfo = new BasicInfoListAdapter.Info(getString(R.string.id_card_number), BaseInfoRowViewItem.SIMPLETEXT_TYPE, null, (contacter != null ? contacter.getIdNumber() : null),false));
        idnumberInfo.textChangeListener = new BasicInfoListAdapter.Info.TextChangeListener() {
            //身份证号码与生日联动
            @Override
            public void textChanged(String key, String value) {
                
                try {
                    if(DataVerify.personIdValid(value)){
                        birthdayInfo.setValue(DateFormatUtils.getBirthFromId(value));
                        contacterInfoAdapter.getView(5, null, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.age_range), BaseInfoRowViewItem.SELECTION_TYPE
                , (contacter != null ? contacter.getAgeScopeCode() : null), (contacter != null ? contacter.getAgeScope() : null),false));
        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.contacter_type), BaseInfoRowViewItem.SELECTION_TYPE
                , (contacter != null ? contacter.getContTypeCode() : null), (contacter != null ? contacter.getContType() : null),true));
        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.relation), BaseInfoRowViewItem.SELECTION_TYPE
                , (contacter != null ? contacter.getContRelationCode() : null), (contacter != null ? contacter.getContRelation() : null),false));
        contacterInfo.add(new BasicInfoListAdapter.Info(getString(R.string.valid_date), BaseInfoRowViewItem.DATE_TYPE, null, (contacter != null && contacter.getLicenseValid() != null ? String.valueOf(contacter.getLicenseValid()) : null),false));

        contacterInfoAdapter.setContentList(contacterInfo);
        refreshTable();
    }

    public BasicInfoListAdapter getContacterInfoAdapter() {
        return contacterInfoAdapter;
    }

    public void setContacterInfoAdapter(BasicInfoListAdapter contacterInfoAdapter) {
        this.contacterInfoAdapter = contacterInfoAdapter;
        refreshTable();
    }
    /**
     * 更新联系人详细信息
     */
    @SuppressWarnings("deprecation")
    public void refreshTable() {
        if (contacterInfoList != null && contacterInfoAdapter != null) {
            contacterInfoList.removeAllViews();
            for (int i = 0; i < contacterInfoAdapter.getCount(); i ++) {
                contacterInfoList.addView(contacterInfoAdapter.getView(i, null, null));
                View dividerView = new View(getActivity());
                dividerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
                dividerView.setBackgroundColor(getResources().getColor(R.color.list_divider));
                contacterInfoList.addView(dividerView);
            }
        }
    }
    public Contacter getContacter(){
        return contacter;
    }
    

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.info_menu, menu);
        if(addFlag){
            item = menu.findItem(R.id.save);
        }else{
            item = menu.findItem(R.id.edit);
        }
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                if (!getContacterInfoAdapter().getEditable()) {
                    getContacterInfoAdapter().setEditable(!getContacterInfoAdapter().getEditable());
                } else {
                    //项目校验
                    if (validation()) {
                        //项目校验之后调用异步线程API，新建或者更新联系人信息
                        new BaseTask<Boolean>(getActivity()) {
                            @Override
                            protected Boolean doInBackground(String... params) {
                                
                                CRMManager manager = ((RoilandCRMApplication)getActivity().getApplication()).getCRMManager();
                                try {
                                    manager.updateContacter(getEditedContacter());
                                    return true;
                                } catch (ResponseException e) {
                                    responseException = e;
                                    return false;
                                }
                            }

                            @Override
                            protected void onPostExecute(Boolean result) {
                                
                                super.onPostExecute(result);
                                if (responseException != null) {
                                    Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (result) {
                                        getContacterInfoAdapter().setEditable(false);
                                        item.setIcon(R.drawable.edit_btn_selector);
                                        getContacterInfoAdapter().refresh();
                                        Toast.makeText(getActivity(), getString(R.string.opt_success), Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.opt_fail), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }.execute();
                    }
                }
                if (getContacterInfoAdapter().getEditable()) {
                    item.setIcon(R.drawable.oppoinfo_save_btn);
                } else {
                    item.setIcon(R.drawable.edit_btn_selector);
                }
                break;
            case R.id.save:
                if(validation()){
                    new BaseTask<Boolean>(getActivity()) {

                        @Override
                        protected Boolean doInBackground(String... params) {
                            CRMManager manager = ((RoilandCRMApplication)getActivity().getApplication()).getCRMManager();
                            try {
                                contacter = getEditedContacter();
                                manager.createContacter(contacter);
                            } catch (ResponseException e) {
                                responseException = e;
                                return false;
                            }
                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            super.onPostExecute(result);
                            if(responseException != null){
                                Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                if(result){
                                    getContacterInfoAdapter().setEditable(!getContacterInfoAdapter().getEditable());
                                    getContacterInfoAdapter().refresh();
                                    Toast.makeText(getActivity(), getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                }
                            }
                        }
                        
                        
                    }.execute();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 将联系人信息set到实体类中
     * @return Contacter 联系人信息类
     */
    public Contacter getEditedContacter(){
        Contacter result = new Contacter();
        if(contacter != null){
            result = this.contacter;
        }
        if(addFlag){
            result.setProjectID(prjID);
            result.setCustomerID(customerId);
        }else{
            result.setContacterID(this.contacter.getContacterID());
            result.setCustomerID(this.contacter.getCustomerID());
            result.setProjectID(this.contacter.getProjectID());
        }
        for (BasicInfoListAdapter.Info info : contacterInfo){
            if (info.key.equals(getString(R.string.contacter))){
                result.setContName(info.value);
            }else if (info.key.equals(getString(R.string.contacter_mobiel))){
                result.setContMobile(info.value);
            }else if (info.key.equals(getString(R.string.contacter_other_phone))){
                result.setContOtherPhone(info.value);
            }else if (info.key.equals(getString(R.string.lead_contacter))){
                result.setIsPrimContanter(info.value);
            }else if (info.key.equals(getString(R.string.sex)) && info.pairKey != null){
                result.setContGenderCode(info.pairKey);
                result.setContGender(info.value);
            }else if (info.key.equals(getString(R.string.birthday))){
                result.setContBirthday(info.value);
            }else if (info.key.equals(getString(R.string.id_card_number))){
                result.setIdNumber((info.value));
            }else if (info.key.equals(getString(R.string.age_range)) && info.pairKey != null){
                result.setAgeScopeCode(info.pairKey);
                result.setAgeScope(info.value);
            }else if (info.key.equals(getString(R.string.contacter_type)) && info.pairKey != null){
                result.setContTypeCode(info.pairKey);
                result.setContType(info.value);
            }
            else if (info.key.equals(getString(R.string.relation)) && info.pairKey != null){
                result.setContRelation(info.value);
                result.setContRelationCode(info.pairKey);
            }else if (info.key.equals(getString(R.string.valid_date))){
                result.setLicenseValid(info.getLongValue());
            }
        }
        return result;
    }
    public void setEditable(){
        initEditable = true;
    }
    
    public boolean validation() {
        String errString = null;
        List<BaseInfoRowViewItem> viewList = getContacterInfoAdapter().getViewList();
        String temp = getContacterInfoAdapter().getCaches().get(1).value;
        String tempt = getContacterInfoAdapter().getCaches().get(2).value;
        for (int i = 0; i < viewList.size(); i ++) {
            errString = null;
            BaseInfoRowViewItem view = viewList.get(i);
            BasicInfoListAdapter.Info info = getContacterInfoAdapter().getCaches().get(i);
            if (info.key.trim().equals(getString(R.string.contacter))) {
                String value = info.value;
                if (value == null || value.equals("")) {
                    errString = getString(R.string.dataverify_bemust);
                } else if (value.length() > 20)  {
                    errString = getString(R.string.dataverify_contacter_long);
                }
            }
            else if(info.key.trim().contains(getString(R.string.contacter_mobiel)) || info.key.trim().contains(getString(R.string.contacter_other_phone))){
                String value = info.value;
                if (!StringUtils.isEmpty(value)) {
                    if(info.key.trim().contains(getString(R.string.contacter_mobiel)) && ! DataVerify.isPhoneNumberVaild(value)){    
                         errString=getString(R.string.dataverify_phonenumber);
                         
                    } else if(info.key.trim().contains(getString(R.string.contacter_other_phone)) && !DataVerify.isOtherPhoneVaild(value)){
                         errString=getString(R.string.dataverify_otherphonenumber);
                    } 
                }
            }
            else if (info.key.equals(getString(R.string.birthday))) {
                String value = info.value;
                if (!StringUtils.isEmpty(value)) {
                    try {
                        value = DateFormatUtils.formatDate(Long.parseLong(value));
                        if (!(DataVerify.dateCompare1(value, "1909-01-01") && DataVerify.dateCompare2(
                                DataVerify.systemDate(), value))) {
                             errString = getString(R.string.dataverify_birth);
                        }
                    } catch (Exception e) {
                        
                        e.printStackTrace();
                    }
                }
            }
            else if (info.key.equals(getString(R.string.id_card_number))) {
                if (!StringUtils.isEmpty(info.value) ) {
                        IdcardValidator iv = new IdcardValidator();
                        if (!iv.isValidate18Idcard(info.value)) {
                             errString = getResources().getString(R.string.dataverify_id1);
                    }
                }
            }
            else if (info.key.equals(getString(R.string.contacter_type))) {
                String value = view.getTxtValue().getText().toString();
                if (value == null || value.equals("")) {
                    errString = getString(R.string.dataverify_contacter_type);
                }
            }
            if(i >= 1){
                if(StringUtils.isEmpty(temp)&&StringUtils.isEmpty(tempt)){
                    errString = getString(R.string.dataverify_phonenumber_musthave);
                }               
            }
            if (errString != null) {
                break;
            }
        }
        if (errString != null) {
            Toast.makeText(getActivity(), errString, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
