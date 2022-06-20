package com.suicuntong.sct.controller;

import com.alibaba.fastjson.JSONObject;
import com.suicuntong.sct.entity.User;
import com.suicuntong.sct.service.UserService;
import com.suicuntong.sct.utils.DateUtil;
import com.suicuntong.sct.utils.StringUtil;
import com.suicuntong.sct.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;
    /**
     * 登陆界面
     */
    @GetMapping("/login")
    public String LoginPage(){
        return "/home/login";
    }

    /**
     * 注册界面
     */
    @GetMapping("/register")
    public String registerPage() {
        return "/home/register_new";
    }

    /**
     * 404界面
     */
    @GetMapping("/404")
    public String errorPage() {
        return "/home/404";
    }

    /**
     * 忘记密码界面
     */
    @GetMapping("/retrivepwd")
    public String retrivepwdPage() {
        return "/home/retrivepwd_new";
    }

    /**
     * 请求登录
     */
    @ResponseBody
    @RequestMapping("/loginCheck")
    public ResponseResult login(@RequestBody User user, HttpSession session) {
        ResponseResult result;
        System.out.println(user.toString());
        result = userService.login(user.getPhone(), user.getPassword());
        if (result.getCode().equals("201")) {
            //session.setAttribute("user", user);
        }
        return result;
    }

    /**
     * 安全退出
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        //清除sessin
        session.invalidate();
        return "redirect:/home/login";
    }


    /**
     * 请求注册
     */
    @ResponseBody
    @RequestMapping("/registerCheck")
    public ResponseResult register(@RequestBody JSONObject jsonParam) {
        ResponseResult result = new ResponseResult();
        System.out.println(jsonParam.toString());

        String name = jsonParam.get("name").toString();
        String phone = jsonParam.get("phone").toString();
        String password = jsonParam.get("password").toString();
        String confirm = jsonParam.get("confirm").toString();
        String question = jsonParam.get("question").toString();
        String answer = jsonParam.get("answer").toString();

        switch (StringUtil.checkRegisterFormat(name,phone,password,confirm)){
            case 0:
                User user = new User(name, phone, password, DateUtil.getDate() ,question ,answer);
                System.out.println(user.toString());
                result = userService.register(user);
                break;
            case 1:
                result.setCode("303");
                result.setMessage("密码长度为6-20位");
                break;
            case 2:
                result.setCode("303");
                result.setMessage("请输入密码");
                break;
            case 3:
                result.setCode("303");
                result.setMessage("请确认密码");
                break;
            case 4:
                result.setCode("303");
                result.setMessage("手机号格式错误，请输入正确的手机号");
                break;
            case 5:
                result.setCode("303");
                result.setMessage("用户名长度为2-10位");
                break;
            default:
                result.setCode("303");
                result.setMessage("注册失败");
        }
        return result;
    }

    /**
     * 用户确认
     */
    @RequestMapping("/userCheck")
    @ResponseBody
    public ResponseResult userCheck(@RequestBody JSONObject jsonParam) {
        System.out.println(jsonParam.toString());
        ResponseResult result = new ResponseResult();
        String name = jsonParam.get("name").toString();
        String phone = jsonParam.get("phone").toString();
        if(StringUtil.isPhone(phone)){
            result = userService.userCheck(name,phone);
        }else {
            result.setCode("303");
            result.setMessage("验证失败");
        }
        return result;
    }

    /**
     * 密保问题确认
     */
    @ResponseBody
    @RequestMapping("/questionCheck")
    public ResponseResult questionCheck(@RequestBody JSONObject jsonParam) {
        ResponseResult result;
        System.out.println(jsonParam.toString());
        String phone = jsonParam.get("phone").toString();
        String question = jsonParam.get("question").toString();
        String answer = jsonParam.get("answer").toString();

        result = userService.questionCheck(phone,question,answer);


        return result;
    }

    /**
     * 修改密码
     */
    @ResponseBody
    @RequestMapping("/passwordChange")
    public ResponseResult passwordChange(@RequestBody JSONObject jsonParam) {
        ResponseResult result = new ResponseResult();
        System.out.println(jsonParam.toString());
        String phone = jsonParam.get("phone").toString();
        String password = jsonParam.get("password").toString();
        String confirm = jsonParam.get("confirm").toString();


        System.out.println(StringUtil.isPhone(phone));
        System.out.println(StringUtil.isPassword(password));
        System.out.println(password.equals(confirm));
        if(StringUtil.isPhone(phone) && StringUtil.isPassword(password) && password.equals(confirm)){
            result = userService.passwordChange(phone, password);
        }else {
            result.setCode("303");
            System.out.println("303-6");
            result.setMessage("验证失败");
        }
        System.out.println(result.toString());
        return result;
    }
}
