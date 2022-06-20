package com.suicuntong.sct.service;



import com.suicuntong.sct.entity.File;
import com.suicuntong.sct.entity.User;
import com.suicuntong.sct.vo.ResponseResult;

import java.util.List;


public interface  FileService {

    List<File> displayAllList(String path); //显示当前目录结构
    ResponseResult createFile(User user, String fileName, String path); //新建文件
    ResponseResult createDir(User user,String dirName, String path); //新建文件夹
    ResponseResult copy(User user,String sourcePath, String targetPath); //复制文件
    ResponseResult move(User user,String sourcePath, String targetPath); //移动文件
    ResponseResult rename(User user,String oldName,String newName); //重命名文件或文件夹
    ResponseResult delete(User user,String delPath); //删除文件或文件夹
    ResponseResult upload(User user,String localPath,String hdfsPath); //上传文件或文件夹
    ResponseResult download(User user,String hdfsFilePath,String localFilePath); //下载文件

    String readFile(String filePath); //查看文件内容
}
