package com.chen.discipline.love.day.receiver

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.chen.discipline.love.day.MainActivity
import com.chen.discipline.love.day.MyApplication
import com.chen.discipline.love.day.R
import com.chen.discipline.love.day.service.LoveDayRunService
import com.chen.discipline.love.day.service.MyJobService
import com.chen.discipline.love.day.utils.UpdateAppWidget.updateAppWidgetSS

class TestWidgetProvider : AppWidgetProvider() {
    companion object {
        const val CLICK_ACTION = "com.chen.discipline.love.day.action.CLICK" // 点击事件的广播ACTION
    }

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        val intent = Intent(CLICK_ACTION)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            R.id.showContentLayout,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews.setOnClickPendingIntent(R.id.showContentLayout, pendingIntent)
        for (appWidgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
        Log.e("chen","-----onUpdate------")
        context.startService(Intent(context, LoveDayRunService::class.java))
        MyJobService.startJob(context);
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (CLICK_ACTION == intent.action) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    override fun onDeleted(
        context: Context,
        appWidgetIds: IntArray
    ) {
        super.onDeleted(context, appWidgetIds)
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        val daoSession = MyApplication.getInstance().daoSession
        updateAppWidgetSS(context, daoSession, daoSession.personMessageDao)
    }

    /**
     * 当小部件大小改变时
     */
    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    override fun onRestored(
        context: Context,
        oldWidgetIds: IntArray,
        newWidgetIds: IntArray
    ) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }
}