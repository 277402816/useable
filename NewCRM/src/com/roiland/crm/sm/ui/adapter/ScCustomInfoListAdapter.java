package com.roiland.crm.sm.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.ui.widget.CheckBoxViewItem;
import com.roiland.crm.sm.ui.widget.EditTextViewItem;
import com.roiland.crm.sm.ui.widget.NameTextViewItem;
import com.roiland.crm.sm.ui.widget.SelectTextViewItem;
import com.roiland.crm.sm.ui.widget.TextViewItem;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 客户订单adapter
 * </pre>
 *
 * @extends BaseAdapter 
 * @author liuyu
 * @version $Id: ScCustomInfoListAdapter.java, v 0.1 2013-6-21 下午2:57:34 liuyu Exp $
 */
public class ScCustomInfoListAdapter extends BaseAdapter{
    ArrayList<BasicInfoListAdapter.Info> customInfoList;
    BasicInfoListAdapter.Info idnumberInfo;
    BasicInfoListAdapter.Info birthdayInfo;
    BasicInfoListAdapter.Info idtypeInfo;
    Context context;
    int size;
    int num = 0;//判断页面加载几次
    public Project project = null;
    public boolean moreFlag = true;
    public boolean editFlag = false;
    private boolean editData = true;//true 编辑  false 新建
    private boolean addFlag;
    private boolean isDetail = false;
    private String isOldCusCheck = "false";
    private String isBigCusCheck = "false";
    public void isCustInfoListDetail(boolean flg){
        this.isDetail = flg;
    }
    public Customer getCustomer(Customer c){
        Customer customer = null;
        if(c == null){
            customer = new Customer();
        }else{
            customer = c;
        }
        if(isDetail){
            
     
        customer.setCustName(customInfoList.get(0).value);
        customer.setCustFrom(customInfoList.get(1).value);
        customer.setCustFromCode(customInfoList.get(1).pairKey);
        customer.setCustType(customInfoList.get(2).value);
        customer.setCustTypeCode(customInfoList.get(2).pairKey);
        customer.setCollectFrom(customInfoList.get(3).value);
        customer.setCollectFromCode(customInfoList.get(3).pairKey);
        customer.setInfoFrom(customInfoList.get(4).value);
        customer.setInfoFromCode(customInfoList.get(4).pairKey);
        customer.setCustMobile(customInfoList.get(5).value);
        customer.setCustOtherPhone(customInfoList.get(6).value);
        customer.setGender(customInfoList.get(7).value);
        customer.setGenderCode(customInfoList.get(7).pairKey);
        customer.setBirthday(customInfoList.get(8).value);
        customer.setIdType(customInfoList.get(9).value);
        customer.setIdTypeCode(customInfoList.get(9).pairKey);
        customer.setIdNumber(customInfoList.get(10).value);
        customer.setProvince(customInfoList.get(11).value);
        customer.setProvinceCode(customInfoList.get(11).pairKey);
        customer.setCity(customInfoList.get(12).value);
        customer.setCityCode(customInfoList.get(12).pairKey);
        customer.setDistrict(customInfoList.get(13).value);
        customer.setDistrictCode(customInfoList.get(13).pairKey);
        customer.setQq(customInfoList.get(14).value);
        customer.setAddress(customInfoList.get(15).value);
        customer.setPostcode(customInfoList.get(16).value);
        customer.setEmail(customInfoList.get(17).value);
        customer.setConvContactTime(customInfoList.get(18).value);
        customer.setConvContactTimeCode(customInfoList.get(18).pairKey);
        customer.setExpectContactWay(customInfoList.get(19).value);
        customer.setExpectContactWayCode(customInfoList.get(19).pairKey);
        customer.setFax(customInfoList.get(20).value);
        customer.setExistingCar(customInfoList.get(21).value);
        customer.setExistingCarCode(customInfoList.get(21).pairKey);
        customer.setIndustry(customInfoList.get(22).value);
        customer.setIndustryCode(customInfoList.get(22).pairKey);
        customer.setPosition(customInfoList.get(23).value);
        customer.setPositionCode(customInfoList.get(23).pairKey);
        customer.setEducation(customInfoList.get(24).value);
        customer.setEducationCode(customInfoList.get(24).pairKey);
        customer.setExistingCarBrand(customInfoList.get(25).value);
        customer.setCustInterest1(customInfoList.get(26).value);
        customer.setCustInterestCode1(customInfoList.get(26).pairKey);
        customer.setCustInterest2(customInfoList.get(27).value);
        customer.setCustInterestCode2(customInfoList.get(27).pairKey);
        customer.setCustInterest3(customInfoList.get(28).value);
        customer.setCustInterestCode3(customInfoList.get(28).pairKey);
        customer.setExistLisenPlate(customInfoList.get(29).value);
        customer.setEnterpType(customInfoList.get(30).value);
        customer.setEnterpTypeCode(customInfoList.get(30).pairKey);
        customer.setEnterpPeopleCount(customInfoList.get(31).value);
        customer.setEnterpPeopleCountCode(customInfoList.get(31).pairKey);
        customer.setRegisteredCapital(customInfoList.get(32).value);
        customer.setRegisteredCapitalCode(customInfoList.get(32).pairKey);
        customer.setCompeCarModel(customInfoList.get(33).value);
        customer.setCompeCarModelCode(customInfoList.get(33).pairKey);
        customer.setRebuyStoreCustTag(Boolean.valueOf(customInfoList.get(34).value));
        customer.setRebuyOnlineCustTag(Boolean.valueOf(customInfoList.get(35).value));
        customer.setChangeCustTag(Boolean.valueOf(customInfoList.get(36).value)) ;
        customer.setRegularCustTag(Boolean.valueOf(customInfoList.get(37).value));
        customer.setRegularCust(customInfoList.get(38).value);
        customer.setRegularCustCode(customInfoList.get(38).pairKey);
        customer.setLoanCustTag(Boolean.valueOf(customInfoList.get(39).value));
        customer.setHeaderQuartCustTag(Boolean.valueOf(customInfoList.get(40).value));
        customer.setBigCustTag(Boolean.valueOf(customInfoList.get(41).value));
        customer.setBigCusts(customInfoList.get(42).value);
        customer.setBigCustsCode(customInfoList.get(42).pairKey);
        customer.setCustComment(customInfoList.get(43).value);
        if (project != null && project.getCustomer() != null) {
            customer.setDormancy(project.getCustomer().getDormancy());
            }
        }else{
            customer.setCustName(customInfoList.get(0).value);
            customer.setCustFrom(customInfoList.get(1).value);
            customer.setCustFromCode(customInfoList.get(1).pairKey);
            customer.setCustType(customInfoList.get(2).value);
            customer.setCustTypeCode(customInfoList.get(2).pairKey);
            customer.setInfoFrom(customInfoList.get(3).value);
            customer.setInfoFromCode(customInfoList.get(3).pairKey);
            customer.setCustMobile(customInfoList.get(4).value);
            customer.setCustOtherPhone(customInfoList.get(5).value);
        }
        return customer;
    }
    
