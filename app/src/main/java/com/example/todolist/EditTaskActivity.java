package com.example.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.utils.Task;
import com.example.todolist.utils.TaskLab;

public class EditTaskActivity extends AppCompatActivity {

    //Поле, хранит EditText с основным текстом задачи.
    private EditText mEditText;
    //Поле, хранит EditText с дополнительным текстом задачи.
    private EditText mEditAdditionalText;
    //Поле, хранит кнопку, по нажатию на которую подверждаем изменение задания.
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        //Связываем элементы разметки с полями.
        mEditText = findViewById(R.id.edit_task_text);
        mConfirmButton = findViewById(R.id.edit_task_button);
        mEditAdditionalText = findViewById(R.id.edit_task_additional);
        //Получаем задание, которое нужно изменить, посредством получения его из полученного Intent.
        final Task task =  (Task) getIntent().getSerializableExtra("task");
        //Устанавливаем в EditText текст этого задания.
        mEditText.setText(task.getText());
        //Если есть доп. текст, ставим его в поле.
        if(!task.getAdditionalText().equals("")) {
            mEditAdditionalText.setText(task.getAdditionalText());
        }
        //Ставим слушатель нажатий на кнопку.
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем новый текст из EditText-ов.
                String text = mEditText.getText().toString().trim();
                String addText = mEditAdditionalText.getText().toString().trim();
                //Если EditText не пустой, и если текст не совпадает с тем, что был ранее.
                if((!text.equals("") && !text.equals(task.getText()))|| !addText.equals(task.getAdditionalText()) ) {
                    //Устанавливаем обновленный текст в задание.
                    task.setText(text);
                    if(!addText.equals("")){
                        task.setAdditionalText(addText);
                    }
                    //Через хранилище обновляем задание в БД и завершаем активность.
                    TaskLab.get(EditTaskActivity.this).updateTask(task);
                    finish();
                 //Если же текст не изменился, просто завершаем активность.
                } else if(text.equals(task.getText())){
                    finish();
                //В противном случае - EditText пустой, и нужно оповестить пользователя.
                } else {
                    Toast.makeText(EditTaskActivity.this, "Task text cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
