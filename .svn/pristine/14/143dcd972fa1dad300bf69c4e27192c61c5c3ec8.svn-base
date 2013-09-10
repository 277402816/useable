package com.roiland.crm.sm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
/*
 * Utilities class for Image processing
 */
public class BitmapUtil {
	private static final String tag = "BitmapUtil";

	private static PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(Mode.SRC_IN);

	private static final float ROUND_PX = 12;
	private static final int COLOR = 0xff424242;

	public static Bitmap toBitmapNoColor(Bitmap bitmap, boolean needRecycle) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		try {
			int gray = 0;
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int bitmapFromPixels[] = new int[width * height];
			int bitmapToPixels[] = new int[width * height];
			float temp = 0.9f;
			bitmap.getPixels(bitmapFromPixels, 0, width, 0, 0, width, height);
			for (int i = 0; i < bitmapFromPixels.length; i++) {
				int alfa = (bitmapFromPixels[i] >> 24) & 0xff;
				if (alfa == 0) {
					continue;
				}
				gray = (int) (((bitmapFromPixels[i] >> 16) & 0xff) * 0.3 * temp);
				gray += (int) (((bitmapFromPixels[i] >> 8) & 0xff) * 0.59 * temp);
				gray += (int) (((bitmapFromPixels[i]) & 0xff) * 0.11 * temp);
				bitmapToPixels[i] = (alfa << 24) | (gray << 16) | (gray << 8) | gray;
			}
			Bitmap newBitmap = Bitmap.createBitmap(bitmapToPixels, width, height, bitmap.getConfig());
			return newBitmap;
		} finally {
			if (needRecycle) {
				bitmap.recycle();
			}
		}
	}

	public static Bitmap fixBitmap(Bitmap bitmap, int screenWidth, int screenHeight) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		Bitmap scaleBitmap = bitmap;
		int w = scaleBitmap.getWidth(), h = scaleBitmap.getHeight();
		if (w > screenWidth || h > screenHeight||(w < screenWidth && h < screenHeight)) {
			float xrate = (.0f + screenWidth) / w;
			float yrate = (.0f + screenHeight) / h;
			float rate = Math.min(xrate, yrate);
			w = Math.round(w * rate);
			h = Math.round(h * rate);
			scaleBitmap = Bitmap.createScaledBitmap(scaleBitmap, w, h, false);
		}

		Log.i(tag, "scaleBitmap scaleBitmap.getWidth() after.... " + scaleBitmap.getWidth());
		return scaleBitmap;
	}

	public static Bitmap toMarkedBitmap(Bitmap src, Bitmap mark) {
		if (src == null || mark == null) {
			return null;
		}

		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);
		cv.drawBitmap(mark, w * 2 / 3, h * 1 / 2, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newb;
	}

	public static Bitmap toRoundedCornerBitmap(Bitmap bitmap, boolean recycled) {
		return toRoundedCornerBitmap(bitmap, 1, recycled);
	}

	public static Bitmap toRoundedCornerBitmap(Bitmap bitmap) {
			return toRoundedCornerBitmap(bitmap, 1, false);
	}

	private static Paint roundedPaint;
	private static Paint xmodePaint;
	private static synchronized void initPaint() {
		if (roundedPaint == null) {
			roundedPaint = new Paint();
			roundedPaint.setAntiAlias(true);
			roundedPaint.setColor(COLOR);

			xmodePaint = new Paint(roundedPaint);
			xmodePaint.setXfermode(porterDuffXfermode);
		}
	}

	public static Bitmap toRoundedCornerBitmap(Bitmap bitmap, float resizeScale, boolean recycled) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		try {
			int bmpWidth = (int) (bitmap.getWidth() * resizeScale);
			int bmpHeight = (int) (bitmap.getHeight() * resizeScale);

			Bitmap output = null;
			try {
				output = Bitmap.createBitmap(bmpWidth, bmpHeight,
						Config.RGB_565);
			} catch (OutOfMemoryError ex) {
				Log.e(tag, "", ex);
				return null;
			}

			Canvas canvas = new Canvas();
			canvas.setBitmap(output);
			canvas.drawARGB(0, 0, 0, 0);
			Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF dst = new RectF(0, 0, bmpWidth, bmpHeight);

			initPaint();
//			Paint roundedPaint = new Paint();
//			roundedPaint.setAntiAlias(true);
//			roundedPaint.setColor(COLOR);
//			Paint xmodePaint = new Paint(roundedPaint);
//			xmodePaint.setXfermode(porterDuffXfermode);

			canvas.drawRoundRect(dst, ROUND_PX, ROUND_PX, roundedPaint);
			canvas.drawBitmap(bitmap, src, dst, xmodePaint);
			return output;
		} finally {
			if (recycled) {
				bitmap.recycle();
			}
		}
	}

	public static  int getRectWidthOffset(Rect rect,int screenWidth){
		return (screenWidth-(rect.right-rect.left))>>1;

	}

	public static int getDrawableBoundsWidth(Bitmap bitmap, int screenWidth, int screenHeight) {
		int w = bitmap.getWidth(), h = bitmap.getHeight();

		if (w > screenWidth || h > screenHeight||(w < screenWidth && h < screenHeight)) {
			float xrate = (.0f + screenWidth) / w;
			float yrate = (.0f + screenHeight) / h;
			float rate = Math.min(xrate, yrate);
			w = Math.round(w * rate);
		}
		return w;
	}

	public static int getDrawableBoundsWidthOffset(Bitmap bitmap, int screenWidth, int screenHeight) {
		int w = bitmap.getWidth(), h = bitmap.getHeight();

		if (w > screenWidth || h > screenHeight||(w < screenWidth && h < screenHeight)) {
			float xrate = (.0f + screenWidth) / w;
			float yrate = (.0f + screenHeight) / h;
			float rate = Math.min(xrate, yrate);
			w = Math.round(w * rate);
		}
		return (screenWidth-w)>>1;
	}

	public static Rect getDrawableBounds(Bitmap bitmap, int screenWidth, int screenHeight) {
		return getDrawableBounds(bitmap, screenWidth, screenHeight, 0);
	}

	public static Rect getDrawableBounds(Bitmap bitmap, int screenWidth, int screenHeight, int offsetY) {
		int w = bitmap.getWidth(), h = bitmap.getHeight();

		if (w > screenWidth || h > screenHeight||(w < screenWidth && h < screenHeight)) {
			float xrate = (.0f + screenWidth) / w;
			float yrate = (.0f + screenHeight) / h;
			float rate = Math.min(xrate, yrate);
			w = Math.round(w * rate);
			h = Math.round(h * rate);
		}
		int startX = (screenWidth - w) >> 1;
		int startY = (screenHeight - h) >> 1;
		return new Rect(startX, startY - offsetY, startX + w, startY + h - offsetY);
	}

	public static Rect getDrawableBounds(int startX, Bitmap bitmap,
			int screenWidth, int screenHeight) {
		return getDrawableBounds(startX, bitmap, screenWidth, screenHeight, 0);
	}

	public static Rect getDrawableBounds(int startX, Bitmap bitmap,
			int screenWidth, int screenHeight, int offsetY) {
		int w = bitmap.getWidth(), h = bitmap.getHeight();


		if (w > screenWidth || h > screenHeight||(w < screenWidth && h < screenHeight)) {
			float xrate = (.0f + screenWidth) / w;
			float yrate = (.0f + screenHeight) / h;
			float rate = Math.min(xrate, yrate);
			w = Math.round(w * rate);
			h = Math.round(h * rate);
		}

		int startY = (screenHeight - h) >> 1;
		return new Rect(startX, startY - offsetY, startX + w, startY + h - offsetY);
	}

	public static int[] getImageWH(File file) {
		int[] wh = { -1, -1 };
		if (file != null && file.exists() && !file.isDirectory()) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				InputStream is = new FileInputStream(file);
				BitmapFactory.decodeStream(is, null, options);
				wh[0] = options.outWidth;
				wh[1] = options.outHeight;
			} catch (Exception e) {
				Log.w(tag, e.getMessage(), e);
			}
		}

		return wh;
	}

	public static Bitmap openBitmapByScale(File file, int showWidth, int showHeight) {
		Bitmap bitmap = null;
		try {
			int[] wh = getImageWH(file);
			if (wh[0] == -1 || wh[1] == -1) {
				return null;
			}

			int bitmapWidth = wh[0], bitmapHeight = wh[1];
			BitmapFactory.Options options = new BitmapFactory.Options();
			double scaleW = (bitmapWidth + .0) / showWidth;
			double scaleH = (bitmapHeight + .0) / showHeight;
			double scale = Math.max(scaleW, scaleH);
			options.inSampleSize = (int) Math.round(scale);
			InputStream is = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (Exception e) {
			Log.w(tag, e.getMessage(), e);
		} catch (OutOfMemoryError e) {
			Log.w(tag, e.getMessage(), e);
		}
		return bitmap;
	}

}
