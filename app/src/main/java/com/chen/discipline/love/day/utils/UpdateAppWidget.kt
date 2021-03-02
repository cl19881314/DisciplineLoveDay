package com.chen.discipline.love.day.utils

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import com.chen.discipline.love.day.R
import com.chen.discipline.love.day.greendao.DaoSession
import com.chen.discipline.love.day.greendao.PersonLoveDay
import com.chen.discipline.love.day.greendao.PersonMessageDao
import com.chen.discipline.love.day.receiver.TestWidgetProvider

/**
 * @author Chenhong
 * @date 2020/3/19.
 * @des
 */
object UpdateAppWidget {
    fun updateAppWidgetSS(context: Context,daoSession : DaoSession, personMessageDao : PersonMessageDao){
        var loveDay1: PersonLoveDay? = null
        var loveDay2: PersonLoveDay? = null
        var index1 = 366
        var index2 = 366
        val loadAll = daoSession.personLoveDayDao.loadAll()
        for (i in loadAll.indices) {
            val split = loadAll[i].day.split("月")
            var indexRes = if (loadAll[i].dayType == "0") {
                CalculationDaysUtil.getLunarCalculationDays(split[0].toInt(), split[1].toInt())
            } else {
                CalculationDaysUtil.getSolarCalculationDays(split[0].toInt(), split[1].toInt())
            }
            if (indexRes < index1) {
                loveDay1 = loadAll[i]
                index1 = indexRes
            }
            if (loadAll.size >= 2 && i + 1 < loadAll.size) {
                val split2 = loadAll[i + 1].day.split("月")
                var indexRes2 = if (loadAll[i + 1].dayType == "0") {
                    CalculationDaysUtil.getLunarCalculationDays(split2[0].toInt(), split2[1].toInt())
                } else {
                    CalculationDaysUtil.getSolarCalculationDays(split2[0].toInt(), split2[1].toInt())
                }
                if (indexRes2 < index2) {
                    loveDay2 = loadAll[i + 1]
                    index2 = indexRes2
                }
            }
            if (index1 > index2) {
                var temp = loveDay1
                loveDay1 = loveDay2
                loveDay2 = temp
                var tempIndex = index1
                index1 = index2
                index2 = tempIndex
            }
        }
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        if (loadAll.size >= 1) {
            remoteViews.setTextViewText(R.id.txtTypeName, loveDay1?.dayName)
            remoteViews.setTextViewText(R.id.txtDay, "${loveDay1?.day}日")
            remoteViews.setTextViewText(R.id.txtName, personMessageDao.load(loveDay1?.personId)?.name)
            remoteViews.setTextViewText(R.id.txtIndex, "距离今天还有${index1}天")
        }
        if (loadAll.size >= 2) {
            remoteViews.setTextViewText(R.id.txtTypeName2, loveDay2?.dayName)
            remoteViews.setTextViewText(R.id.txtDay2, "${loveDay2?.day}日")
            remoteViews.setTextViewText(R.id.txtName2, personMessageDao.load(loveDay2?.personId)?.name)
            remoteViews.setTextViewText(R.id.txtIndex2, "距离今天还有${index2}天")
        }
        val thisName = ComponentName(context, TestWidgetProvider::class.java)
        val manager = AppWidgetManager.getInstance(context)
        manager.updateAppWidget(thisName, remoteViews)
    }
}