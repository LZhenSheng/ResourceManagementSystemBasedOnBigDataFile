package com.suicuntong.sct.controller;

import com.alibaba.fastjson.JSONObject;
import com.suicuntong.sct.entity.File;
import com.suicuntong.sct.entity.User;
import com.suicuntong.sct.service.ESService;
import com.suicuntong.sct.service.FileService;
import com.suicuntong.sct.utils.FileUtil;
import com.suicuntong.sct.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sct")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private ESService esService;

    private User user;

    @RequestMapping("/index")
    public String list(@RequestParam(value="path") String path,
                       @RequestParam(value="id") String id,
                       Model model) {
        this.user = new User(id);
        System.out.println(user.toString());
        System.out.println("path="+path);
        List<File> list = fileService.displayAllList(path);
        List<Map<String,Object>> filelist = new ArrayList<>();
        for(File file:list){
            Map<String,Object> fileMap = new HashMap<>();
            fileMap.put("fileName",file.getName());
            fileMap.put("fileType",file.getType());
            if(file.getType().equals(file.getTypeFolder())){
                fileMap.put("size","-");
            } else {
                fileMap.put("size",FileUtil.getNetFileSizeDescription(file.getSize()));
            }
            fileMap.put("modificationTime",file.getModificationTime());
            fileMap.put("filePath",file.getFilePath());
            filelist.add(fileMap);
        }
        model.addAttribute("path",path);
        model.addAttribute("id",id);
        model.addAttribute("fileList",filelist);
        System.out.println(model.toString());
        return "sct/index";
    }

    @ResponseBody
    @RequestMapping("/download")
    public ResponseResult download(@RequestBody JSONObject jsonParam){
        System.out.println(jsonParam.toString());
        ResponseResult result = new ResponseResult();
        String filepath = jsonParam.get("filepath").toString();
        String localpath = jsonParam.get("localpath").toString();
        result = fileService.download(user,filepath,localpath);
        return result;
    }

    @ResponseBody
    @RequestMapping("/upload")
    public ResponseResult upload(@RequestBody JSONObject jsonParam){
        System.out.println(jsonParam.toString());
        String filepath = jsonParam.get("filepath").toString();
        String localpath = jsonParam.get("localpath").toString();
        ResponseResult result = fileService.upload(user,localpath,filepath);
        String insertpath = filepath +"/"+ FileUtil.getFileNameFromPath(localpath);
        System.out.println(insertpath);
        esService.insert(insertpath);

        return result;
    }

    @ResponseBody
    @RequestMapping("/rename")
    public ResponseResult rename(@RequestBody JSONObject jsonParam){
        System.out.println(jsonParam.toString());
        ResponseResult result = new ResponseResult();
        String oldName = jsonParam.get("oldName").toString();
        String newName = jsonParam.get("newName").toString();

        result = fileService.rename(user,oldName,newName);

        return result;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public ResponseResult delete(@RequestBody JSONObject jsonParam){
        System.out.println(jsonParam.toString());
        ResponseResult result = new ResponseResult();
        String delPath = jsonParam.get("delPath").toString();
        result= fileService.delete(user,delPath);

        return result;
    }

    @ResponseBody
    @RequestMapping("/move")
    public ResponseResult move(@RequestBody JSONObject jsonParam){
        System.out.println(jsonParam.toString());
        ResponseResult result = new ResponseResult();
        String sourcePath = jsonParam.get("sourcePath").toString();
        String targetPath = jsonParam.get("targetPath").toString();
        result = fileService.move(user,sourcePath,targetPath);

        return result;
    }

    @ResponseBody
    @RequestMapping("/copy")
    public ResponseResult copy(@RequestBody JSONObject jsonParam){
        System.out.println(jsonParam.toString());
        ResponseResult result = new ResponseResult();
        String sourcePath = jsonParam.get("sourcePath").toString();
        String targetPath = jsonParam.get("targetPath").toString();
        result = fileService.copy(user,sourcePath,targetPath);

        return result;
    }

    @ResponseBody
    @RequestMapping("/createDir")
    public ResponseResult createDir(@RequestBody JSONObject jsonParam){
        ResponseResult result;
        System.out.println(jsonParam.toString());
        String dirName = jsonParam.get("dirName").toString();
        String path = jsonParam.get("path").toString();

        result = fileService.createDir(user,dirName,path);

        return result;
    }
}
