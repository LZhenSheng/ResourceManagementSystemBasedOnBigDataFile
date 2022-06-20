package com.suicuntong.sct.service;

import com.suicuntong.sct.entity.File;

import java.util.List;
import java.util.Map;

public interface ESService {

   void insert(String filePath);

   List<Map<String,Object>> match(String search_query, String search_type, String file_type, String sort_type);
}
