package com.roiland.crm.sm.utils;

import android.graphics.Paint;
import android.text.InputFilter;
import android.text.Spanned;

public class MyInputFilter implements InputFilter{

	 private Paint mPaint; 
	    
	   private int mMaxWidth; 
	 
	   private static final String TAG = "MyInputFilter"; 
	    
	   private int EDIT_WIDTH = 20; 
	   private int EDIT_WIDTH3 =180 ; 
	   private int EDIT_WIDTH2 = 952; 
	    
	   private int mPadding = 5; 
	    
	   public MyInputFilter(Paint paint, int maxWidth,int choose) { 
	      if (paint != null) { 
	         mPaint = paint; 
	      } else { 
	         mPaint = new Paint(); 
	         mPaint.setTextSize(30F); 
	      } 
	 
	      if (maxWidth > 0) { 
	         mMaxWidth = maxWidth - mPadding; 
	      } else if(choose==1){ 
	         mMaxWidth = EDIT_WIDTH; 
	      } else if(choose==2){ 
		         mMaxWidth = EDIT_WIDTH2; 
		      } else if(choose==3){
		    	  mMaxWidth = EDIT_WIDTH3; 
		      }
	 
	   } 
	 
	 
	   @Override 
	   public CharSequence filter(CharSequence source, int start, int end, 
	         Spanned dest, int dstart, int dend) { 
	 
	      float w = mPaint.measureText(dest.toString() + source.toString()); 
	      if (w > mMaxWidth) { 
	         //TODO: remind the user not to input anymore 
	         return ""; 
	      } 
	 
	      return source; 
	   } 
	 
}
