package com.suicuntong.sct.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