    public void setCustomerInfo(Customer c){
        customInfoList.get(0).value = c.getCustName();
        customInfoList.get(1).value = c.getCustFrom();
        customInfoList.get(1).pairKey = c.getCustFromCode();
        customInfoList.get(2).value = c.getCustType();
        customInfoList.get(2).pairKey = c.getCustTypeCode();
        customInfoList.get(3).value = c.getCollectFrom();
        customInfoList.get(3).pairKey = c.getCollectFromCode();
        customInfoList.get(4).value = c.getInfoFrom();
        customInfoList.get(4).pairKey = c.getInfoFromCode();
        customInfoList.get(5).value = c.getCustMobile();
        customInfoList.get(6).value = c.getCustOtherPhone();
        customInfoList.get(7).value = c.getGender();
        customInfoList.get(7).pairKey = c.getGenderCode();
        customInfoList.get(8).value = DateFormatUtils.formatDate(c.getBirthday());
        customInfoList.get(9).value = c.getIdType();
        customInfoList.get(9).pairKey = c.getIdTypeCode();
        customInfoList.get(10).value = c.getIdNumber();
        customInfoList.get(11).value = c.getProvince();
        customInfoList.get(11).pairKey = c.getProvinceCode();
        customInfoList.get(12).value = c.getCity();
        customInfoList.get(12).pairKey = c.getCityCode();
        customInfoList.get(13).value = c.getDistrict();
        customInfoList.get(13).pairKey = c.getDistrictCode();
        customInfoList.get(14).value = c.getQq();
        customInfoList.get(15).value = c.getAddress();
        customInfoList.get(16).value = c.getPostcode();
        customInfoList.get(17).value = c.getEmail();
        customInfoList.get(18).value = c.getConvContactTime();
        customInfoList.get(18).pairKey = c.getConvContactTimeCode();
        customInfoList.get(19).value = c.getExpectContactWay();
        customInfoList.get(19).pairKey = c.getExpectContactWayCode();
        customInfoList.get(20).value = c.getFax();
        customInfoList.get(21).value = c.getExistingCar();
        customInfoList.get(21).pairKey = c.getExistingCarCode();
        customInfoList.get(22).value = c.getIndustry();
        customInfoList.get(22).pairKey = c.getIndustryCode();
        customInfoList.get(23).value = c.getPosition();
        customInfoList.get(23).pairKey = c.getPositionCode();
        customInfoList.get(24).value = c.getEducation();
        customInfoList.get(24).pairKey = c.getEducationCode();
        customInfoList.get(25).value = c.getExistingCarBrand();
        customInfoList.get(26).value = c.getCustInterest1();
        customInfoList.get(26).pairKey = c.getCustInterestCode1();
        customInfoList.get(27).value = c.getCustInterest2();
        customInfoList.get(27).pairKey = c.getCustInterestCode2();
        customInfoList.get(28).value = c.getCustInterest3();
        customInfoList.get(28).pairKey = c.getCustInterestCode3();
        customInfoList.get(29).value = c.getExistLisenPlate();
        customInfoList.get(30).value = c.getEnterpType();
        customInfoList.get(30).pairKey = c.getEnterpTypeCode();
        customInfoList.get(31).value = c.getEnterpPeopleCount();
        customInfoList.get(31).pairKey = c.getEnterpPeopleCountCode();
        customInfoList.get(32).value = c.getRegisteredCapital();
        customInfoList.get(32).pairKey = c.getRegisteredCapitalCode();
        customInfoList.get(33).value = c.getCompeCarModel();
        customInfoList.get(33).pairKey = c.getCompeCarModelCode();
        customInfoList.get(34).value = String.valueOf(c.getRebuyStoreCustTag());
        customInfoList.get(35).value = String.valueOf(c.getRebuyOnlineCustTag());
        customInfoList.get(36).value = String.valueOf(c.getChangeCustTag());
        customInfoList.get(37).value = String.valueOf(c.getRegularCustTag());
        customInfoList.get(38).value = c.getRegularCust();
        customInfoList.get(38).pairKey = c.getRegularCustCode();
        customInfoList.get(39).value = String.valueOf(c.getLoanCustTag());
        customInfoList.get(40).value = String.valueOf(c.getHeaderQuartCustTag());
        customInfoList.get(41).value = String.valueOf(c.getBigCustTag());
        customInfoList.get(42).value = c.getBigCusts();
        customInfoList.get(42).pairKey= c.getBigCustsCode();
        customInfoList.get(43).value = c.getCustComment();
        notifyDataSetChanged();
    }
    //点击更多加载更多信息
    public void showMore()
    {
        if(!moreFlag)
        {
            size = customInfoList.size();
        }else
        {
            size = 8;
        }
        moreFlag = !moreFlag;
    }
    //切换编辑状态
    public void showEdit()
    {
        editFlag = !editFlag;
        
    }
    
