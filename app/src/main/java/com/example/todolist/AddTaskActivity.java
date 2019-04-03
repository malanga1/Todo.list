package com.example.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.utils.Task;
import com.example.todolist.utils.TaskLab;

public class AddTaskActivity extends AppCompatActivity {

    //Поле, хранит EditText с основным текстом задачи.
    private EditText mTaskTextEditText;
    //Поле, хранит EditText с дополнительным текстом задачи.
    private EditText mAdditionalTextEditText;
    //Поле, хранит кнопку, по нажатию на которую добавляется новое задание.
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //Связываем EditText-ы.
        mTaskTextEditText = findViewById(R.id.edit_task_text);
        mAdditionalTextEditText = findViewById(R.id.edit_task_additional);
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
                    //Завершаем активность.
                    finish();
                    //В противном случае поле пустое, предупреждаем пользователя.
                } else {
                    Toast.makeText(AddTaskActivity.this, "Task text cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
