package com.example.todolist.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.todolist.R;

import java.util.UUID;

public class AlarmReceiver extends BroadcastReceiver {

    private String NOTIFICATION_CHANNEL_ID = "1";
    private String CHANNEL_NAME = "Alarm";
    private NotificationManager mManager;

    //Метод, который вызывается, когда прилетает Intent.
    @Override
    public void onReceive(Context context, Intent intent) {
        //Получаем экземпляр хранилища, чтобы взять оттуда нужное задание.
        TaskLab taskLab = TaskLab.get(context);
        //Получаем конкретное задание из хранилища, используя ID, который был передан вместе с Intent.
        Task task =  taskLab.getTask(UUID.fromString(intent.getStringExtra("task_ID")));
        //Показываем уведомление этим методом.
        showNotification(task, context);
    }

    //Вспомогательный метод, в котором настраиваются и отсылаются уведомления.
    private void showNotification(Task task, Context context){
        //Получаем экземпляр NotificationManager (Управляет уведомлениями).
        NotificationManager notificationManager = getManager(context);
        //Если у пользователя стоит Андроид О или выше, обязательно нужно создать канал уведомлений.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Создаем канал уведомлений. Устанавливаем ID, имя, и важность(Уведомление должно вспылвать - важность самая высокая).
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        //Получаем экземпляр строителя уведомлений.
        Notification.Builder builder = getBuilder(context, task.getText());
        //Отсылаем уведомление этим методом.
        notificationManager.notify(task.hashCode(), builder.build());

    }

    //Вспомогательный метод для построения уведомления.
    private Notification.Builder getBuilder(Context context, String userText){
        //Определяем строителя.
        Notification.Builder builder;
        //Если у пользователя Андроид О или выше, для инициализации пользуемся конструктором с указанием ID канала.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID);
        //Если версия ниже - жто не нужно.
        } else {
            builder = new Notification.Builder(context);
        }
        //Построение уведомления. Заголовок, текст, приоритет, иконкка.
        builder.setContentTitle("Don't forget to...")
                .setContentText(userText)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true);
        return builder;
    }

    //Вспомогательный метод, который возвращает экземпляр NotificationManager.
    private NotificationManager getManager(Context context) {
        if (mManager == null) {
            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}
