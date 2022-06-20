package com.suicuntong.sct.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suicuntong.sct.dao.ESDao;
import com.suicuntong.sct.dao.HdfsDao;
import com.suicuntong.sct.entity.File;
import com.suicuntong.sct.service.ESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ESServiceImpl implements ESService {

    @Autowired
    private HdfsDao hdfsDao;

    @Autowired
    private ESDao esDao;

    @Override
    public void insert(String filePath) {
        try {
            Map<String,Object> fileMap = hdfsDao.getFileInfo(filePath);

            if(fileMap.get("fileName").toString().lastIndexOf( "." )< 0 ){

            }
            File file = new File();
            file.setName(fileMap.get("fileName").toString());
            file.setFileInfo();
            file.setSize((Long) fileMap.get("size"));
            file.setFilePath(fileMap.get("filePath").toString());
            file.setModificationTime(fileMap.get("modificationTime").toString());

            Map<String,Object> insertMap = new HashMap<String,Object>();
            insertMap.put("fileName",file.getName());
            insertMap.put("fileType",file.getFileExtension());
            insertMap.put("fileExtension",true);
            insertMap.put("fileSize",file.getSize());
            insertMap.put("fileModificationTime",file.getModificationTime());
            System.out.println(hdfsDao.readFileToString(filePath));
            insertMap.put("fileContent",(file.getType().equalsIgnoreCase("txt")) && (file.getSize() < (1024*1024*10)) ? hdfsDao.readFileToString(filePath) : "");
            insertMap.put("filePath",filePath);
            
            ObjectMapper mapper = new ObjectMapper();
            String docJson = mapper.writeValueAsString(insertMap);
            System.out.println("docJson="+docJson);
            esDao.insert(docJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> match(String search_query, String search_type, String file_type, String sort_type) {
        List<Map<String, Object>> filelist = new ArrayList<>();
        try {
            filelist = esDao.match(search_query,search_type,file_type,sort_type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filelist;
    }

}
