package com.example.todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

//Адаптер, вспомогательный класс, который ставит заданый список в RecyclerView.
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    //Список, который должен быть представлен в RecyclerView.
    private List<Task> mTaskList;
    //Контекст.
    private Context mContext;

    public TaskAdapter(List<Task> tasks, Context context){
        mTaskList = tasks;
        mContext = context;
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
        return new TaskViewHolder(v, mContext);
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

    //Класс, который содерэит в себе все компоненты разметки элемента списка, через него мы и получаем ссылки на них.
     class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private static final int DELETE_ITEM_ID = 1;

        //Поле с основным текстом пользователя.
        private TextView userText;

        //Корневой элемент разметки, который мы прослушиваем на нажатия.
        private LinearLayout itemBackground;


        //Конструктор, котороый создает TaskViewHolder.
        public TaskViewHolder( View itemView, Context context) {
            super(itemView);
            //Связываем с элементром из разметки.
            userText = itemView.findViewById(R.id.edit_task_text);
            itemBackground = itemView.findViewById(R.id.linear_layout);
            mContext = context;
            itemView.setOnCreateContextMenuListener(this);
        }

        //Возвращает TextView с основным текстом пользователя.
        public TextView getUserText() {
            return userText;
        }

        //Возвращает корневой элемент.
        public LinearLayout getItemBackground() {
            return itemBackground;
        }

        //Создаётся всплывающее меню при долгом нажатии.
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //Получаем ID задания, которое отображает в данное время элемент.
            final UUID id = (UUID) v.getTag();
            //Добавляем строку списка с меню и ставим слушатель нажатия на этот элемент.
            menu.add(0, DELETE_ITEM_ID, 0, "Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //Получаем обьект хранилища.
                    TaskLab taskLab = TaskLab.get(mContext);
                    //Получаем конкретное задания из хранилища.
                    Task task = taskLab.getTask(id);
                    //Узнаём позицию этого задания в списке.
                    int position = mTaskList.indexOf(task);
                    //Удаляем из списка этот элемент, затем оповещаем Адаптер о том, что мы удалили этот элемент,
                    //это всё делается для анимации удаления.
                    mTaskList.remove(position);
                    notifyItemRemoved(position);
                    //Удаляем из БД это задание.
                    taskLab.deleteTask(id);
                    return true;
                }
            });

        }

    }

}
