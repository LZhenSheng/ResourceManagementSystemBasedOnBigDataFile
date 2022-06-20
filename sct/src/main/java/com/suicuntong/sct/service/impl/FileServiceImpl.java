package com.suicuntong.sct.service.impl;

import com.suicuntong.sct.dao.ESDao;
import com.suicuntong.sct.dao.GuassDao;
import com.suicuntong.sct.dao.HdfsDao;
import com.suicuntong.sct.entity.File;
import com.suicuntong.sct.entity.User;
import com.suicuntong.sct.service.FileService;
import com.suicuntong.sct.utils.FileUtil;
import com.suicuntong.sct.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private GuassDao guassDao;

    @Autowired
    private ESDao esDao;

    @Autowired
    private HdfsDao hdfsDao;

    @Override
    public List<File> displayAllList(String path) {
        List<File> lists = new ArrayList<>();
        try {
            List<Map<String,Object>> fileList = hdfsDao.listFiles(path);
            List<File> finalLists = lists;
            fileList.forEach(fileMap -> {
                File file = new File();
                file.setName(fileMap.get("fileName").toString());
                file.setFileInfo();
                file.setSize((Long) fileMap.get("size"));
                file.setFilePath(fileMap.get("filePath").toString());
                file.setModificationTime(fileMap.get("modificationTime").toString());
                finalLists.add(file);
            });
            if (lists.size() != 0) {
                lists = FileUtil.SortDirectory(finalLists);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    public ResponseResult createFile(User user, String fileName, String path) {
        ResponseResult result = new ResponseResult();
        try {
            if (hdfsDao.createFile(path.equals("/") ? path + fileName : path + "/" + fileName, "")){
                result.setCode("101");//成功
                result.setMessage("0");
            } else {
                result.setCode("102");//失败
                result.setMessage("失败");
            }
        } catch (Exception e) {
            result.setCode("103");//失败
            result.setMessage("失败");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResponseResult createDir(User user, String dirName, String path) {
        ResponseResult result = new ResponseResult();
        try {
            String dirPath = path.equals("/") ? path + dirName : path + "/" + dirName;
            if (hdfsDao.mkdir(dirPath)){
                guassDao.createList(user.getPhone(),user.getName(),dirPath);
                result.setCode("201");//创建成功
                result.setMessage("0");
            } else {
                result.setCode("202");//创建失败
                result.setMessage("新建文件夹"+dirName+"失败");
            }
        } catch (Exception e) {
            result.setCode("203");//创建失败
            result.setMessage("新建文件夹"+dirName+"失败");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResponseResult copy(User user, String sourcePath, String targetPath) {
        ResponseResult result = new ResponseResult();
        try {
            if(hdfsDao.copyFile(sourcePath,targetPath)){
                guassDao.copyFile(user.getPhone(),sourcePath,targetPath, 0);
                result.setCode("301");//成功
                result.setMessage("0");
            } else {
                result.setCode("302");//失败
                result.setMessage("失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("303");//失败
            result.setMessage("失败");
        }

        return result;
    }

    @Override
    public ResponseResult move(User user, String sourcePath, String targetPath) {
        ResponseResult result = new ResponseResult();
        try {
            if (hdfsDao.moveFile(sourcePath,targetPath)){
                guassDao.moveFile(user.getPhone(),sourcePath,targetPath,0);
                result.setCode("401");//成功
                result.setMessage("0");
            } else {
                result.setCode("402");//成功
                result.setMessage("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("403");//成功
            result.setMessage("0");
        }
        return result;
    }

    @Override
    public ResponseResult rename(User user, String oldName, String newName) {
        ResponseResult result = new ResponseResult();
        try {
            if (hdfsDao.renameFile(oldName,newName)){
                guassDao.renameFile(user.getPhone(),oldName,newName);
                result.setCode("501");//成功
                result.setMessage("0");
            } else {
                result.setCode("502");//失败
                result.setMessage("失败");
            }
        } catch (Exception e) {
            result.setCode("503");//失败
            result.setMessage("失败");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResponseResult delete(User user, String delPath) {
        ResponseResult result = new ResponseResult();
        try {
            if (hdfsDao.deleteFile(delPath)){
                guassDao.deleteFile(user.getPhone(),delPath,0);
                result.setCode("601");//成功
                result.setMessage("0");
            } else {
                result.setCode("602");//失败
                result.setMessage("失败");
            }
        } catch (Exception e) {
            result.setCode("603");//失败
            result.setMessage("失败");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResponseResult upload(User user, String localPath, String hdfsPath) {
        ResponseResult result = new ResponseResult();
        try {
            if (hdfsDao.uploadFile(localPath,hdfsPath)){
                String uploadPath = hdfsPath + FileUtil.getFileNameFromPath(localPath);
                guassDao.uploadFile(user.getPhone(),hdfsPath,0);
                result.setCode("701");//成功
                result.setMessage("0");
            } else {
                result.setCode("702");//失败
                result.setMessage("失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("703");//失败
            result.setMessage("失败");
        }
        return result;
    }

    @Override
    public ResponseResult download(User user, String hdfsFilePath, String localFilePath) {
        ResponseResult result = new ResponseResult();
        try {
            if (hdfsDao.downloadFile(hdfsFilePath,localFilePath)){
                guassDao.downloadFile(user.getPhone(),hdfsFilePath,0);
                result.setCode("801");//成功
                result.setMessage("0");
            } else {
                result.setCode("802");//成功
                result.setMessage("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("803");//成功
            result.setMessage("0");
        }
        return result;
    }

    @Override
    public String readFile(String filePath) {
        String result="";
        try {
            result = hdfsDao.readFileToString(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
