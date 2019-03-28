package com.example.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.List;

//Адаптер, вспомогательный класс, который ставит заданый список в RecyclerView.
public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    //Список, который должен быть представлен в RecyclerView.
    private List<Task> mTaskList;

    public TaskAdapter(List<Task> tasks){
        mTaskList = tasks;
    }

    public void updateAdapterData(List<Task> list){
        mTaskList = list;
        notifyDataSetChanged();
    }

    //Этот метод создает новый объект ViewHolder всякий раз, когда RecyclerView нуждается в этом.
    //Создаётся столько элементов, сколько помещаются на экран, остальные делаются из тех, что вышли за пределы экрана.
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //LayoutInflater это класс, который умеет из содержимого layout-файла создать View-элемент, с помощью метода inflate.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new TaskViewHolder(v);
    }

    //Этот метод принимает объект ViewHolder и устанавливает необходимые данные для соответствующей строки во view-компоненте.
    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int position) {
        //Получаем экземпляр задания для установки данных.
        Task task = mTaskList.get(position);
        //Забираем из TaskViewHolder TextView и устанавливаем туда данные из задания.
        taskViewHolder.getUserText().setText(task.getText());
        //Передаём уникальный айди задания, через setTag.
        taskViewHolder.getItemBackground().setTag(task.getId());
    }
    //Возвращает количество элементов.
    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

}
