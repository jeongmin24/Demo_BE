package com.example.responsecheck.controller;

import com.example.responsecheck.dto.response.ResponseCheckResponse;
import com.example.responsecheck.service.ResponseCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/response-check")
public class ResponseCheckController {
    private final ResponseCheckService service;

    public ResponseCheckController(ResponseCheckService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ResponseCheckResponse> check() {
        return ResponseEntity.ok(new ResponseCheckResponse(HttpStatus.OK.value(), service.message()));
    }
}
