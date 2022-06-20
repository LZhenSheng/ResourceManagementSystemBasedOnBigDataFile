package com.suicuntong.sct.service;

import com.suicuntong.sct.entity.User;
import com.suicuntong.sct.vo.ResponseResult;

public interface UserService {
    //校验登录
    ResponseResult login(String phone, String password);
    //注册
    ResponseResult register(User user);
    //找回密码
    //ResponseResult findbackPassword(String phone);

    ResponseResult userCheck(String name,String phone);

    ResponseResult questionCheck(String phone, String question, String answer);

    ResponseResult passwordChange(String phone, String password);
}
