package com.example.todolist;

import java.util.ArrayList;
import java.util.List;

//Клас, отвечающий за хранение заданий и работу с ними(Singleton).
public class TaskLab {

    //Список всех заданий пользователя.
   private List<Task> mTaskList;

    //Единственный экземпляр хранилища.
    private static TaskLab sTaskLab;

    //Приватный конструктор, в котором инициализируется список заданий.
    private TaskLab(){
        mTaskList = new ArrayList<>();
        initiateList();
    }

    //Метод, с помощью которого можно получить экземпляр хранилища.
    public static TaskLab get(){
        //Если экземпляра нет - создать его.
        if(sTaskLab == null) sTaskLab = new TaskLab();
        return sTaskLab;
    }

    //Возвращает список заданий.
    public List<Task> getTaskList() {
        return mTaskList;
    }

    public void addTask(Task task){
        mTaskList.add(task);
    }


    private void initiateList(){
        for(int i = 0; i<100; i++){
            Task task = new Task();
            task.setText("User added new text, so it's task#"+ i);
            mTaskList.add(task);
        }
    }


}
