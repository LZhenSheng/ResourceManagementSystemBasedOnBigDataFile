package com.suicuntong.sct.dao;


import com.suicuntong.sct.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends BaseDao {

    /**
     * 创建用户
     * @param user
     * @return
     * @throws SQLException
     */
    public static int createUser(User user) {
        Connection conn = getConnection();
        int result = 0;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO consumer VALUES(?, ?, ?,? ,?,?)");
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getDate());
            ps.setString(5, user.getQuestion());
            ps.setString(6, user.getAnswer());
            result = ps.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 修改用户
     * @param phone
     * @return
     * @throws SQLException
     */
    public static String findbackPassword(String phone) {
        Connection conn = getConnection();
        String result = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM consumer WHERE cs_phone = ?");
            ps.setString(1, phone);
            rs = ps.executeQuery();
            while(rs.next()) {
                result=rs.getString(2);
                break;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return  result;
    }

    /**
     * 查询用户
     * @return
     * @throws SQLException
     */
    public static boolean findUser(String phone,String password) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result=false;
        try {
            ps = conn.prepareStatement("SELECT * FROM consumer WHERE cs_phone = ? AND cs_password = ?");
            ps.setString(1,phone);
            ps.setString(2,password);
            rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getString(2).trim().equals(password)&&rs.getString(3).trim().equals(phone)){
                    result=true;
                    break;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        System.out.println(result+" "+phone+"  "+password);
        return result;
    }



    /**
     * 以用户名手机号查询用户
     * @return
     */
    public static boolean userCheck(String name,String phone) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result=false;
        try {
            ps = conn.prepareStatement("SELECT * FROM consumer WHERE cs_phone = ? AND cs_name = ?");
            ps.setString(1,phone);
            ps.setString(2,name);
            rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getString(1).trim().equals(name)&&rs.getString(3).trim().equals(phone)){
                    result=true;
                    break;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 以手机号密保查询用户
     * @return
     */
    public static boolean questionCheck( String phone,String question,String answer) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result=false;
        try {
            ps = conn.prepareStatement("SELECT * FROM consumer WHERE cs_phone = ? AND cs_question_type=? AND cs_question_answer=?");
            ps.setString(1,phone);
            ps.setString(2,question);
            ps.setString(3,answer);
            rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getString(3).trim().equals(phone)&&
                        rs.getString(5).trim().equals(question)&&
                        rs.getString(6).trim().equals(answer)){
                    result=true;
                    break;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 以手机号修改密码
     * @param phone
     * @param password
     * @return
     */
    public static int passwordChange(String phone,String password) {

        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = 0;
        try {
            ps = conn.prepareStatement("UPDATE consumer SET cs_password=? WHERE cs_phone=?");
            ps.setString(1,password);
            ps.setString(2,phone);
            result = ps.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        return result;
    }
}