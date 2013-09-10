/**
 * Roiland.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.roiland.crm.sm.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.core.model.OppoFunnel;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author cjyy
 * @version $Id: BasicViewDraw.java, v 0.1 2013-5-29 下午11:10:10 cjyy Exp $
 */
public class BasicViewDraw extends View {
    
    private OppoFunnel oppoFunnel;
    static int totalHeight = 500;
    static int lastPoint = 0;
    private Bitmap backGround;
    private int  resolution;
    
    private int RESOLUTION_1 = 1;
    private int RESOLUTION_2 = 2;
    /**
     * @param context
     */
    public BasicViewDraw(Context context ,OppoFunnel oppoFunnel) {
        super(context);
        if(oppoFunnel!=null){
            this.oppoFunnel =  oppoFunnel;
        }else{
            this.oppoFunnel = new OppoFunnel();
        }
        backGround = BitmapFactory.decodeResource(context.getResources(), R.drawable.list_item_background);
//        setBackgroundResource();
    }
    
    /**
     * 
     * <pre>
     * 根据各流程状态数值计算出相应的高度
     * </pre>
     *
     * @param oppoNum   各流程状态数值
     * @return
     */
    private int getHeightValue(int oppoNum) {
        float percent = ((float) oppoNum / (float)oppoFunnel.getRecordNum());
        Log.e("BasicViewDraw", "percent = "+ percent);
        return  (int) (percent * totalHeight);
    }
    
    /**
     * 
     * <pre>
     * 获取百分比数字
     * </pre>
     *
     * @param oppoNum   各流程状态数值
     * @return
     */
    private int getPercentNum(int oppoNum) {
        float percent = ((float) oppoNum / (float)oppoFunnel.getRecordNum());
        return Math.round(percent * 100);
    }

    /**
     * 
     * <pre>
     * 
     * </pre>
     *
     * @param num
     * @return
     */
    private int getPerHeight(int num) {
        return (totalHeight / 100) * num;
    }
    
    /**
     * 
     * <pre>
     * 获取底部数值
     * </pre>
     * @param point     顶部数值
     * @param oppoNum   各流程状态数值
     * @return
     */
    public int getBottom(int point, int oppoNum) {
        Log.e("BasicViewDraw", "getBottom = lastPoint:"+lastPoint+", oppoNum:"+oppoNum);
        lastPoint = point + getHeightValue(oppoNum);
        return lastPoint;
    }
    
