package com.roiland.crm.sm.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 
 * <pre>
 * 实现图片缩放处理
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: MulitPointTouchListener.java, v 0.1 2013-7-2 上午9:43:53 shuang.gao Exp $
 */
public class MulitPointTouchListener implements OnTouchListener {
    Activity           activity;
    Matrix             matrix      = new Matrix(); ;
    Matrix             savedMatrix = new Matrix();
    DisplayMetrics     dm;
    static ImageView   imgView;
    static Bitmap      bitmap;
    static float       MINSCALER   = 1f;          // 最小缩放比例
    static final float MAX_SCALE   = 4f;          // 最大缩放比例
    static final int   NONE        = 0;           // 初始状态     
    static final int   DRAG        = 1;           // 拖动    
    static final int   ZOOM        = 2;           // 缩放     
    int                mode        = NONE;
    PointF             prev        = new PointF();
    PointF             mid         = new PointF();
    float              dist        = 1f;

    public MulitPointTouchListener(Activity activity, DisplayMetrics dm, Matrix matrix,
                                   Bitmap bitmap, ImageView imageView) {
        super();
        this.activity = activity;
        this.dm = dm;
        this.matrix = matrix;
        this.bitmap = bitmap;
        this.imgView = imageView;

    }

    /**      * 触屏监听      */
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // 主点按下         
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                prev.set(event.getX(), event.getY());
                mode = DRAG;
                break; // 副点按下         
            case MotionEvent.ACTION_POINTER_DOWN:
                dist = spacing(event);
                // 如果连续两点距离大于10，则判定为多点模式
                if (spacing(event) > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - prev.x, event.getY() - prev.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float tScale = newDist / dist;
                        matrix.postScale(tScale, tScale, mid.x, mid.y);
                    }
                }
                break;
        }
        imgView.setImageMatrix(matrix);
        //        CheckView();
        //        minZoom();
        return true;
    }

    /**      * 限制最大最小缩放比例，自动居中      */
    private void CheckView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < MINSCALER) {
                matrix.setScale(MINSCALER, MINSCALER);
            }
            if (p[0] > MAX_SCALE) {
                matrix.set(savedMatrix);
            }
        }
        center();
    }

    /**      * 最小缩放比例，最大为100%      */
    private void minZoom() {
        MINSCALER = Math.min((float) dm.widthPixels / (float) bitmap.getWidth(),
            (float) dm.heightPixels / (float) bitmap.getHeight());
        if (MINSCALER < 1.0) {
            matrix.postScale(MINSCALER, MINSCALER);
        }
    }

    private void center() {
        center(true, true);
    }

    /**      * 横向、纵向居中      */
    private void center(boolean horizontal, boolean vertical) {
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;
        if (vertical) {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imgView.getHeight() - rect.bottom;
            }
        }
        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**      * 两点的距离      */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**      * 两点的中点      */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
