package com.example.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

//Класс, который содерэит в себе все компоненты разметки элемента списка, через него мы и получаем ссылки на них.
public class TaskViewHolder extends RecyclerView.ViewHolder {
    //Поле с основным текстом пользователя.
    private TextView userText;

    public TaskViewHolder( View itemView) {
        super(itemView);
        //Связываем с элементром из разметки.
        userText = itemView.findViewById(R.id.task_text);
    }

    public TextView getUserText() {
        return userText;
    }
}
