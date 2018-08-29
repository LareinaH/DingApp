package com.admin.ac.ding.utils;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyDateUtils {
    public static Boolean isSameMonth(Date date1, Date date2) {
        //Create 2 intances of calendar
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        //set the given date in one of the instance and current date in another
        cal1.setTime(date1);
        cal2.setTime(date2);

        //now compare the dates using functions
        if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
                // the date falls in current month
                return true;
            }
        }

        return false;
    }

    public static List<String> getDateRange(LocalDate start, LocalDate end) {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> ret = new ArrayList<>();
        LocalDate tmp = start;
        while(tmp.isBefore(end) || tmp.equals(end)) {
            ret.add(tmp.format(formatter));
            tmp = tmp.plusDays(1);
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(getDateRange(LocalDate.parse("2018-08-19"), LocalDate.parse("2018-09-19")));
    }
}
