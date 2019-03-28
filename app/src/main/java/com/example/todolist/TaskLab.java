package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.database.DBSchema;
import com.example.todolist.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Клас, отвечающий за хранение заданий и работу с ними(Singleton).
public class TaskLab {


    //Единственный экземпляр хранилища.
    private static TaskLab sTaskLab;

    //Обьект базы данных.
    private SQLiteDatabase mSQLiteDatabase;


    //Приватный конструктор, в котором инициализируется список заданий.
    private TaskLab(Context context){
        //Получаем экземпляр базы данных через помошника.
        mSQLiteDatabase = new DataBaseHelper(context)
                .getWritableDatabase();
        initiateList();
    }

    //Метод, с помощью которого можно получить экземпляр хранилища.
    public static TaskLab get(Context context){
        //Если экземпляра нет - создать его.
        if(sTaskLab == null) sTaskLab = new TaskLab(context);
        return sTaskLab;
    }

    //Получаем определенное задание по UUID.
    public Task getTask(UUID id){
        Cursor cursor = mSQLiteDatabase.query(DBSchema.TaskTable.NAME,
                null,
                DBSchema.TaskTable.Collums.UUID + "=?",
                new String[]{id.toString()},
                null,
                null,
                null,
                null
        );
        return getTaskFromCursor(cursor);
    }

    //Возвращает список заданий.
    public List<Task> getTaskList() {
        //Получаем обьект Cursor со всеми строками таблицы.
        Cursor cursor = mSQLiteDatabase.query(DBSchema.TaskTable.NAME,
                null, //Выбираем все столбцы.
                null,
                null,
                null,
                null,
                null
        );
        return getAllFromCursor(cursor);
    }
    //Возвращает конкретный экземпляр задания.
    public void addTask(Task task){
        //Получаем ContentValues из Task для вставки в БД.
        ContentValues contentValues = getContentValues(task);
        //Записываем новое задание в БД.
        mSQLiteDatabase.insert(DBSchema.TaskTable.NAME, null, contentValues);
    }

    //Метод, с помощью которого удаляем абсолютно все задания.
    public void deleteAll(){
        mSQLiteDatabase.delete(DBSchema.TaskTable.NAME, null, null);
    }

    //Удаляется определенное задание по UUID.
    public void deleteTask(UUID id){
        mSQLiteDatabase.delete(DBSchema.TaskTable.NAME, DBSchema.TaskTable.Collums.UUID + "=?", new String[]{id.toString()});
    }



    private void initiateList(){
        for(int i = 0; i<5; i++){
            Task task = new Task();
            task.setText("User added new text, so it's task#"+ i);
            addTask(task);
        }
    }

    //Вспомогательный метод, который преобразует обьект Task в ContentValues для вставки в БД.
    private static ContentValues getContentValues(Task task) {
        //Создается обьект ContentValues.
        ContentValues contentValues = new ContentValues();
        //Заполнение значениями: Key - имя столбца, Value - значение, которое нужно добавить в этот столбец.
        contentValues.put(DBSchema.TaskTable.Collums.UUID, task.getId().toString());
        contentValues.put(DBSchema.TaskTable.Collums.TEXT, task.getText());
        return contentValues;
    }

    //Вспомогательный метод, преобразует данные из Cursor в список из заданий.
    private List<Task> getAllFromCursor(Cursor cursor){
        //Инициализируем список.
        List<Task> list = new ArrayList<>();
        //Перемещаем курсор на первую позицию.
        cursor.moveToFirst();
        //Пока курсор не за пределами массива данных, извлекаем данные.
        while(!cursor.isAfterLast()){
            //Извлекаем уникальный идентификатор.
            String uuidStr = cursor.getString(cursor.getColumnIndex(DBSchema.TaskTable.Collums.UUID));
            //Извлекаем текст.
            String userText = cursor.getString(cursor.getColumnIndex(DBSchema.TaskTable.Collums.TEXT));
            //Создаем задание с уже известным Айди.
            Task task = new Task(UUID.fromString(uuidStr));
            task.setText(userText);
            //Добавляем задание в список.
            list.add(task);
            //Переходим к следующей записи в бд.
            cursor.moveToNext();
        }
        //Закрываем курсор, освобождаем ресурсы.
        cursor.close();
        return list;
    }

    //Вспомогательный метод, преобразует данные из Cursor в задание.
    private static Task getTaskFromCursor(Cursor cursor){
        //Перемещаем курсор на первую позицию.
        cursor.moveToFirst();
        //Извлекаем уникальный идентификатор.
        String uuidStr = cursor.getString(cursor.getColumnIndex(DBSchema.TaskTable.Collums.UUID));
        //Извлекаем текст.
        String userText = cursor.getString(cursor.getColumnIndex(DBSchema.TaskTable.Collums.TEXT));
        //Создаем задание с уже известным Айди.
        Task task = new Task(UUID.fromString(uuidStr));
        task.setText(userText);
        //Закрываем курсор, освобождаем ресурсы.
        cursor.close();
        return task;
    }


}