    /**
     * 
     * <pre>
     *  绘制柱形体方法
     * </pre>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param percentNum    百分比数
     * @param left          左边位置
     * @param top           顶边位置
     * @param right         右边位置
     * @param bottom        底边位置
     */
    private void drawCyinder(Canvas canvas, Paint paint, int percentNum, float left, float top, float right, float bottom) {
        if (paint == null)
            paint = new Paint();
        paint.setStrokeWidth((float) 5.0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        for (int i=1; i<=percentNum; i++) {
            float num = top + getPerHeight(i);
            Log.d("drawCyinder", "percentNum =="+percentNum + " , getPerHeight(i)=" +getPerHeight(i) +" ,top + getPerHeight(i) = " + top + getPerHeight(i) + " , num + 100==" + num + 100);
            RectF oval = new RectF(left, num, right, num + 100);
            canvas.drawArc(oval, 0, 180, false, paint);
        }
    }
    
    /**
     * 绘制2维漏斗图形
     * @param canvas
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startNum = 0;
        int nowTop =0;
        if(getHeight() < 1000){
            resolution = RESOLUTION_1;
             startNum = getHeight() / 2;
        }else{
            resolution = RESOLUTION_2;
             startNum = getHeight() / 2 - 80 ;
        }
        totalHeight = getHeight() / 2;
        
        
        int top = 0;
        int bottom = startNum + 100;
        int left = 50;
        int right = getWidth() - left;
        totalHeight = totalHeight - 155;
//        Paint paintA = new Paint();
//        canvas.drawBitmap(backGround, 0, 0, paintA);
        /* 设置背景为白色 */ 
        canvas.drawColor(Color.WHITE);
        
        /* 画各个颜色标识 */
        drawIndicater(canvas ,resolution);

        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setStrokeWidth((float) 9.0);
        /* 设置paint 的style为 FILL：实心 */
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#000080"));
        paint.setStrokeJoin(Join.ROUND);
        
        /* 画固定的椭圆形 */
        RectF oval = new RectF(left, startNum , right, startNum + 110);
        if(oppoFunnel !=null && oppoFunnel.getDisplayNum()==0 && oppoFunnel.getDriveNum()==0 && oppoFunnel.getFirstNum()==0 && 
                oppoFunnel.getNeedNum()==0 && oppoFunnel.getOrderNum()==0 && oppoFunnel.getPriceNum()==0 ){
           
        }else{
            canvas.drawArc(oval, 0, 360, false, paint);
        }
        
        
        bottom = getBottom(startNum, oppoFunnel.getFirstNum());
        /* 设置paint的颜色 */
        paint.setColor(Color.rgb(13, 79, 190));
        Log.e("BasicViewDraw", "onDraw1 [left ："+left+", right : " +right+ ", top : "+ startNum +", bottom : " +bottom+ " ]");
        drawCyinder(canvas, paint, getPercentNum(oppoFunnel.getFirstNum()), left, startNum, right, bottom );
        if(resolution == RESOLUTION_1){
            top = lastPoint - getPercentNum(oppoFunnel.getFirstNum())/2;
            nowTop = top ;
        }else{
            top = lastPoint + 5;
            nowTop = top ;
        }
        bottom = getBottom(top, oppoFunnel.getNeedNum());
        Log.e("BasicViewDraw", "onDraw2 [left ："+left+", right : " +right+ ", top : "+ top +", bottom : " +bottom+ " ]");
        paint.setColor(Color.rgb(13, 190, 175));
        drawCyinder(canvas, paint, getPercentNum(oppoFunnel.getNeedNum()), left, top, right, bottom );
        if(nowTop ==lastPoint){
            top = lastPoint ;
            nowTop = top;
        }else{
            top = lastPoint + 5;
            nowTop = top;
        }
        bottom = getBottom(top, oppoFunnel.getDisplayNum());
        Log.e("BasicViewDraw", "onDraw3 [left ："+left+", right : " +right+ ", top : "+ top +", bottom : " +bottom+ " ]");
        paint.setColor(Color.rgb(160, 0, 0));
        
        drawCyinder(canvas, paint, getPercentNum(oppoFunnel.getDisplayNum()), left, top, right, bottom );
        if(nowTop ==lastPoint){
            top = lastPoint ;
            nowTop = top;
        }else{
            top = lastPoint + 5;
            nowTop = top;
        }
        bottom = getBottom(top, oppoFunnel.getDriveNum());
        Log.e("BasicViewDraw", "onDraw4 [left ："+left+", right : " +right+ ", top : "+ top +", bottom : " +bottom+ " ]");
        paint.setColor(Color.rgb(216, 191, 19));
        drawCyinder(canvas, paint, getPercentNum(oppoFunnel.getDriveNum()), left, top, right, bottom );
        if(nowTop ==lastPoint){
            top = lastPoint ;
            nowTop = top;
        }else{
            top = lastPoint + 5;
            nowTop = top;
        }
        bottom = getBottom(top, oppoFunnel.getPriceNum());
        Log.e("BasicViewDraw", "onDraw5 [left ："+left+", right : " +right+ ", top : "+ top +", bottom : " +bottom+ " ]");
        paint.setColor(Color.rgb(59, 190, 13));
        drawCyinder(canvas, paint, getPercentNum(oppoFunnel.getPriceNum()), left, top, right, bottom );
        
        if(nowTop ==lastPoint){
            top = lastPoint ;
            nowTop = top;
        }else{
            top = lastPoint + 5;
            nowTop = top;
        }
        bottom = getBottom(top, oppoFunnel.getOrderNum());
        Log.e("BasicViewDraw", "onDraw6 [left ："+left+", right : " +right+ ", top : "+ top +", bottom : " +bottom+ " ]");
        paint.setColor(Color.rgb(171, 13, 190));
        drawCyinder(canvas, paint, getPercentNum(oppoFunnel.getOrderNum()), left, top, right, bottom );
        
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth((float) 9.0);
     
       if(resolution == RESOLUTION_1){
           
           /* 画一个实心三角形，遮掩左边部分矩形图 */
           Path pathLeft = new Path();
           
           pathLeft.moveTo(left - 37 , startNum );
//           pathLeft.lineTo(left + (right - left)/2.5f, bottom - 50);
           pathLeft.lineTo(left + 50 + (right - left)/2.5f, bottom + 120);
           pathLeft.lineTo(left - 80, bottom + 120);
           pathLeft.close();
           canvas.drawPath(pathLeft, paint);
           

           /* 画一个实心三角形，遮掩右边部分矩形图 */
           Path pathRight = new Path();
           pathRight.moveTo(right + 37, startNum );
//           pathRight.lineTo(right - (right - left)/2.5f, bottom - 100);
           pathRight.lineTo(right - 50 - (right - left)/2.5f, bottom + 120);
           pathRight.lineTo(right + 80, bottom + 120);
           pathRight.close();
           canvas.drawPath(pathRight, paint);
           
       }else{
           /* 画一个实心三角形，遮掩左边部分矩形图 */
           Path pathLeft = new Path();
           
           pathLeft.moveTo(left - 30 , startNum );
//           pathLeft.lineTo(left + (right - left)/2.5f, bottom - 50);
           pathLeft.lineTo(left + 50 + (right - left)/2.5f, bottom + 120);
           pathLeft.lineTo(left - 80, bottom + 120);
           pathLeft.close();
           canvas.drawPath(pathLeft, paint);
           

           /* 画一个实心三角形，遮掩右边部分矩形图 */
           Path pathRight = new Path();
           pathRight.moveTo(right + 30, startNum );
//           pathRight.lineTo(right - (right - left)/2.5f, bottom - 100);
           pathRight.lineTo(right - 50 - (right - left)/2.5f, bottom + 120);
           pathRight.lineTo(right + 80, bottom + 120);
           pathRight.close();
           canvas.drawPath(pathRight, paint);
       }
        
    }
    
