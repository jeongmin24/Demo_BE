package com.example.postittodo.service;

import com.example.postittodo.dto.response.TodoResponse;
import com.example.postittodo.repository.TodoRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> listTodos() {
        return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt", "id"))
                .stream().map(TodoResponse::from).toList();
    }
}
