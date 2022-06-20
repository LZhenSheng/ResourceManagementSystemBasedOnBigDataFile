package com.suicuntong.sct.utils;


import static com.suicuntong.sct.utils.UserUtil.REGEX_PHONE;

/**
 * 字符串相关工具类
 */
public class StringUtil {

    /**
     * 是否是手机号
     *
     * @param value
     * @return
     */
    public static boolean isPhone(String value) {
        if(value==null)
            return false;
        return value.matches(REGEX_PHONE);
    }


    /**
     * 是否符合密码格式
     *
     * @param value
     * @return
     */
    public static boolean isPassword(String value) {
        if(value==null)
            return false;
        return value.length() >= 6 && value.length() <= 15;
    }

    /**
     * 是否符合昵称格式
     *
     * @param value
     * @return
     */
    public static boolean isNickname(String value) {
        return value.length() >= 2 && value.length() <= 10;
    }
    /***
     * 判断手机号和密码是否符合格式
     * @param account
     * @param password
     * @return
     */
    public static boolean isAccountPassword(String account,String password) {
        return isPhone(account)&&isPassword(password);
    }

    /***
     * 注册是否成功
     * @param account
     * @param password1
     * @param password2
     * @return
     */
    public static int checkRegisterFormat(String name,String account,String password1,String password2) {
        int result = -1;
        if(name!=null&&name.length()>=2&&name.length()<=10){
            if(StringUtil.isPhone(account)){
                if(StringUtil.isAccountPassword(account,password1)&&password1.equals(password2)){
                    result = 0;
                }else if(password1!=null&&password2!=null&&password1.equals(password2)){
                    result= 1; //密码长度为6-20位
                }else if(password1==null){
                    result= 2; //请输入密码
                }else{
                    result= 3; //请确认密码
                }
            }else{
                result= 4; //手机号格式错误，请输入正确的手机号
            }
        }else{
            result= 5; //用户名长度为2-10位
        }
        return result;
    }

    public static boolean checkLoginFormat(String phone,String password){
        boolean result=false;
        if(isPhone(phone)){
            if(isPassword(password)){
                result=true;
            }else{
                result=false;
            }
        }else{
            result=false;
        }
        return result;
    }
}
