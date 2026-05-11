package com.example.backend.domain.sse.controller;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "SSE API", description = "SSE 연결 및 데이터 전송 API")
public interface SseApi {

	@Operation(summary = "SSE 연결", description = "로그인한 멤버가 실시간 알림을 받기 위해 서버와 연결")
	@ApiResponse(
		responseCode = "200",
		description = "연결 성공",
		content = @Content(
			mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
			examples = @ExampleObject(
				name = "Connect Event",
				value = "data: {\"type\": \"CONNECT\", \"message\": \"connected\"}\n\n"
			)
		)
	)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	SseEmitter connect(@Auth AuthMember member);
}
