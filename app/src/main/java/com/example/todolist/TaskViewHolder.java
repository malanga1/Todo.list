package com.example.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

//Класс, который содерэит в себе все компоненты разметки элемента списка, через него мы и получаем ссылки на них.
public class TaskViewHolder extends RecyclerView.ViewHolder {
    private static final int DELETE_ITEM_ID = 1;
    //Поле с основным текстом пользователя.
    private TextView userText;



    private LinearLayout itemBackground;

    public TaskViewHolder( View itemView) {
        super(itemView);
        //Связываем с элементром из разметки.
        userText = itemView.findViewById(R.id.task_text);
        itemBackground = itemView.findViewById(R.id.linear_layout);
    }

    public TextView getUserText() {
        return userText;
    }

    public LinearLayout getItemBackground() {
        return itemBackground;
    }





}
