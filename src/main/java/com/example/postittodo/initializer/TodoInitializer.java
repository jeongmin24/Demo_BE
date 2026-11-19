package com.example.postittodo.initializer;

import com.example.postittodo.domain.Todo;
import com.example.postittodo.repository.TodoRepository;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TodoInitializer implements ApplicationRunner {
    private final TodoRepository todoRepository;

    public TodoInitializer(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (todoRepository.count() > 0) {
            return;
        }

        todoRepository.saveAll(List.of(
                new Todo("프로젝트 구조 정리", true),
                new Todo("목록 조회 API 확인", false),
                new Todo("DevLink로 라이트모드 적용", false)
        ));
    }
}
