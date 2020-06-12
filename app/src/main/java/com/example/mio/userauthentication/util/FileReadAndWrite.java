package com.example.mio.userauthentication.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.RandomAccessFile;

public class FileReadAndWrite {
    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createFile(File file){
        try{
            if(file.exists()){
                //System.out.println("----- 文件已存在" + file.getAbsolutePath());
                return "";
            }
            if(file.getParentFile().exists()){
                //System.out.println("----- 创建文件" + file.getAbsolutePath());
                file.createNewFile();
            }
            else {
                //创建目录之后再创建文件
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                //System.out.println("----- 创建文件" + file.getAbsolutePath());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String createDir(String dirPath){
        //因为文件夹可能有多层，比如:  a/b/c/ff.txt  需要先创建a文件夹，然后b文件夹然后...
        try{
            File file=new File(dirPath);
            if(file.exists()){
                //System.out.println("----- 目录已存在" + file.getAbsolutePath());
                return file.getAbsolutePath();
            }
            if(file.getParentFile().exists()){
                //System.out.println("----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
                return file.getAbsolutePath();
            }
            else {
                createDir(file.getParentFile().getAbsolutePath());
                //System.out.println("----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return dirPath;
    }

    public static void writeFileWithPath(String path,String fileName,String content){
        try{
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File sdCardDir = Environment.getExternalStorageDirectory();
                String mypath = sdCardDir.getCanonicalPath() + "/" + path;
                //File myDir = new File(mypath);
                //System.out.println(myDir.exists());
                createDir(mypath);
                //System.out.println(myDir.getCanonicalPath());
                writeFile(path + fileName,content);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void writeFile(String fileName,String content){
        try{
            //System.out.println(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
            //System.out.println("Write Test");
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File sdCardDir = Environment.getExternalStorageDirectory();
                //System.out.println(sdCardDir.canRead());
                //System.out.println(sdCardDir.canWrite());
                //System.out.println(sdCardDir.getCanonicalPath() + "/" + fileName);
                File targetFile = new File(sdCardDir.getCanonicalPath() + "/" + fileName);
                RandomAccessFile raf = new RandomAccessFile(targetFile,"rw");
                raf.seek(targetFile.length());
                raf.write(content.getBytes());
                raf.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
