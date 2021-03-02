package com.chen.discipline.love.day.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * @author Chenhong
 * @date 2020/3/11.
 * @des
 */
public class CalculationDaysUtil {

    /**
     * 计算农历距离今天还有多少天
     */
    public static int getLunarCalculationDays(int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int[] solars = LunarCalendarUtils.lunarToSolar(thisYear, month, day, false);
        return getSolarCalculationDays(solars[1], solars[2]);
    }

    /**
     * 公历距离今天多少天
     * @param month
     * @param day
     * @return
     */
    public static int getSolarCalculationDays(int month, int day) {
        int allDay = 0;
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMoth = calendar.get(Calendar.MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (month < thisMoth + 1) {
            for (int i = thisMoth + 1; i < 12; i++) {
                //今年剩下月天数
                allDay += CalendarUtils.getMonthDays(thisYear, i);
            }
            for (int i = 0; i < month - 1; i++) {
                //明年天数
                allDay += CalendarUtils.getMonthDays(thisYear + 1, i);
            }
            allDay += CalendarUtils.getMonthDays(thisYear, thisMoth) - thisDay + day;
        } else if (month > thisMoth + 1){
            for (int i = thisMoth + 1; i < month - 1; i++) {
                allDay += CalendarUtils.getMonthDays(thisYear, i);
            }
            allDay += CalendarUtils.getMonthDays(thisYear, thisMoth) - thisDay + day;
        } else if (month == thisMoth + 1){
            if (day >= thisDay) {
                allDay = day - thisDay;
            } else {
                //已经过了
                for (int i = thisMoth + 1; i < 12; i++) {
                    //今年下月开始天数
                    allDay += CalendarUtils.getMonthDays(thisYear, i);
                }
                for (int i = 0; i < month - 1; i++) {
                    //明年天数
                    allDay += CalendarUtils.getMonthDays(thisYear + 1, i);
                }
                allDay += day;
            }
        }
        return allDay;
    }
}
