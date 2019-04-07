package com.example.todolist.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Task task = (Task) intent.getSerializableExtra("task");
        notify1(task, context);
    }

    public void notify1(Task task, Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "1",
                    "Main",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Main channel");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(context, "1")
                    .setContentTitle("Don't forget to..")
                    .setContentText(task.getText())
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true);

            notificationManager.notify(1, builder.build());

        } else {

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle("Don't forget to..")
                    .setContentText(task.getText())
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true);

            notificationManager.notify(1, builder.build());

        }


    }

}