    /**
     * 
     * <pre>
     * 显示漏斗图中各个颜色所代表的数据标识
     * </pre>
     *
     * @param canvas
     */
    private void drawIndicater(Canvas canvas , int resolution) {
        
        int height = getHeight();
        int weigh = getWidth();
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        /* 设置paint 的style为 FILL：实心 */
        paint.setStyle(Paint.Style.FILL);
        /* 字体加粗 */
        paint.setFakeBoldText(true); 
        /* 写字 */
        if(resolution==RESOLUTION_1){
            paint.setTextSize(20);
        }else{
            paint.setTextSize(30);
        }
        /* 统计 */
        canvas.drawText("统计：", 50, 50,paint);
        /* 记录数 */
        canvas.drawText(getResources().getString(R.string.str_record_num) + oppoFunnel.getRecordNum(), (float) (weigh*0.24), (float) (height*0.1),paint);
        /* 总台数 */
        canvas.drawText(getResources().getString(R.string.str_revenue_num) + oppoFunnel.getRevenueNum(), (float) (weigh*0.24), (float) (height*0.14),paint);
        /* 加权后总台数 */
        canvas.drawText(getResources().getString(R.string.str_weight_total_num) + oppoFunnel.getWeightTotalCount(), (float) (weigh*0.24), (float) (height*0.18),paint);
        
        /* 分割线 */
        canvas.drawLine(0, (float) (height*0.2), 720, (float) (height*0.2), paint);

        /* 图形 */
        canvas.drawText("图形：", 50, (float) (height*0.24),paint);
        
            /* 写字 */
            if(resolution==RESOLUTION_1){
                paint.setTextSize(17);
            }else{
                paint.setTextSize(26);
            }
            /* 设置颜色为红色 */
            paint.setColor(Color.rgb(13, 79, 190));
            /* 画一个实心正方形 */
            float startHight1 = (float) (height*0.24);
            float startWeigh1 = (float) (weigh*0.20);
            canvas.drawRect(startWeigh1, startHight1, startWeigh1+30, startHight1+30, paint);
            /* 初次接触 */
            canvas.drawText(getResources().getString(R.string.str_text1) + oppoFunnel.getFirstNum(), startWeigh1 +40, startHight1+25,paint);
            /* 设置颜色为蓝色 */
            paint.setColor(Color.rgb(13, 190, 175));
            /* 画一个实心正方形 */
            canvas.drawRect(startWeigh1, startHight1+40, startWeigh1+30, startHight1+70, paint);
            /* 弄清客户需求 */
            canvas.drawText(getResources().getString(R.string.str_text2) + oppoFunnel.getNeedNum(), startWeigh1 +40, startHight1+65,paint);
            /* 设置颜色为青色 */
            paint.setColor(Color.rgb(160, 0, 0));
            /* 画一个实心正方形 */
            canvas.drawRect(startWeigh1, startHight1 + 80, startWeigh1+30, startHight1 + 110, paint);
            /* 新车展示 */
            canvas.drawText(getResources().getString(R.string.str_text3) + oppoFunnel.getDisplayNum(), startWeigh1 +40, startHight1 + 105,paint);
            
            float startWeigh2 = (float) (weigh*0.6);
            
            /* 设置颜色为绿色 */
            paint.setColor(Color.rgb(59, 190, 13));
            /* 画一个实心正方形 */
            canvas.drawRect(startWeigh2, startHight1, startWeigh2+30, startHight1+30, paint);
            /* 报价 */
            canvas.drawText(getResources().getString(R.string.str_text5) + oppoFunnel.getPriceNum(), startWeigh2 +40, startHight1+25,paint);
            
            /* 设置颜色为黄色 */
            paint.setColor(Color.rgb(216, 191, 19));
            /* 画一个实心正方形 */
            canvas.drawRect(startWeigh2, startHight1+40, startWeigh2+30, startHight1+70, paint);
            /* 试车 */
            canvas.drawText(getResources().getString(R.string.str_text4) + oppoFunnel.getDriveNum(), startWeigh2 +40, startHight1+65,paint);

    

            /* 设置颜色为粉色 */
            paint.setColor(Color.rgb(171, 13, 190));
            /* 画一个实心正方形 */
            canvas.drawRect(startWeigh2, startHight1 + 80, startWeigh2+30, startHight1 + 110, paint);
            /* 签订单 */
            canvas.drawText(getResources().getString(R.string.str_text6) + oppoFunnel.getOrderNum(), startWeigh2 +40, startHight1+105,paint);
        }
     
}
