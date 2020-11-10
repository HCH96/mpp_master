package org.tech.mobileprogrammingproject.Daily;

import java.util.ArrayList;

public class TodoM {
    private String todoMorning;

    // 화면에 표시될 문자열 초기화
    public TodoM(String todoMorning) {
        this.todoMorning = todoMorning;
    }

    public String getTodoMorning() {
        return todoMorning;
    }

    public void setTodoMorning(String todoMorning) {
        this.todoMorning = todoMorning;
    }

    public static ArrayList<TodoM> createTodoList(String todo){
        ArrayList<TodoM> todoList = new ArrayList<TodoM>();
        TodoM todom = new TodoM(todo);
        todoList.add(todom);
        return todoList;
    }


}