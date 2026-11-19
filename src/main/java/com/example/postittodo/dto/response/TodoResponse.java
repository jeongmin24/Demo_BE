package com.example.postittodo.dto.response;

import com.example.postittodo.domain.Todo;
import java.time.Instant;

public record TodoResponse(Long id, String content, boolean completed, Instant createdAt, Instant updatedAt) {
    public static TodoResponse from(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getContent(), todo.isCompleted(),
                todo.getCreatedAt(), todo.getUpdatedAt());
    }
}
