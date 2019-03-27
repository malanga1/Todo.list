package com.example.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.todolist.database.DBSchema.TaskTable;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "taskdb";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //Если БД не существует, она создаётся с помощью этого метода.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TaskTable.NAME + "(" + " _id integer primary key autoincrement, " +
                TaskTable.Collums.UUID + ", " +
                TaskTable.Collums.TEXT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
