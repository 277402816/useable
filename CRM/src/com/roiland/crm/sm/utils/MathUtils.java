package com.roiland.crm.sm.utils;

import java.util.Random;

public class MathUtils {
	public static float randomFloat(float min,float max){
    	Random rnd=new Random();
    	float di=max-min;
    	int diff=Math.round(di*1000);
    	int num=rnd.nextInt(diff)+Math.round(min*1000);
    	float ff=((float)num)/1000;
    	return ff;
    }
	public static float splitFloat(float f){
		int i=Math.round(f*100);
		float ff=((float)i)/100;
		return ff;
	}
}
