package com.example.postittodo.controller;

import com.example.postittodo.dto.response.TodoResponse;
import com.example.postittodo.service.TodoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoResponse> listTodos() {
        return todoService.listTodos();
    }
}
