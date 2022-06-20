package com.suicuntong.sct.service.impl;


import com.suicuntong.sct.dao.HdfsDao;
import com.suicuntong.sct.dao.UserDao;
import com.suicuntong.sct.entity.User;
import com.suicuntong.sct.service.UserService;
import com.suicuntong.sct.utils.StringUtil;
import com.suicuntong.sct.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    //@Autowired
    private UserDao userDao;

    @Autowired
    private HdfsDao hdfsDao;

    @Override
    public ResponseResult login(String phone, String password) {
        //从数据库中查询用户是否存在
        ResponseResult result = new ResponseResult();
        if(StringUtil.checkLoginFormat(phone,password)){
            if (userDao.findUser(phone,password)){
                System.out.println("cg:"+phone);
                result.setCode("201");//验证成功
                result.setMessage("0");
            } else {
                result.setCode("202");//用户名或密码错误
                result.setMessage("用户名或密码错误！");
                System.out.println("sb:"+phone);
            }
        } else {
            result.setCode("202");//用户名或密码错误
            result.setMessage("用户名或密码错误！");
            System.out.println("sb:"+phone);
        }
        return result;
    }

    @Override
    public ResponseResult register(User user) {
        ResponseResult result = new ResponseResult();
       if(userDao.createUser(user)==1){
           try {
               hdfsDao.mkdir(user.getPhone());
           } catch (Exception e) {
               e.printStackTrace();
           }
           result.setCode("301");//注册成功
           result.setMessage("0");
       } else {
           result.setCode("302");//注册失败
           result.setMessage("注册失败，该手机号已被注册");
       }
        return result;
    }

/*    @Override
    public ResponseResult findbackPassword(String phone) {
        ResponseResult result = new ResponseResult();
        if(StringUtil.isPhone(phone)){
            //userDao.findbackPassword(phone);
        } else {
            result.setCode("402");
            result.setMessage("手机号格式错误，请输入正确的手机号");
        }
        return result;
    }*/

    @Override
    public ResponseResult userCheck(String name,String phone) {
        ResponseResult result = new ResponseResult();
        if(StringUtil.isPhone(phone)){
            if (userDao.userCheck(name,phone)){
                System.out.println("401");
                result.setCode("401");
                result.setMessage("验证成功");
            } else {
                System.out.println("402");
                result.setCode("402");
                result.setMessage("验证失败");
            }
        } else {
            System.out.println("403");
            result.setCode("402");
            result.setMessage("验证失败");
        }
        return result;
    }

    @Override
    public ResponseResult questionCheck( String phone,String question,String answer) {
        ResponseResult result = new ResponseResult();
        if( userDao.questionCheck(phone, question, answer)){
            result.setCode("501");
            System.out.println("501");
            result.setMessage("验证成功");
        } else {
            System.out.println("502");
            result.setCode("502");
            result.setMessage("验证失败");
        }
        return result;
    }

    @Override
    public ResponseResult passwordChange(String phone, String password) {
        ResponseResult result = new ResponseResult();
        if( userDao.passwordChange(phone, password) == 1){
            result.setCode("601");
            result.setMessage("修改成功");
            System.out.println("601");
        } else {
            result.setCode("602");
            System.out.println("602");
            result.setMessage("修改失败");
        }
        return result;
    }

}
