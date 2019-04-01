package com.example.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.todolist.utils.Task;
import com.example.todolist.utils.TaskAdapter;
import com.example.todolist.utils.TaskLab;

import java.util.UUID;

public class TaskListActivity extends AppCompatActivity {

    //Ссылка на RecyclerView.
    RecyclerView mRecyclerView;
    //Ссылка на Адаптер.
    TaskAdapter mTaskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        //Связываем с макетом.
        mRecyclerView = findViewById(R.id.task_recycler_view);
        //LayoutManager отвечает за позиционирование view-компонентов в RecyclerView.
        //Этот класс отвечает за работу с адаптером, именно он решает, переиспользовать View или создать новый.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Определяем адаптер, передавая ему список заданий из хранилища.
        mTaskAdapter = new TaskAdapter(TaskLab.get(this).getTaskList(), this);
        //Устанавливаем адаптер.
        mRecyclerView.setAdapter(mTaskAdapter);

    }

    //Обновляем данные в адаптере, по возращению в основную активность.
    @Override
    protected void onResume() {
        super.onResume();
        mTaskAdapter.updateAdapterData(TaskLab.get(this).getTaskList());
    }

    //Метод, с помощью которого создаётся контекстное меню.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        //Устанавливаем файл макета меню.
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    //Вызывается при нажатии одного из элементов меню.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Определяем по айди элемента, какой был нажат.
        switch (item.getItemId()) {
            //Если это +, запускаем активность для добавления новых заданий.
            case R.id.icon_add:
                startActivity(new Intent(this, AddTaskActivity.class));
                return true;
            case R.id.item_delete_all:
                TaskLab.get(this).deleteAll();
                mTaskAdapter.updateAdapterData(TaskLab.get(this).getTaskList());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Метод, который вызывается при нажатии на элемент списка.
    public void onItemClick(View view) {
        //Получаем айди задания, к которому относится данный элемент списка.
        UUID id = (UUID) view.getTag();
        //Получаем конкретное задание по Айди.
        Task task = TaskLab.get(this).getTask(id);
        //Создаём Intent для запуска EditTaskActivity.
        Intent intent = new Intent(TaskListActivity.this, EditTaskActivity.class);
        //Кладем дополнительную информацию в виде задания, которое нужно отредактировать.
        intent.putExtra("task", task);
        //Запускаем EditTaskActivity.
        startActivity(intent);

    }


}
