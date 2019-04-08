package com.example.todolist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
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
    //Поле, хранит EditText с временем оповещения.
    private EditText mTimeEditText;
    //Переключатель, который показывает, нужно ли пользователю уведомление.
    private Switch mNeedAlarmSwitch;
    //Поле, хранит кнопку, по нажатию на которую добавляется новое задание.
    private Button mAddButton;

    private TimePickerDialog mTimePickerDialog;

    Calendar mCurrentTimeCalendar;

    Calendar mNotificationTimeCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mCurrentTimeCalendar = Calendar.getInstance();
        mNotificationTimeCalendar = Calendar.getInstance();

        //Связываем EditText-ы и Switch.
        mTaskTextEditText = findViewById(R.id.edit_task_text);
        mAdditionalTextEditText = findViewById(R.id.edit_task_additional);
        mTimeEditText = findViewById(R.id.time_edit_text);
        setEnableTime(false);
        mNeedAlarmSwitch = findViewById(R.id.need_alarm_switch);
        //Связываем Button.
        mAddButton = findViewById(R.id.create_task_button);

        mNeedAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEnableTime(isChecked);
            }
        });

        mTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mTimeEditText.setText(String.format("%02d:%02d", hourOfDay, minute));
                        mNotificationTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mNotificationTimeCalendar.set(Calendar.MINUTE, minute);
                    }
                }, mCurrentTimeCalendar.get(Calendar.HOUR_OF_DAY), mCurrentTimeCalendar.get(Calendar.MINUTE),true);
                mTimePickerDialog.show();
            }
        });




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
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, task.hashCode(), intent, 0);
        //Устанавливаем оповещение. В указанное премя по PendingIntent запустится AlarmReceiver.
        alarmManager.set(AlarmManager.RTC_WAKEUP, mNotificationTimeCalendar.getTimeInMillis(), alarmIntent);
    }

    private void setEnableTime(boolean needEnable){


        if(needEnable){
            mTimeEditText.setEnabled(true);
            mTimeEditText.setFocusable(true);
            mTimeEditText.setText(String.format("%02d:%02d", mCurrentTimeCalendar.get(Calendar.HOUR_OF_DAY), mCurrentTimeCalendar.get(Calendar.MINUTE)));
        }
        else {
            mTimeEditText.setHint(String.format("%02d:%02d", mCurrentTimeCalendar.get(Calendar.HOUR_OF_DAY), mCurrentTimeCalendar.get(Calendar.MINUTE)));
            mTimeEditText.setEnabled(false);
            mTimeEditText.setInputType(InputType.TYPE_NULL);
            mTimeEditText.setFocusable(false);
        }
    }


}
