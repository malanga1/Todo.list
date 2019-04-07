package com.example.todolist.utils;

import android.content.Context;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.todolist.R;

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
    public void onBindViewHolder(final TaskViewHolder taskViewHolder, final int position) {
        //Получаем экземпляр задания для установки данных.
        final Task task = mTaskList.get(position);
        //Забираем из TaskViewHolder TextView и устанавливаем туда данные из задания.
        final TextView taskText = taskViewHolder.getUserText();
        final TextView additionalText = taskViewHolder.getAdditionalText();
        taskText.setText(task.getText());
        //Проверка на дополнительный текст.
        String addText = task.getAdditionalText();
        //Если текст есть, показываем этот TextView и ставим туда текст, в противном случае убираем этот TextView.
        if(!addText.equals("")){
            additionalText.setVisibility(View.VISIBLE);
            additionalText.setText(addText);
        } else {
            additionalText.setVisibility(View.GONE);
        }
        //Передаём уникальный айди задания, через setTag.
        taskViewHolder.getItemBackground().setTag(task.getId());
        //Забираем из TaskViewHolder CheckBox.
        CheckBox isDoneBox = taskViewHolder.getIsDoneCheckBox();
        isDoneBox.setOnCheckedChangeListener(null);
        //Если на задани стоит флаг выполнения, отмечаем CheckBox.
        if(task.isDone()){
            isDoneBox.setChecked(true);
            //Делаем текст зачеркнутым.
            taskText.setPaintFlags(taskText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            additionalText.setPaintFlags(additionalText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        //В проивном случае нужно бязательно установить false. чтобы при переиспользовании он не оставался true.
        else {
            isDoneBox.setChecked(false);
            //Удаляем зачеркивание текста.
            taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            additionalText.setPaintFlags(additionalText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        //Устанавливаем слушателя изменения состояния CheckBox.
        isDoneBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Делаем текст зачеркнутым.
                    taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    additionalText.setPaintFlags(additionalText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    //Помечаем задание как сделанное, и обновляем его в БД.
                    task.setDone(true);
                    TaskLab.get(mContext).updateTask(task);
                    notifyItemChanged(taskViewHolder.getAdapterPosition());
                }
                else {
                    //Удаляем зачеркиване текста.
                    taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    additionalText.setPaintFlags(additionalText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    //Помеячаем задание как не сделанное и обновляем его в БД.
                    task.setDone(false);
                    TaskLab.get(mContext).updateTask(task);
                    notifyItemChanged(taskViewHolder.getAdapterPosition());
                }
            }
        });
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

        //Поле с дополнительным текстом пользователя.
        private TextView additionalText;

        //Корневой элемент разметки, который мы прослушиваем на нажатия.
        private ConstraintLayout itemBackground;

        //CheckBox, показывающий, сделано ли задание.
        private CheckBox isDoneCheckBox;


        //Конструктор, котороый создает TaskViewHolder.
        public TaskViewHolder( View itemView, Context context) {
            super(itemView);
            //Связываем с элементром из разметки.
            userText = itemView.findViewById(R.id.edit_task_text);
            additionalText = itemView.findViewById(R.id.additional_text);
            itemBackground = itemView.findViewById(R.id.linear_layout);
            isDoneCheckBox = itemView.findViewById(R.id.is_done_check_box);
            mContext = context;
            itemView.setOnCreateContextMenuListener(this);
        }

        //Возвращает TextView с основным текстом пользователя.
        public TextView getUserText() {
            return userText;
        }

        //Возвращает корневой элемент.
        public ConstraintLayout getItemBackground() {
            return itemBackground;
        }

        //Возвращает CheckBox.
        public CheckBox getIsDoneCheckBox(){ return isDoneCheckBox;}

        //Возвращает TextView с дополнительным текстом пользователя.
        public TextView getAdditionalText() {
            return additionalText;
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
