package com.suicuntong.sct.controller;

import com.suicuntong.sct.entity.User;
import com.suicuntong.sct.service.ESService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sct")
public class ESController {
    @Resource
    private ESService esService;

    private User user;

    /**
     * ES界面
     */
    @RequestMapping("/es")
    public String ESPage(@RequestParam(value="search_query",defaultValue = "") String search_query,
                         @RequestParam(value="search_type",defaultValue = "FILE_NAME_SEARCH") String search_type,
                         @RequestParam(value="file_type",defaultValue = "ALL_TYPE_FILE") String file_type,
                         @RequestParam(value="sort_type",defaultValue = "SORT_BY_MATCH") String sort_type,
                         @RequestParam(value="id") String id,
                         Model model) {
        this.user = new User(id);
        System.out.println(user.toString());
        List<Map<String,Object>> filelist = new ArrayList<>();
        filelist=esService.match(search_query, search_type, file_type, sort_type);
        System.out.println(filelist);
        model.addAttribute("id",id);
        model.addAttribute("fileList",filelist);

        System.out.println(model.toString());
        return "/sct/ESsearch";
    }



}
