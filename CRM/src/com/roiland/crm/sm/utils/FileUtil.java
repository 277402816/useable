package com.roiland.crm.sm.utils;

import java.io.File;
import java.io.IOException;

import com.roiland.crm.sm.RoilandCRMApplication;
import com.roiland.crm.sm.ui.view.LoginActivity;

import android.content.Context;
import android.os.Environment;

/**
 * 
 * <pre>
 * 创建文件
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: FileUtil.java, v 0.1 2013-6-20 下午3:39:09 shuang.gao Exp $
 */
public class FileUtil {
    public static String PATH;
    public static File updateDir = null;
    public static File updateFile = null;

    public static void createFile(String name) {
        
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState())) {
          //获取外部存储的路径返回绝对路径的,其实就是你的SD卡的文件路径
            updateDir = new File(Environment.getExternalStorageDirectory()
                    + "/" + LoginActivity.downloadDir);
            updateFile = new File(updateDir + "/" + name + ".apk");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    
    public static File createTempFile(String tempFile, Context context) {

        if (Environment.getExternalStorageState().equals(  
            Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中  
            PATH = Environment.getExternalStorageDirectory()  
                    .getAbsolutePath() + File.separator + "RoilandCRMLog";  
        } else {// 如果SD卡不存在，就保存到本应用的目录下  
            PATH = context.getFilesDir().getAbsolutePath()  
                    + File.separator + "RoilandCRMLog";  
        }  
        Log.d("FileUtil", "Path == " + PATH);
        File file = new File(PATH);  
        if (!file.exists()) {  
            file.mkdirs();  
        }  
        File temp = new File(file.getPath() + "/" + tempFile);  
        if (temp.exists()) {
            temp.delete();
        }
        return temp;
    }
}
