package com.example.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends AppCompatActivity {

    //Поле, хранит EditText с основным текстом задачи.
    private EditText mTaskTextEditText;
    //Поле, хранит кнопку, по нажатию на которую добавляется новое задание.
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //Связываем EditText.
        mTaskTextEditText = findViewById(R.id.task_text);
        //Связываем Button.
        mAddButton = findViewById(R.id.create_task_button);
        //Настраиваем OnClickListener для кнопки.
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setText(mTaskTextEditText.getText().toString());
                TaskLab taskLab = TaskLab.get();
                taskLab.addTask(task);
                finish();
            }
        });
    }
}
