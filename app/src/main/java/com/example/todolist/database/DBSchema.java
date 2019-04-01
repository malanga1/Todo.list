package com.example.todolist.database;

public class DBSchema {
    public static final class TaskTable {
        public static final String NAME = "tasks";

        public static final class Collums {
            public static final String UUID = "uuid";
            public static final String TEXT = "text";
            public static final String IS_DONE = "isdone";
        }
    }
}
