package com.suicuntong.sct.utils;

import com.suicuntong.sct.entity.File;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {

    /**
     * 找到上级文件夹
     */
    public String getPrePath(String folderPath){
        String prePath = folderPath;
        if (!folderPath.equals("/")){
            if(folderPath.lastIndexOf('/') == 0){
                prePath = "/";
            }else {
                prePath = folderPath.substring(0,folderPath.lastIndexOf('/'));
            }
        }
        return prePath;
    }

    public static String getFileNameFromPath(String path){
        String[] paths = path.split("/");
        if(paths.length == 1) {
            paths = path.split("\\\\");
        }
        return paths[paths.length-1];
    }



    /**
     * 文件路径转换
     */
    public String getPath(String path){
        String newPath = path;
        int thirdTime = path.indexOf('/');
        for(int i=0;i<2;i++) {
            thirdTime=path.indexOf('/',thirdTime+1);
        }
        newPath = newPath.substring(thirdTime);
        return newPath;
    }

    public String getTime(String beginDate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)));
        return sd;
    }

    /**
     * 将字节转化为 KB MB GB
     * @param size
     * @return
     */
    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            }
            else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 文件夹排在前面
     * @param fileList
     * @return
     */
    public static List<File> SortDirectory(List<File> fileList){
        List<File> tempItems = new ArrayList<>();
        List<File> otherItems = new ArrayList<>();
        for(File file : fileList) {
            if(file.getType().equals(file.getTypeFolder())){
                tempItems.add(file);
            } else {
                otherItems.add(file);
            }
        }
        for (File file : otherItems){
            tempItems.add(file);
        }
        return tempItems;
    }
}
