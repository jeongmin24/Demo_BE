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
    private static final List<DemoTodo> ORIGINAL_DEMO_TODOS = List.of(
            new DemoTodo("프로젝트 구조 정리", true),
            new DemoTodo("목록 조회 API 확인", false),
            new DemoTodo("DevLink로 라이트모드 적용", false)
    );

    private static final List<DemoTodo> ADDITIONAL_DEMO_TODOS = List.of(
            new DemoTodo("프로젝트 README 검토", true),
            new DemoTodo("팀원에게 시연 순서 공유", false),
            new DemoTodo("서버 접속 주소 확인", true),
            new DemoTodo("작업 완료 결과 기록", false),
            new DemoTodo("모바일 화면에서 카드 간격 확인", false),
            new DemoTodo("API 응답 예시 정리", false),
            new DemoTodo("보드의 빈 목록 문구 확인", true),
            new DemoTodo("시연 후 확인할 항목 정리", false),
            new DemoTodo("로컬 브로커 연결 확인", true),
            new DemoTodo("두 테마에서 포스트잇 색 비교", false),
            new DemoTodo("프론트엔드 빌드 확인", true),
            new DemoTodo("작업 요청 문구 작성", false),
            new DemoTodo("시연 화면 캡처", false),
            new DemoTodo("포스트잇 보드 스타일 검토", true),
            new DemoTodo("소스코드 맵에서 API 경로 찾기", false),
            new DemoTodo("팀 에이전트 접속 확인", false),
            new DemoTodo("오늘 시연 흐름 리허설", false)
    );

    private final TodoRepository todoRepository;

    public TodoInitializer(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        long todoCount = todoRepository.count();
        if (todoCount == 0) {
            insertDemoTodos(ORIGINAL_DEMO_TODOS);
        } else if (todoCount != ORIGINAL_DEMO_TODOS.size() || !hasOriginalDemoTodos()) {
            return;
        }

        insertDemoTodos(ADDITIONAL_DEMO_TODOS);
    }

    private boolean hasOriginalDemoTodos() {
        List<DemoTodo> existingTodos = todoRepository.findAll().stream()
                .map(todo -> new DemoTodo(todo.getContent(), todo.isCompleted()))
                .toList();
        return existingTodos.containsAll(ORIGINAL_DEMO_TODOS);
    }

    private void insertDemoTodos(List<DemoTodo> demoTodos) {
        todoRepository.saveAll(demoTodos.stream()
                .map(todo -> new Todo(todo.content(), todo.completed()))
                .toList());
    }

    private record DemoTodo(String content, boolean completed) {}
}