    public ScCustomInfoListAdapter(ArrayList<BasicInfoListAdapter.Info> customInfoList, Context context) {
        this.customInfoList = customInfoList;
        this.context = context;
    }
    public ScCustomInfoListAdapter(Context context) {
        this.customInfoList = new ArrayList<BasicInfoListAdapter.Info>();
        this.context = context;
    }
    
    public void addItem(String item, String data) {
        addItem(item, data, null);
    }
    public void addItem(String item, String data, String pairKey) {
        BasicInfoListAdapter.Info info = new BasicInfoListAdapter.Info();
        if(item.equals(context.getString(R.string.idType))){
            idtypeInfo = new BasicInfoListAdapter.Info();
            idtypeInfo.key = item;
            idtypeInfo.value = data;
            idtypeInfo.pairKey = pairKey;
            this.customInfoList.add(idtypeInfo);
        }else if(item.equals(context.getString(R.string.birthday))){
            birthdayInfo = new BasicInfoListAdapter.Info();
            birthdayInfo.key = item;
            birthdayInfo.value = data;
            birthdayInfo.pairKey = pairKey;
            this.customInfoList.add(birthdayInfo);
        }
        else if(item.equals(context.getString(R.string.idNumber))){
            idnumberInfo = new BasicInfoListAdapter.Info();
            idnumberInfo.key = item;
            idnumberInfo.value = data;
            idnumberInfo.pairKey = pairKey;
            this.customInfoList.add(idnumberInfo);
            idnumberInfo.textChangeListener = new BasicInfoListAdapter.Info.TextChangeListener() {
                //身份证号同生日联动
                @Override
                public void textChanged(String key, String value) {
                    try {
                        if (!idtypeInfo.value.equals(context.getString(R.string.personID))) 
                        return; 
                        birthdayInfo.setValue(DateFormatUtils.getBirthFromId(idnumberInfo.value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }else{
        info.key = item;
        info.value = data;
        info.pairKey = pairKey;
        this.customInfoList.add(info);
        }
        this.notifyDataSetChanged();            
    }
    
    public void removeItem(String item) {
        for (int i = 0; i < customInfoList.size(); i ++) {
            if (customInfoList.get(i).key.equals(item.replaceAll(":", ""))) {
                customInfoList.remove(i);
                break;
            }
        }
    }
    
    public void setItem(String item, String data) {
        setItem(item, data, null);
    }
    /**
     * 更新客户信息list信息
     * @param item item名称
     * @param data item内容
     * @param pairKey item内容对应code
     */
    public void setItem(String item, String data, String pairKey) {
        //如果页面加载1次或者data不为空值，则为customInfoList中添加新数据
            for (int i = 0; i < customInfoList.size(); i ++) {
                if (customInfoList.get(i).key.equals(item.replaceAll(":", ""))) {
                customInfoList.get(i).value = data;
                customInfoList.get(i).pairKey = pairKey;
                break;
                }
            }
    }
    
    public void removeItem(int index) {
        customInfoList.remove(index);
        this.notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        size = customInfoList.size();
        return size;
    }

    @Override
    public Object getItem(int arg0) {
        arg0 = getIndex(arg0);
        if (arg0 < customInfoList.size()) {
            return customInfoList.get(arg0);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        arg0 = getIndex(arg0);
        TextViewItem textViewItem = null;
        if (arg1 == null) {
            textViewItem = getItemType(customInfoList.get(arg0).key);
        } else {
            textViewItem = (TextViewItem) arg1;
        }
        if(customInfoList.get(arg0).key.contains(context.getString(R.string.bigCustTag))){
            isBigCusCheck = customInfoList.get(arg0).value;
        }
        if(customInfoList.get(arg0).key.contains(context.getString(R.string.regularCustTag))){
            isOldCusCheck = customInfoList.get(arg0).value;
        }
        textViewItem.setKeyText(customInfoList.get(arg0).key + ":");
        textViewItem.setValue(customInfoList.get(arg0).value);
        textViewItem.setTag(customInfoList.get(arg0).key);
        textViewItem.setEditFlag(editFlag);
        

        return textViewItem;
    }
    /**
     * 设置item的类型
     * @param key 项目名
     * @return item 项目类型
     */
    public TextViewItem getItemType(String key){
        if(key.compareTo(context.getString(R.string.custName)) == 0 ){
            NameTextViewItem item = new NameTextViewItem(context);
            item.setEditTextFilter();
            if(!editData){
                item.transferData(editData);
            }
            if(addFlag){
                item.setAddFlag(addFlag);
            }
            if(key.compareTo(context.getString(R.string.custName)) == 0){
                item.setMustItem();
            }
            return item;
            
        }else if(key.compareTo(context.getString(R.string.birthday)) == 0 || key.compareTo(context.getString(R.string.custFrom)) == 0 ||
                key.compareTo(context.getString(R.string.custType)) == 0 || key.compareTo(context.getString(R.string.infoFrom)) == 0 ||
                key.compareTo(context.getString(R.string.collectFrom)) == 0 ||
                key.compareTo(context.getString(R.string.gender)) == 0 || key.compareTo(context.getString(R.string.convContactTime)) == 0 ||
                key.compareTo(context.getString(R.string.expectContactWay)) == 0 || key.compareTo(context.getString(R.string.existingCar)) == 0 ||
                key.compareTo(context.getString(R.string.industry)) == 0 || key.compareTo(context.getString(R.string.province)) == 0 ||
                key.compareTo(context.getString(R.string.city)) == 0 || key.compareTo(context.getString(R.string.idType)) == 0 ||
                key.compareTo(context.getString(R.string.district)) == 0 || key.compareTo(context.getString(R.string.position)) == 0 ||
                key.compareTo(context.getString(R.string.education)) == 0 || key.compareTo(context.getString(R.string.custInterest1)) == 0 ||
                key.compareTo(context.getString(R.string.custInterest2)) == 0 || key.compareTo(context.getString(R.string.custInterest3)) == 0 ||
                key.compareTo(context.getString(R.string.enterpType)) == 0 || key.compareTo(context.getString(R.string.enterpPeopleCount)) == 0 ||
                key.compareTo(context.getString(R.string.registeredCapital)) == 0 || key.compareTo(context.getString(R.string.compeCarModel)) == 0 ||
                key.compareTo(context.getString(R.string.regularCust)) == 0 || key.compareTo(context.getString(R.string.bigCusts)) == 0) {
            SelectTextViewItem item = new SelectTextViewItem(context);
            if(!editData){
                item.transferData(editData);
            }
            if(key.compareTo(context.getString(R.string.custFrom)) == 0 ||
                    key.compareTo(context.getString(R.string.custType)) == 0 || key.compareTo(context.getString(R.string.infoFrom)) == 0 ||
                    key.compareTo(context.getString(R.string.collectFrom)) == 0){
                item.setMustItem();
            }
            if(key.compareTo(context.getString(R.string.bigCusts))==0 && isBigCusCheck.equals("true")){
                item.setMustItem();
            }
            if(key.compareTo(context.getString(R.string.bigCusts))==0 && isBigCusCheck.equals("false")){
                item.locked = true;
            }
            if(key.compareTo(context.getString(R.string.regularCust))==0 && isOldCusCheck.equals("true")){
                item.setMustItem();
            }
            if(key.compareTo(context.getString(R.string.regularCust))==0 && isOldCusCheck.equals("false")){
                item.locked = true;
            }
            return item;
        }else if(key.compareTo(context.getString(R.string.rebuyStoreCustTag)) == 0 || key.compareTo(context.getString(R.string.rebuyOnlineCustTag)) == 0 ||
                key.compareTo(context.getString(R.string.regularCustTag)) == 0 || key.compareTo(context.getString(R.string.loanCustTag)) == 0 ||
                key.compareTo(context.getString(R.string.headerQuartCustTag)) == 0 || key.compareTo(context.getString(R.string.bigCustTag)) == 0 ||
                key.compareTo(context.getString(R.string.changeCustTag)) == 0){
            CheckBoxViewItem item=new CheckBoxViewItem(context);
            if(item!=null && key.compareTo(context.getString(R.string.rebuyStoreCustTag))==0){
                item.locked = true;
            }
            return item;
        }else{
            EditTextViewItem item = new EditTextViewItem(context);
            if(key.compareTo(context.getString(R.string.custMobile)) == 0){
                 item.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            if(key.compareTo(context.getString(R.string.custOtherPhone)) == 0){
                item.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            if( key.compareTo(context.getString(R.string.qq)) == 0 ){
                item.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            if(key.compareTo(context.getString(R.string.idNumber)) == 0){
                item.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            }
            if(key.equals(context.getString(R.string.comment))){
                Log.i("", "sfasf");
            }
            return item;
        }
    }
    
    
    public void clearData()
    {
//        for(int i=0; i<customInfoList.size(); i++){
//            customInfoList.get(i).value = "";
//            customInfoList.get(i).pairKey = "";
//            
//        }
        customInfoList.clear();
    }
    
    private int getIndex(int index){
        if(!moreFlag){
            if(index == 7){
                return customInfoList.size() - 1;
            }
        }
        return index;
    }
    public ArrayList<BasicInfoListAdapter.Info> getCustomInfoList() {
            return customInfoList;
    }

    public void setEditFlag(boolean editFlag) {
        this.editFlag = editFlag;
    }
    
    public void setAddFlag(boolean addFlag){
        this.addFlag = addFlag;
    }
}
