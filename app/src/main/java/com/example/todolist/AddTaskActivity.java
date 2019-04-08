package com.example.todolist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.todolist.utils.AlarmReceiver;
import com.example.todolist.utils.Task;
import com.example.todolist.utils.TaskLab;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    //Поле, хранит EditText с основным текстом задачи.
    private EditText mTaskTextEditText;
    //Поле, хранит EditText с дополнительным текстом задачи.
    private EditText mAdditionalTextEditText;
    //Переключатель, который показывает, нужно ли пользователю уведомление.
    private Switch mNeedAlarmSwitch;
    //Поле, хранит кнопку, по нажатию на которую добавляется новое задание.
    private Button mAddButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //Связываем EditText-ы и Switch.
        mTaskTextEditText = findViewById(R.id.edit_task_text);
        mAdditionalTextEditText = findViewById(R.id.edit_task_additional);
        mNeedAlarmSwitch = findViewById(R.id.need_alarm_switch);
        //Связываем Button.
        mAddButton = findViewById(R.id.create_task_button);


        //Настраиваем OnClickListener для кнопки.
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем тект из EditText-ов.
                String text = mTaskTextEditText.getText().toString().trim();
                String addText = mAdditionalTextEditText.getText().toString().trim();
                //Если поле не пустое, добавляем новое задание.
                if(!text.equals("")) {
                    //Создаем новое задание и ставим текст, который указал пользователь.
                    Task task = new Task();
                    task.setText(text);
                    if(!addText.equals("")){
                        task.setAdditionalText(addText);
                    } else task.setAdditionalText("");
                    //Получаем экземпляр хранилища и добавляем в БД новое задание.
                    TaskLab taskLab = TaskLab.get(AddTaskActivity.this);
                    taskLab.addTask(task);

                    //Проверяем Switch, и если он включен, то устанавливаем уведомление.
                    if(mNeedAlarmSwitch.isChecked()){
                        setAlarm(task);
                    }

                    //Завершаем активность.
                    finish();
                    //В противном случае поле пустое, предупреждаем пользователя.
                } else {
                    Toast.makeText(AddTaskActivity.this, "Task text cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //Метод, который устанавливает оповещение.
    private void setAlarm(Task task){
        //Получаем экземпляр AlarmManager.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Создаем Intent и передаём в него задание о котором нужно напомнить.
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("task_ID", task.getId().toString());
        //Оборачиваем Intent в PendingIntent (обертка, которая позволяет стороннему приложению выполнять определенный код, в данный момент это AlarmReceiver).
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        //Получаем эекземпляр календаря.
        Calendar calendar = Calendar.getInstance();
        //Устанавливаем в него текущее время.
        calendar.setTimeInMillis(System.currentTimeMillis());
        //Устанавливаем время, нужное нам(время, когда должно прийти оповещение).
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 11);
        //Устанавливаем оповещение. В указанное премя по PendingIntent запустится AlarmReceiver.
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }



}
