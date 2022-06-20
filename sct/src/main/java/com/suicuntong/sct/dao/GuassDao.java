package com.suicuntong.sct.dao;

import com.suicuntong.sct.utils.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuassDao extends BaseDao{
    /**
     * 创建文件夹
     * @return
     * @throws SQLException
     */
    public static int createList(String phone,String name,String path) {
        Connection conn = getConnection();
        int result = 0;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO create_list VALUES(?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, path);
            ps.setString(3, phone);
            ps.setString(4, DateUtil.getDate());
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

    /***
     * 文件重命名
     * @param phone
     * @param oldName
     * @param newName
     */
    public static void renameFile(String phone, String oldName, String newName) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO rename_file VALUES(?, ?, ?, ?)");
            ps.setString(1, oldName);
            ps.setString(2, newName);
            ps.setString(3,phone);
            ps.setString(4, DateUtil.getDate());
            ps.executeUpdate();
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

    }

    /***
     * 文件夹重命名
     * @param phone
     * @param oldName
     * @param newName
     */
    public static void renameList(String phone, String oldName, String newName) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO rename_list VALUES(?, ?, ?, ?)");
            ps.setString(1, oldName);
            ps.setString(2, newName);
            ps.setString(3,phone);
            ps.setString(4, DateUtil.getDate());
            ps.executeUpdate();
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
    }

    /***
     * 上传文件
     * @param phone
     * @param name
     * @param len
     */
    public static void uploadFile(String phone, String name, double len) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO upload_file VALUES(?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setDouble(2, len);
            ps.setString(3,phone);
            ps.setString(4, DateUtil.getDate());
            ps.executeUpdate();
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
    }

    /***
     * 下载文件
     * @param phone
     * @param name
     * @param length
     */
    public static void downloadFile(String phone, String name, double length) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO download_file VALUES(?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setDouble(2, length);
            ps.setString(3,phone);
            ps.setString(4, DateUtil.getDate());
            ps.executeUpdate();
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
    }

    /***
     * 删除文件
     * @param phone
     * @param name
     * @param len
     */
    public static void deleteFile(String phone, String name, double len) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO delete_file VALUES(?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setDouble(2, len);
            ps.setString(3,phone);
            ps.setString(4, DateUtil.getDate());
            ps.executeUpdate();
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
    }

    /***
     * 删除文件夹
     * @param phone
     * @param name
     */
    public static void deleteList(String phone, String name) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO delete_folder VALUES(?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2,phone);
            ps.setString(3, DateUtil.getDate());
            ps.executeUpdate();
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
    }

    /***
     * 删除文件夹
     * @param phone
     */
    public static void copyFile(String phone, String sourcePath,String targetPath,double len) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO copy_file VALUES(?,?,?,?,?)");
            ps.setString(1, sourcePath);
            ps.setString(2,targetPath);
            ps.setString(4,DateUtil.getDate());
            ps.setString(3, phone);
            ps.setDouble(5,len);
            ps.executeUpdate();
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
    }

    public static void moveFile(String phone, String prePath, String resPath, double len) {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO remove_file VALUES(?,?,?,?,?)");
            ps.setString(1, prePath);
            ps.setString(2,resPath);
            ps.setString(4,DateUtil.getDate());
            ps.setString(3, phone);
            ps.setDouble(5,len);
            ps.executeUpdate();
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
    }
}
