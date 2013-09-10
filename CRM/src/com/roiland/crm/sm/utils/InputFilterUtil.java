package com.roiland.crm.sm.utils;

import com.roiland.crm.sc.R;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

/**
 * 
 * <pre>
 * 限定EditText输入的长度
 * </pre>
 * @implements InputFilter
 * @author liuyu
 * @version $Id: InputFilterUtil.java, v 0.1 2013-7-31 上午10:57:38 liuyu Exp $
 */

public class InputFilterUtil implements InputFilter{
    private static String tag = "InputFilterUtil";
    Context context;
    int length ;
    String key;
    
    public InputFilterUtil(Context context,String key,int length){
        this.length = length;
        this.key = key;
        this.context = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        if(context != null){
            if(key.equals(context.getString(R.string.preorderCount)) || key.equals(context.getString(R.string.salesQuote))){
                return firstNotZero(source.toString(), dest.toString());
            }else {
                return restrictMount(source.toString(), dest.toString());
            } 
        }
        return "";
    }
    
    private CharSequence firstNotZero(String source,String dest){
        int len = (source.toString() + dest.toString()).length();
        if(dest.length() == 0 && source.equals("0")){
            return "";
        }else{
            if(len >= length){
                return "";
            }else{
                return source;
            }
        }
    }
    
    private CharSequence restrictMount(String source,String dest){
        int len = (source.toString() + dest.toString()).length();
        Log.i(tag, ""+len);
        if(len >= length){
            return "";
        }else{
            return source;
        }
    }

}
