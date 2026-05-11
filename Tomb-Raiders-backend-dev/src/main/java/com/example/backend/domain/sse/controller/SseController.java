package com.example.backend.domain.sse.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.backend.domain.sse.service.SseService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SseController implements SseApi {

	private final SseService sseService;

	@Override
	@GetMapping(value = "/v1/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connect(@Auth AuthMember member) {
		return sseService.connect(member.id(), member.role());
	}
}
