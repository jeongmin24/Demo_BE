package com.example.responsecheck.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private static final String DEMO_TIMESTAMP = "2026-11-20T00:00:00.000Z";

    // Keep the order aligned with the frontend demo list, which sends the card index.
    private static final List<TodoDetail> DEMO_TODOS = List.of(
            demoTodo(-1, "오늘 꼭 해야 할 일\n중요한 목표 세 가지 정하기", true),
            demoTodo(-2, "집중해서 30분 독서하기\n마음에 남는 문장 기록하기", false),
            demoTodo(-3, "점심 먹고 가볍게 산책하기\n햇빛 보면서 잠깐 쉬기", false),
            demoTodo(-4, "밀린 메일 확인하고\n필요한 답장 보내기", true),
            demoTodo(-5, "진행 중인 프로젝트\n다음 작업 정리하기", false),
            demoTodo(-6, "물 한 잔 마시고\n어깨와 목 스트레칭하기", true),
            demoTodo(-7, "이번 주 장보기 목록\n냉장고 확인하며 적어두기", false),
            demoTodo(-8, "하루를 마무리하며\n오늘 잘한 일 하나 기록하기", false)
    );

    @GetMapping("/detail/{postNum}")
    public ResponseEntity<TodoDetail> getDetail(@PathVariable("postNum") int postNum) {
        if (postNum < 0 || postNum >= DEMO_TODOS.size()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(DEMO_TODOS.get(postNum));
    }

    private static TodoDetail demoTodo(int id, String content, boolean completed) {
        return new TodoDetail(id, content, completed, DEMO_TIMESTAMP, DEMO_TIMESTAMP);
    }

    public record TodoDetail(int id, String content, boolean completed, String createdAt, String updatedAt) {
    }
}
