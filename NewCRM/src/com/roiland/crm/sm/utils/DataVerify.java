package com.roiland.crm.sm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;

import com.roiland.crm.sc.R;

public class DataVerify {
    static String telePhoneTemp = null;
    static String IDtemp =null;

    public static String infoValidation(String item, String value,
            String orderId, String orderStatus, String strGiveUp,
            String strGiveUp2,Context context,boolean isGiveUpChecked,boolean editFlag) {

        String errString = null;
        String temp = null;
        String isGiveUp = null;
        String checkCust = null;
        //品牌必填
        if (item.trim().equals(context.getString(R.string.brand_1))) {  
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);

            }

        }
        //签订单时车身颜色 内饰颜色 配置必填
        if (item.trim().equals(context.getString(R.string.outsideColor))) {
            if ((!StringUtils.isEmpty(orderId) || context.getString(R.string.orderStatus_finish).equals(orderStatus))) {
                if (isRequired(value)) {
                    return errString = context.getString(R.string.dataverify_hasorder);

                }
            }

        }

        if (item.trim().equals(context.getString(R.string.insideColor))) {
            if ((!StringUtils.isEmpty(orderId) || context.getString(R.string.orderStatus_finish).equals(orderStatus))) {
                if (isRequired(value)) {
                    return errString = context.getString(R.string.dataverify_hasorder);

                }
            }

        }
        if (item.trim().equals(context.getString(R.string.carConfiguration))) {
            if ((!StringUtils.isEmpty(orderId) || context.getString(R.string.orderStatus_finish).equals(orderStatus))) {
                if (isRequired(value)) {
                    return errString = context.getString(R.string.dataverify_hasorder);

                }
            }

        }

        if (item.trim().equals(context.getString(R.string.flowStatus))) {
            if ((context.getString(R.string.flowStatus_4).equals(value))) {
                return errString = context.getString(R.string.orderStatus_finish);
            }

        }
        //销售报价校验数字，9位整数
        if (item.trim().equals(context.getString(R.string.salesQuote))) {
            if (!StringUtils.isEmpty(value) && !StringUtils.integerNumber(value)) {
                return errString = context.getString(R.string.dataverify_salessquote);
            }
        }
        // 判断预购台数不能超过两位数
        if (item.trim().equals(context.getString(R.string.preorderCount))) {
            if(!StringUtils.isEmpty(value)){
                  String str = value.replace(("^(0+)"), "");
                    if (str.length() > 2) {
                        return errString = context.getString(R.string.dataverify_preorderCount_1);

                    }
                    if ("0".equals(str)) {
                        return errString =  context.getString(R.string.dataverify_preorderCount);

                    }
            }
        }
        // 预购日期不能小于当前期，不能超过一周
        if (item.trim().contains(context.getString(R.string.preorderDate))) {
                try {
                    if (isRequired(value)) {
                        return errString = context.getString(R.string.dataverify_bemust);
                    }else{
                        //预购日期值做不小于当前日期验证
                        if (!dateCompare2(value, systemDate())) {
                            temp = value;
                            return errString = context.getString(R.string.dataverify_preorderDate);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }           
        } 
        // 成交日期不能小于当前期，不能超过一周
        if (item.contains(context.getString(R.string.finish_preorderDate))) {
            try {
                if (!compareDate(value, systemDate())) {
                    temp = value;
                    return errString = context.getString(R.string.dataverify_preorderDate_1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //底盘号校验位数为17位，不允许有字符和汉字
        if (item.trim().equals(context.getString(R.string.chassisNo))) {
            if (!StringUtils.isEmpty(value)) {
                if (isChinese(value) || hasSpecialCharacter(value)
                        || value.length() != 17) {
                    return errString = context.getString(R.string.dataverify_chassisNo);
                }
            }
        }
        //发动机号校验不允许有字符和汉字 小于20
        if (item.trim().equals(context.getString(R.string.engineNo))) {
            if (!StringUtils.isEmpty(value)) {
                if (isChinese(value) || hasSpecialCharacter(value)
                        || value.length() >= 20) {
                    return errString = context.getString(R.string.dataverify_engineNo);
                }
            }
        }
        //牌照号第一个为汉字或“WJ”,且不能存在~、!、@、#、$、%、^、&、*、()等特殊字符 小于20
        if (item.trim().equals(context.getString(R.string.licensePlate))) {
            if (!StringUtils.isEmpty(value)) {
                if (!isLicensePlate(value) || value.length() >= 20) {
                    return errString = context.getString(R.string.dataverify_licensePlate);
                }
            }
        }
        //提车日期不能大于预购日期
        if (item.trim().equals(context.getString(R.string.pickupDate))) {
            if (!StringUtils.isEmpty(value)) {
                if(!isGiveUpChecked){
                    try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
                        Date now = new Date();
                        if (!dateCompare(value, df.format(now))) {
                            return errString = context.getString(R.string.dataverify_pickupDate);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //放弃销售机会放弃原因必填
        if (item.trim().equals(context.getString(R.string.giveupTag))) {
            if (!StringUtils.isEmpty(value))
                isGiveUp = value;
        }
        if (item.trim().equals(context.getString(R.string.giveupReason))) {
            if ("true".equals(strGiveUp)) {
                if (StringUtils.isEmpty(value)) {
                    return errString = context.getString(R.string.dataverify_bemust);
                }
            }
        }
        //勾选老客户推荐 老客户选择必填
        if (item.trim().equals(context.getString(R.string.regularCustTag))) {
            if (!StringUtils.isEmpty(value))
                strGiveUp = value;
        }
        if (item.trim().equals(context.getString(R.string.regularCust))) {
            if ("true".equals(strGiveUp)) {
                if (StringUtils.isEmpty(value)) {
                    return errString = context.getString(R.string.dataverify_bemust);
                }
            }
        }
        //勾选大客户 大客户选择必填
        if (item.trim().equals(context.getString(R.string.bigCustTag))) {
            if (!StringUtils.isEmpty(value))
                strGiveUp2 = value;
        }
        if (item.trim().equals(context.getString(R.string.bigCusts))) {
            if ("true".equals(strGiveUp2)) {
                if (StringUtils.isEmpty(value)) {
                    return errString = context.getString(R.string.dataverify_bemust);
                }
            }
        }
        //发票名称小于50
        if (item.trim().equals(context.getString(R.string.invoiceTitle))) {
            if (!StringUtils.isEmpty(value)) {
                if (value.length() >= 50) {
                    return errString =  context.getString(R.string.dataverify_invoicetitle);

                }
            }
        }
        //备注小于200
        if (item.trim().equals(context.getString(R.string.comment))) {
            if (!StringUtils.isEmpty(value)) {
                if (value.length() >= 200) {
                    return errString =context.getString(R.string.dataverify_comment);
                }
            }
        }
        //客户名称 客户来源 客户类别 信息来源必填
        if (item.trim().equals(context.getString(R.string.custName))) {
            if(!editFlag){
                if (isRequired(value)) {
                    return errString =  context.getString(R.string.dataverify_bemust);
    
                }
                if (value.length() > 50) {
                    return errString = context.getString(R.string.dataverify_custname);
    
                }
            }

        }
        if (item.trim().equals(context.getString(R.string.custFrom))) {
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);

            }

        }
        if (item.trim().equals(context.getString(R.string.custType))) {
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);

            }

        }
        if (item.trim().equals(context.getString(R.string.infoFrom))) {
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);

            }

        }
        if (item.trim().equals(context.getString(R.string.collectFrom))) {
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);

            }

        }
        //客户移动电话01开头长度为12位或者1开头长度为11位
        //客户其他电话只能键入0~9数字 长度>= 7 , <= 12
        if (item.trim().equals( context.getString(R.string.custMobile)) || item.trim().equals( context.getString(R.string.custOtherPhone))) {
            if(!editFlag){
                if (!StringUtils.isEmpty(value)) {
                    telePhoneTemp = value;
                    if (item.trim().equals(context.getString(R.string.custMobile)) && !isPhoneNumberVaild(value)) {
                        return errString = context.getString(R.string.dataverify_phonenumber);
    
                    } else if (item.trim().equals(context.getString(R.string.custOtherPhone))
                            && !isOtherPhoneVaild(value)) {
                        return errString = context.getString(R.string.dataverify_otherphonenumber);
                    }
                }
            }

        }
        //18位身份证号码，前17位必须是数字，最后一位除数字外，可能是"X"，并且从身份证中取出生日信息，生日校验同上；
        //其他证件不能含有中文字符及全角字符、组织证号必须大于4位且不能含有~、!、@、#、$、%、^、&、*、()等特殊字符
        if(item.trim().equals(context.getString(R.string.idType))){
            if (!StringUtils.isEmpty(value)) {
                if(value.equals(context.getString(R.string.personID))){
                    IDtemp = context.getString(R.string.personID);
                }else if(value.equals(context.getString(R.string.organization_id))){
                    IDtemp = context.getString(R.string.organization_id);
                }else{
                    IDtemp = null;
                }
            }
        }
        if (item.trim().equals(context.getString(R.string.idNumber))) { 
            if (!StringUtils.isEmpty(value) && IDtemp != null) {
                if (IDtemp!=null&&IDtemp.equals(context.getString(R.string.personID))) {
                    IdcardValidator iv = new IdcardValidator();
                    if (!iv.isValidate18Idcard(value)) {
                        IDtemp=null;
                        return errString = context.getString(R.string.dataverify_id1);

                    }
                } else if (IDtemp!=null&&IDtemp.equals(context.getString(R.string.organization_id))) {
                    if (value.length() <= 4 || hasSpecialCharacter(value)) {
                        IDtemp=null;
                        return errString = context.getString(R.string.dataverify_id2);
                        
                    }
                } else {
                    if (isChinese(value) || isChinese1(value)) {
                        return errString = context.getString(R.string.dataverify_id3);
                    }
                }

            }
        }
        //生日不能小于1909-01-01或者大于当前时间
        if (item.trim().equals(context.getString(R.string.birthday))) {
            if (!StringUtils.isEmpty(value)) {
                try {
                    if (!(dateCompare(value, DateFormatUtils.parseDateToLongString("1909-01-01")) && dateCompare(
                        DateFormatUtils.parseDateToLongString(systemDate()), value))) {
                        return errString = context.getString(R.string.dataverify_birth);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        //QQ只能键入0~9数字 长度小于20
        if (item.trim().equals(context.getString(R.string.qq))) {
            if (!StringUtils.isEmpty(value)) {
                if ((!isNumVaild(value)) || value.length() >= 20) {
                    return errString =context.getString(R.string.dataverify_qq_number);
                }
            }

        }
        //邮编只能键入0~9数字 长度小于20
        if (item.trim().equals(context.getString(R.string.postcode))) {
            if (!StringUtils.isEmpty(value)) {
                if ((!isNumVaild(value)) || value.length() >= 20) {
                    return errString =context.getString(R.string.dataverify_postnumber);
                }
            }

        }
        // 客户地址 < 100
        if (item.trim().equals(context.getString(R.string.address))) {
            if (!StringUtils.isEmpty(value)) {
                if (value.length() >= 100){
                    return errString =context.getString(R.string.dataverify_address);
                }
            }

        }
        //E-mailE-mail地址填写，“@”前后至少要有一位字符 长度小于50
        if (item.trim().equals("E-mail")) {
            if (!StringUtils.isEmpty(value)) {
                if ((!isEmailVaild(value)) || value.length() >= 50) {
                    return errString = context.getString(R.string.dataverify_email);
                }
            }
        }
        //传真号码只能键入0~9数字 长度大于等于7小于等于12
        if (item.trim().equals(context.getString(R.string.fax))) {
            if (!StringUtils.isEmpty(value)) {
                if ((!isNumVaild(value)) || value.length() > 12
                        || value.length() < 7) {
                    return errString = context.getString(R.string.dataverify_fax_number);
                }
            }

        }
        //现用车牌号第一个为汉字或“WJ”,且不能存在~、!、@、#、$、%、^、&、*、()等特殊字符
        if (item.trim().equals(context.getString(R.string.existLisenPlate))) {
            if (!StringUtils.isEmpty(value)) {
                if (!isLicensePlate(value)) {
                    return errString = context.getString(R.string.dataverify_licensePlate1);
                }
            }
        }
        //备注长度小于200
        if (item.trim().equals(context.getString(R.string.purchasecarintention_comment))) {
            if (!StringUtils.isEmpty(value)) {
                if (value.length() >= 200) {
                    return errString = context.getString(R.string.dataverify_comment);
                }
            }
        }
        //活动类型必填
        if (item.trim().equals(context.getString(R.string.activitytype))) {
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);

            }

        }
        //执行时间必填 不能小于系统日期
        if (item.trim().equals(context.getString(R.string.executetime))) {
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);
            }
            try {
                if(TextUtils.isDigitsOnly(value)){
                    if (!dateCompare(value, DateFormatUtils.parseDateToLongString(systemDate()))) {
                        return errString = context.getString(R.string.dataverify_dotime);
                    } 
                }else{
                    if (!dateCompare(value, systemDate())) {
                        return errString = context.getString(R.string.dataverify_dotime);
                    }
                }
            } catch (Exception e) {
                Log.e("infoValidation", context.getString(R.string.executetime_fail));
            }
        }
        //活动内容必填 小于16位
        if (item.trim().equals(context.getString(R.string.activitycontent))) {
            if (isRequired(value)) {
                return errString = context.getString(R.string.dataverify_bemust);
            } else if (value.length() >= 16) {
                return errString = context.getString(R.string.dataverify_activitycontent);
            }
        }
        
        //领导批示小于120
        if (item.trim().equals(context.getString(R.string.leadercomment))) {
            if (!StringUtils.isEmpty(value.trim())) {
                if ((value.trim().length() > 120)) {
                    return errString = context.getString(R.string.dataverify_leadercomment);
                }
            }else{
                return errString = context.getString(R.string.dataverify_isEmpty);
            }
        }
          
        //邮编为6位
        if(item.trim().equals(context.getString(R.string.postcode))){
            if(!StringUtils.isEmpty(value)){
                if(value.length() != 6){
                    return errString = context.getString(R.string.post_erro);
                }
            }
        }
        return errString;

    }
  
    public static String infoValidationCars(String item, String value,
            String orderStatus) {
        String errString = null;
        if (item.trim().equals("车型")) {
            if ("签订订单".equals(orderStatus)) {
                if (isRequired(value)) {
                    return errString = "产生订单時，品牌，车型，车身颜色，内饰，配置不可为空，请将数据填写完整！";

                }
            }
            if ("递交新车".equals(orderStatus)) {
                if (isRequired(value)) {
                    return errString = "未建立订单，无法递交新车！";

                }
            }
        }
        if (item.trim().equals("车身颜色")) {
            if ("签订订单".equals(orderStatus)) {
                if (isRequired(value)) {
                    return errString = "产生订单時，品牌，车型，车身颜色，内饰，配置不可为空，请将数据填写完整！";

                }
            }

        }

        if (item.trim().equals("内饰颜色")) {
            if (("签订订单".equals(orderStatus))) {
                if (isRequired(value)) {
                    return errString = "产生订单時，品牌，车型，车身颜色，内饰，配置不可为空，请将数据填写完整！";

                }
            }

        }
        if (item.trim().equals("配置")) {
            if (("签订订单".equals(orderStatus))) {
                if (isRequired(value)) {
                    return errString = "产生订单時，品牌，车型，车身颜色，内饰，配置不可为空，请将数据填写完整！";

                }
            }

        }
        return errString;
    }

    // 获得日期型系统日期
    public static String systemDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(date);
        
        return strDate;

    }
    public static String nextMonth(){   
        Date date = new Date();   
        int year=Integer.parseInt(new SimpleDateFormat("yyyy").format(date));//取到年份值   
        int month=Integer.parseInt(new SimpleDateFormat("MM").format(date))+1;//取到鱼粉值   
        int day=Integer.parseInt(new SimpleDateFormat("dd").format(date));//取到天值   
        if(month==0){   
            year-=1;month=12;   
        }   
        else if(day>28){   
            if(month==2){   
                if(year%400==0||(year %4==0&&year%100!=0)){   
                    day=29;   
                }else day=28;   
            }else if((month==4||month==6||month==9||month==11)&&day==31)   
            {   
                day=30;   
            }   
        }   
        String y = year+"";String m ="";String d ="";   
        if(month<10) m = "0"+month;   
        else m=month+"";   
        if(day<10) d = "0"+day;   
        else d = day+"";   
        return y+"-"+m+"-"+d;
    }
        
    // check是否必填,如果为空返回true
    public static boolean isRequired(String str) {
        return StringUtils.isEmpty(str);
    }

    // 校验是否包含中文字符和全角字符，包含返回true
    public static boolean isChinese1(String str) {

        Pattern p = Pattern.compile("[^\\\\x00-\\\\xff]");

        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    // 校验是否包含汉字，包含返回true
    public static boolean isChinese(String str) {
        String regEx = "[\\u4E00-\\u9FA5]";
        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    // 第一个为汉字或“WJ”,且不能存在~、!、@、#、$、%、^、&、*、()等特殊字符
    // 如果牌照号正确 返回true。
    public static boolean isLicensePlate(String str) {
        if (isChinese(str.substring(0, 1))
                || (str.substring(0, 2)).equals("WJ")) {
            if (hasDigitsAndletters(str) || hasOnlyChinese(str)) {
                return true;
            }
        }
        return false;

    }

    // check长度是否超过规定长度
    public static boolean isBeyond(String str, int i) {
        if (str.length() >= i) {
            return true;
        }
        return false;

    }

    // 比较时间大小
    public static boolean dateCompare(String s1, String s2) throws Exception {

        Long d1 = Long.parseLong(s1);
        Long d2 = Long.parseLong(s2);
        // 比较
        if (d1 >= d2) {
            return true;
        } else {
            return false;
        }
    }
    // 比较时间大小
    public static boolean dateCompare1(String s1, String s2) throws Exception {

        Long d1 = DateFormatUtils.parseDateToLong(s1);
        Long d2 = DateFormatUtils.parseDateToLong(s2);
        // 比较
        if (d1 > d2) {
            return true;
        } else {
            return false;
        }
    }
    // 生日比较时间大小
    public static boolean dateCompare2(String s1, String s2) throws Exception {

        Long d1 = DateFormatUtils.parseDateToLong(s1);
        Long d2 = DateFormatUtils.parseDateToLong(s2);
        // 比较
        if (d1 >= d2) {
            return true;
        } else {
            return false;
        }
    }
    // check是否包含~!@#$%^&*<>特殊字符 ，包含返回true，不包含返回false
    public static boolean hasSpecialCharacter(String str) {
        String regEx = "[~。:：!！@#$%^&*<>()（）]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }
    //只是汉字
    public static boolean hasOnlyChinese(String str){
        String regEx = "[\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }
    //只是数字 、字母 
    public static boolean hasDigitsAndletters(String str){
        String regEx = "^[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }
    // check是否全部为数字,返回true则全部为数字
    public static boolean isNumVaild(String str) {
        return TextUtils.isDigitsOnly(str);
    }

    // check手机号码是否正确，输入正确返回true
    public static boolean isPhoneNumberVaild(String num){
        boolean isValid = false;
        String expre = "^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$";
        Pattern pattern = Pattern.compile(expre);
        Matcher matcher = pattern.matcher(num);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    // check其他电话是否正确,若输入正确返回true
    public static boolean isOtherPhoneVaild(String num) {
        boolean isValid = false;
        String expre = "^[0-9]*$";
        Pattern pattern = Pattern.compile(expre);
        Matcher matcher = pattern.matcher(num);
        if (matcher.matches() && num.length() >= 7 && num.length() <= 18) {
            isValid = true;
        }
        return isValid;

    }

    // 判断email格式是否正确,若正确，返回true
    public static boolean isEmailVaild(String email) {
        String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        boolean flag = email.matches(regex);
        return flag;
    }
    
    // check身份证号码填写是否正确
    public static boolean personIdValid(String str) {
       IdcardValidator id = new IdcardValidator();
       return id.isValidate18Idcard(str);
    }

    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            String substr = str.substring(i, i + 1);
            if (!substr.equals("0") && !substr.equals("1")
                    && !substr.equals("2") && !substr.equals("3")
                    && !substr.equals("4") && !substr.equals("5")
                    && !substr.equals("6") && !substr.equals("7")
                    && !substr.equals("8") && !substr.equals("9")
                    && !substr.equals(".")) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareDate(String str1, String str2) {
        Long d1 = DateFormatUtils.parseDateToLong(str1);
        Long d2 = DateFormatUtils.parseDateToLong(str2);
        long l = 24 * 60 * 60 * 1000 * 7;
        if (d2 - d1 >= l) {
            return false;
        } else {
            return true;
        }
    }
    public static String isZero (String str) {
        if(!StringUtils.isEmpty(str)){
            if(str.equals("0")){
                return "";
            }
        }
        return str;

    }
    @SuppressWarnings({"unchecked","unused","all"})   
    public static class IdcardValidator {   
     
       /**  
        * 省，直辖市代码表： { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",  
        * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",  
        * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",  
        * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",  
        * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",  
        * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}  
        */  
       protected String codeAndCity[][] = { { "11", "北京" }, { "12", "天津" },   
               { "13", "河北" }, { "14", "山西" }, { "15", "内蒙古" }, { "21", "辽宁" },   
               { "22", "吉林" }, { "23", "黑龙江" }, { "31", "上海" }, { "32", "江苏" },   
               { "33", "浙江" }, { "34", "安徽" }, { "35", "福建" }, { "36", "江西" },   
               { "37", "山东" }, { "41", "河南" }, { "42", "湖北" }, { "43", "湖南" },   
               { "44", "广东" }, { "45", "广西" }, { "46", "海南" }, { "50", "重庆" },   
               { "51", "四川" }, { "52", "贵州" }, { "53", "云南" }, { "54", "西藏" },   
               { "61", "陕西" }, { "62", "甘肃" }, { "63", "青海" }, { "64", "宁夏" },   
               { "65", "新疆" }, { "71", "台湾" }, { "81", "香港" }, { "82", "澳门" },   
               { "91", "国外" } };   
     
           private String cityCode[] = { "11", "12", "13", "14", "15", "21", "22",   
               "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",   
               "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",   
               "64", "65", "71", "81", "82", "91" };   
     
       // 每位加权因子   
       private int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };   
     
       // 第18位校检码   
       private String verifyCode[] = { "1", "0", "X", "9", "8", "7", "6", "5",   
               "4", "3", "2" };   
     
          
       /**  
        * 验证所有的身份证的合法性  
        * @param idcard  
        * @return  
        */  
       public boolean isValidatedAllIdcard(String idcard) {   
           if (idcard.length() == 15) {   
               idcard = this.convertIdcarBy15bit(idcard);   
           }   
           return this.isValidate18Idcard(idcard);   
       }   
     
       /**  
        * <p>  
        * 判断18位身份证的合法性  
        * </p>  
        * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。  
        * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。  
        * <p>  
        * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。  
        * </p>  
        * <p>  
        * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；  
        * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；  
        * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；  
        * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。  
        * </p>  
        * <p>  
        * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4  
        * 2 1 6 3 7 9 10 5 8 4 2  
        * </p>  
        * <p>  
        * 2.将这17位数字和系数相乘的结果相加。  
        * </p>  
        * <p>  
        * 3.用加出来和除以11，看余数是多少？  
        * </p>  
        * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3  
        * 2。  
        * <p>  
        * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。  
        * </p>  
        *   
        * @param idcard  
        * @return  
        */  
       public boolean isValidate18Idcard(String idcard) {   
            // 非18位为假   
            if (idcard.length() != 18) {   
                return false;   
            }   
            // 获取前17位   
            String idcard17 = idcard.substring(0, 17);   
            // 获取第18位   
            String idcard18Code = idcard.substring(17, 18);   
            char c[] = null;   
            String checkCode = "";   
            // 是否都为数字   
            if (isDigital(idcard17)) {   
                c = idcard17.toCharArray();   
            } else {   
                return false;   
            }   
      
            if (null != c) {   
                int bit[] = new int[idcard17.length()];   
      
                bit = converCharToInt(c);   
      
                int sum17 = 0;   
      
                sum17 = getPowerSum(bit);   
      
                // 将和值与11取模得到余数进行校验码判断   
                checkCode = getCheckCodeBySum(sum17);   
                if (null == checkCode) {   
                    return false;   
                }   
                // 将身份证的第18位与算出来的校码进行匹配，不相等就为假   
                if (!idcard18Code.equalsIgnoreCase(checkCode)) {   
                    return false;   
                }   
            }   
            return true;   
        }   
      
        /**  
         * 验证15位身份证的合法性,该方法验证不准确，最好是将15转为18位后再判断，该类中已提供。  
         *   
         * @param idcard  
         * @return  
         */  
        public boolean isValidate15Idcard(String idcard) {   
            // 非15位为假   
            if (idcard.length() != 15) {   
                return false;   
            }   
      
            // 是否全都为数字   
            if (isDigital(idcard)) {   
                String provinceid = idcard.substring(0, 2);   
                String birthday = idcard.substring(6, 12);   
                int year = Integer.parseInt(idcard.substring(6, 8));   
                int month = Integer.parseInt(idcard.substring(8, 10));   
                int day = Integer.parseInt(idcard.substring(10, 12));   
      
                // 判断是否为合法的省份   
                boolean flag = false;   
                for (String id : cityCode) {   
                    if (id.equals(provinceid)) {   
                        flag = true;   
                        break;   
                    }   
                }   
                if (!flag) {   
                    return false;   
                }   
                // 该身份证生出日期在当前日期之后时为假   
                Date birthdate = null;   
                try {   
                    birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);   
                } catch (ParseException e) {   
                    e.printStackTrace();   
                }   
                if (birthdate == null || new Date().before(birthdate)) {   
                    return false;   
                }   
      
                // 判断是否为合法的年份   
                GregorianCalendar curDay = new GregorianCalendar();   
                int curYear = curDay.get(Calendar.YEAR);   
                int year2bit = Integer.parseInt(String.valueOf(curYear)   
                        .substring(2));   
      
                // 判断该年份的两位表示法，小于50的和大于当前年份的，为假   
                if ((year < 50 && year > year2bit)) {   
                    return false;   
                }   
      
                // 判断是否为合法的月份   
                if (month < 1 || month > 12) {   
                    return false;   
                }   
      
                // 判断是否为合法的日期   
                boolean mflag = false;   
                curDay.setTime(birthdate);  //将该身份证的出生日期赋于对象curDay   
                switch (month) {   
                case 1:   
                case 3:   
                case 5:   
                case 7:   
                case 8:   
                case 10:   
                case 12:   
                    mflag = (day >= 1 && day <= 31);   
                    break;   
                case 2: //公历的2月非闰年有28天,闰年的2月是29天。   
                    if (curDay.isLeapYear(curDay.get(Calendar.YEAR))) {   
                        mflag = (day >= 1 && day <= 29);   
                    } else {   
                        mflag = (day >= 1 && day <= 28);   
                    }   
                    break;   
                case 4:   
                case 6:   
                case 9:   
                case 11:   
                    mflag = (day >= 1 && day <= 30);   
                    break;   
                }   
                if (!mflag) {   
                    return false;   
                }   
            } else {   
                return false;   
            }   
            return true;   
        }   
      
        /**  
         * 将15位的身份证转成18位身份证  
         *   
         * @param idcard  
         * @return  
         */  
        public String convertIdcarBy15bit(String idcard) {   
            String idcard17 = null;   
            // 非15位身份证   
            if (idcard.length() != 15) {   
                return null;   
            }   
      
            if (isDigital(idcard)) {   
                // 获取出生年月日   
                String birthday = idcard.substring(6, 12);   
                Date birthdate = null;   
                try {   
                    birthdate = new SimpleDateFormat("yyMMdd").parse(birthday);   
                } catch (ParseException e) {   
                    e.printStackTrace();   
                }   
                Calendar cday = Calendar.getInstance();   
                cday.setTime(birthdate);   
                String year = String.valueOf(cday.get(Calendar.YEAR));   
      
                idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);   
      
                char c[] = idcard17.toCharArray();   
                String checkCode = "";   
      
                if (null != c) {   
                    int bit[] = new int[idcard17.length()];   
      
                    // 将字符数组转为整型数组   
                    bit = converCharToInt(c);   
                    int sum17 = 0;   
                    sum17 = getPowerSum(bit);   
      
                    // 获取和值与11取模得到余数进行校验码   
                    checkCode = getCheckCodeBySum(sum17);   
                    // 获取不到校验位   
                    if (null == checkCode) {   
                        return null;   
                    }   
      
                    // 将前17位与第18位校验码拼接   
                    idcard17 += checkCode;   
                }   
            } else { // 身份证包含数字   
                return null;   
            }   
            return idcard17;   
        }   
      
        /**  
         * 15位和18位身份证号码的基本数字和位数验校  
         *   
         * @param idcard  
         * @return  
         */  
        public boolean isIdcard(String idcard) {   
            return idcard == null || "".equals(idcard) ? false : Pattern.matches(   
                    "(^\\\\d{15}$)|(\\\\d{17}(?:\\\\d|x|X)$)", idcard);   
        }   
      
        /**  
         * 15位身份证号码的基本数字和位数验校  
         *   
         * @param idcard  
         * @return  
         */  
        public boolean is15Idcard(String idcard) {   
            return idcard == null || "".equals(idcard) ? false : Pattern.matches(   
                    "^[1-9]\\\\d{7}((0\\\\d)|(1[0-2]))(([0|1|2]\\\\d)|3[0-1])\\\\d{3}$",   
                    idcard);   
        }   
      
        /**  
         * 18位身份证号码的基本数字和位数验校  
         *   
         * @param idcard  
         * @return  
         */  
        public boolean is18Idcard(String idcard) {   
            return Pattern   
                    .matches(   
                            "^[1-9]\\\\d{5}[1-9]\\\\d{3}((0\\\\d)|(1[0-2]))(([0|1|2]\\\\d)|3[0-1])\\\\d{3}([\\\\d|x|X]{1})$",   
                            idcard);   
        }   
      
        /**  
         * 数字验证  
         *   
         * @param str  
         * @return  
         */  
        public boolean isDigital(String str) {   
            return str == null || "".equals(str) ? false : str.matches("^[0-9]*$");   
        }   
      
        /**  
         * 将身份证的每位和对应位的加权因子相乘之后，再得到和值  
         *   
         * @param bit  
         * @return  
         */  
        public int getPowerSum(int[] bit) {   
      
            int sum = 0;   
      
            if (power.length != bit.length) {   
                return sum;   
            }   
      
            for (int i = 0; i < bit.length; i++) {   
                for (int j = 0; j < power.length; j++) {   
                    if (i == j) {   
                        sum = sum + bit[i] * power[j];   
                    }   
                }   
            }   
            return sum;   
        }   
        /**
         * 
         * <pre>
         * 含有特殊字符
         * </pre>
         *
         * @param str 要判断的值
         * @return
         */
        public static boolean hasSpecialCharacter(String str) {
            if (str.indexOf("!") >= 0
                    || str.indexOf("@") >= 0
                    || str.indexOf("#") >= 0
                    || str.indexOf("$") >= 0
                    || str.indexOf("%") >= 0
                    || str.indexOf("^") >= 0
                    || str.indexOf("&") >= 0
                    || str.indexOf("*") >= 0
                    || str.indexOf("(") >= 0
                    || str.indexOf(")") >= 0
                    || str.indexOf("<") >= 0
                    || str.indexOf(">") >= 0) {
                return true;
            }
            return false;
        }
        /**  
         * 将和值与11取模得到余数进行校验码判断  
         *   
         * @param checkCode  
         * @param sum17  
         * @return 校验位  
         */  
        public String getCheckCodeBySum(int sum17) {   
            String checkCode = null;   
            switch (sum17 % 11) {   
            case 10:   
                checkCode = "2";   
                break;   
            case 9:   
                checkCode = "3";   
                break;   
            case 8:   
                checkCode = "4";   
                break;   
            case 7:   
                checkCode = "5";   
                break;   
            case 6:   
                checkCode = "6";   
                break;   
            case 5:   
                checkCode = "7";   
                break;   
            case 4:   
                checkCode = "8";   
                break;   
            case 3:   
                checkCode = "9";   
                break;   
            case 2:   
                checkCode = "x";   
                break;   
            case 1:   
                checkCode = "0";   
                break;   
            case 0:   
                checkCode = "1";   
                break;   
            }   
            return checkCode;   
        }   
      
        /**  
         * 将字符数组转为整型数组  
         *   
         * @param c  
         * @return  
         * @throws NumberFormatException  
         */  
        public int[] converCharToInt(char[] c) throws NumberFormatException {   
            int[] a = new int[c.length];   
            int k = 0;   
            for (char temp : c) {   
                a[k++] = Integer.parseInt(String.valueOf(temp));   
            }   
            return a;   
        } 
    }  
    public static String checkOrder(String orderStatus,boolean activityOrder) {
        // TODO Auto-generated method stub
        if(activityOrder){
            if(("订单匹配审核").equals(orderStatus)){
                return "";
            }else {
                return "未进行车辆资源匹配及匹配审核，无法递交新车";
            }
        }else{
            return "未建立订单，无法递交新车！";
        }
    }  

    //验证内网ip是否正确
    public static String InIpCheck(String strInIp){
        
        String reg = "(192\\.168\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" 
                + "(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|"
                + "(172\\.1[6-9]\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" 
                + "(172\\.2[0-9]\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" 
                + "(172\\.3[0-1]\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" ;
        String warnStr = "";
        if(!(Pattern.matches(reg, strInIp))){
            warnStr="您输入的内网ip地址无效，请重新输入！";
        }
        return warnStr;
    }
    //验证内网ip是否正确
    public static String OutIpCheck(String strOutIp){
        
        String reg = "(192\\.168\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" 
                + "(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|"
                + "(172\\.1[6-9]\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" 
                + "(172\\.2[0-9]\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" 
                + "(172\\.3[0-1]\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" ;
        String warnStr = "";
        if(!(Pattern.matches(reg, strOutIp))){
            warnStr="您输入的外网ip地址无效，请重新输入！";
        }
        return warnStr;
    }
    
  //验证内网ip是否正确
    public static String checkIPAddress(String strInIp){
        
        String reg =  "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:{0,1}\\d{1,5})|" ;
        String warnStr = "";
        if(!(Pattern.matches(reg, strInIp))){
            warnStr="网络错误，请重试！";
        }
        return warnStr;
    }
}
