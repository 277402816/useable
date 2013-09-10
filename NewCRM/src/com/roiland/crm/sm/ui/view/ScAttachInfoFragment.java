package com.roiland.crm.sm.ui.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.roiland.crm.sc.R;
import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.core.model.Attach;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.FileUtil;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.MulitPointTouchListener;

/**
 * 
 * <pre>
 * Fragment 列出文档图片信息
 * </pre>
 * extends Fragment
 * @author liuyu
 * @version $Id: ScAttachInfoFragment.java, v 0.1 2013-7-5 上午9:08:22 liuyu Exp $
 */

@SuppressLint("ValidFragment")
public class ScAttachInfoFragment extends Fragment {
	Attach attach;	//显示的文档图片信息
	ImageView imageView; //image view用来显示attach bitmap
    DisplayMetrics     dm;        
    Matrix             matrix      = new Matrix();
    Bitmap bitmap;
    static String tempFileName = "tempbitmap";
	/**
	 * 构造方法
	 * @param attach : 应该显示的文档图片信息
	 */
    public ScAttachInfoFragment(Attach attach) {
		this.attach = attach;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	/**
	 * 回调方法
	 * 当页面布局被创建时调用onCreateView方法
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sm_attachment_info_fragment, container, false);
		imageView = (ImageView)view.findViewById(R.id.attachment_info_imageview);
        if(attach.getPictureSuffix().toLowerCase().contains("png") || attach.getPictureSuffix().toLowerCase().contains("jpg")){
            downloadFile();
		}else{
            //只支持以上两种格式
            new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.prompts)).setItems(
                new String[] {getActivity().getResources().getString(R.string.format_alert) }, null).setNegativeButton(
                    getResources().getString(R.string.cancel), null).show();
		}
        return view;
	}
	
	/**
	 * 下载attach bitmap的异步方法
	 * @param url : attach bitmap文件的路径
	 */
	public void downloadFile() {
		new BaseTask<Bitmap>(getActivity()) {
			@Override
			protected Bitmap doInBackground(String... params) {
				try {
                    InputStream is = ((RoilandCRMApplication) getActivity()
                            .getApplication()).getCRMManager().downloadFile(
                            attach.getAttachmentID());
                    File data = readStream(is, getActivity());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
                    Log.i("-----------STask", data.getPath());
                    Bitmap bitmap = BitmapFactory.decodeFile(data.getPath(), options);
					return bitmap;
				} catch (ResponseException ex) {
				    responseException = ex;
				}
				return null;
			}
			
	         @Override
	            protected void onPostExecute(Bitmap result) {
	             super.onPostExecute(result);
	             if(responseException != null){
	                 Toast.makeText(getActivity(), responseException.getMessage(), Toast.LENGTH_LONG).show();
	                 return ;
	             }
	             if(result != null){
	                 bitmap = result;
	                 dm = new DisplayMetrics();
	                 imageView.setOnTouchListener(new MulitPointTouchListener (getActivity(),dm,matrix,bitmap,imageView));// 设置触屏监听

	                 getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率 
	                 center();
	                 imageView.setImageBitmap(result);
	                 imageView.setImageMatrix(matrix); 
	             }
	            }
		}.execute();	
		
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
                deltaY = imageView.getHeight() - rect.bottom;
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
    
    /**
     * 读取图片
     */
     
    public static File readStream (InputStream inputStream,Activity activity) throws ResponseException {
        File temp = null;
        FileOutputStream outStream = null;      
        byte[] buffer = new byte[1024];        
        int len = 0;        
        int size = 0;
        try {
            temp = FileUtil.createTempFile(tempFileName, activity);
            outStream = new FileOutputStream(temp);   
            while( (len = inputStream.read(buffer)) != -1){        
                outStream.write(buffer, 0, len);       
                size += len;
                if (size > (1024 * 1024 * 8)) {
                    throw new ResponseException(activity.getString(R.string.message));
                }
            }
            
        } catch (IOException e) {
            throw new ResponseException(e);
        } finally {
            try {
                outStream.close();
                inputStream.close(); 
            } catch (IOException e) {
                
            }        
        }
        return temp;
        
    }
    
}
