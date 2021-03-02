package com.chen.discipline.love.day.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.chen.discipline.love.day.MyApplication;
import com.chen.discipline.love.day.greendao.DaoSession;
import com.chen.discipline.love.day.utils.UpdateAppWidget;

/**
 * @author Chenhong
 * @date 2020/3/19.
 * @des
 */
public class MyJobService extends JobService {
    private static final String TAG = "chen";

    public static void startJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(10, new ComponentName(context.getPackageName(), MyJobService.class.getName())).setPersisted(true);
        /**
         * I was having this problem and after review some blogs and the official documentation,
         * I realised that JobScheduler is having difference behavior on Android N(24 and 25).
         * JobScheduler works with a minimum periodic of 15 mins.
         *
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上延迟1s执行
            builder.setMinimumLatency(6 * 60 * 60 * 1000);
        } else {
            //每隔1s执行一次job
            builder.setPeriodic(6 * 60 * 60 * 1000);
        }
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "---job service running---");
        DaoSession daoSession = MyApplication.getInstance().getDaoSession();
        UpdateAppWidget.INSTANCE.updateAppWidgetSS(this, daoSession, daoSession.getPersonMessageDao());
        //7.0以上轮询
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startJob(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
