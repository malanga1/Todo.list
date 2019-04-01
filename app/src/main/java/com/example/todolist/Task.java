package com.example.todolist;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Task implements Serializable {

    //Поле, хранящее текст пользователя.
    private String mText;
    //Поле, хранящее уникальный идентификатор задания.
    private UUID mId;
    //Поле, которое показывает, сделано ли задание.
    private boolean isDone;

    //Пустой конструктор для создания нового задания.
    public Task() {
        this(UUID.randomUUID());
    }

    //Конструктор для иницилизирования уже имеющегося задания (Id для него уже известен).
    public Task(UUID id) {
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

    //Проверка, сделано ли здание.
    public boolean isDone() {
        return isDone;
    }

    //Устанавливаем, сделано ли задание.
    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(mText, task.mText) &&
                Objects.equals(mId, task.mId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mText, mId);
    }
}
