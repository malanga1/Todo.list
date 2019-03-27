package com.example.todolist;

import java.util.UUID;

public class Task {
    //Поле, хранящее текст пользователя.
    private String mText;
    //Поле, хранящее уникальный идентификатор задания.
    private UUID mId;
    //Пустой конструктор для создания нового задания.
    public Task(){
      this(UUID.randomUUID());
    }
    //Конструктор для иницилизирования уже имеющегося задания (Id для него уже известен).
    public Task(UUID id){
        mId = id;
    }
    //Метод, для получения текста задания.
    public String getText() {
        return mText;
    }
    //Метод, для установки текста задания.
    public void setText(String text) {
        mText = text;
    }
    //Метод, для получения уникального идентификатора задания.
    public UUID getId() {
        return mId;
    }
}
