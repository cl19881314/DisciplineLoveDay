package com.chen.discipline.love.day.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.chen.discipline.love.day.MyApplication
import com.chen.discipline.love.day.R
import com.chen.discipline.love.day.greendao.PersonLoveDay
import com.chen.discipline.love.day.receiver.TestWidgetProvider
import com.chen.discipline.love.day.utils.CalculationDaysUtil
import com.chen.discipline.love.day.utils.UpdateAppWidget


/**
 * @author Chenhong
 * @date 2020/3/17.
 * @des
 */
class LoveDayRunService : Service() {
    private val SERVICE_ID = 111
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //判断版本
        if (Build.VERSION.SDK_INT < 18) {//Android4.3以下版本
            //将Service设置为前台服务，可以取消通知栏消息
            startForeground(SERVICE_ID, Notification());
        } else if (Build.VERSION.SDK_INT < 24) {//Android4.3 - 7.0之间
            //将Service设置为前台服务，可以取消通知栏消息
            startForeground(SERVICE_ID, Notification());
            startService(Intent(this, InnerService::class.java));
        } else if (Build.VERSION.SDK_INT >= 26) {//Android 8.0以上
            var manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            var channel = NotificationChannel("channel2222", "NAME2222", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            var notification = Notification.Builder(this, "channel2222")
                .setContentTitle("提示")
                .setContentText("温馨提示")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build();
            startForeground(SERVICE_ID, notification);
        }
        return START_STICKY;
    }

    override fun onCreate() {
        super.onCreate()
        updateData()
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

    private fun updateData() {
        val daoSession = MyApplication.getInstance().daoSession
        var personMessageDao = daoSession.personMessageDao
        Thread(Runnable {
            while (true) {
                Log.d("chen", "------Service is running----------")
                UpdateAppWidget.updateAppWidgetSS(this, daoSession, personMessageDao)
                SystemClock.sleep(6 * 60 * 60 * 1000)
            }
        }).start()
    }

    inner class InnerService : Service() {
        override fun onBind(intent: Intent?): IBinder? {
            return null
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            startForeground(SERVICE_ID, Notification())
            stopForeground(true) //移除通知栏消息
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }
    }
}