package com.example.backend.domain.bannedword.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "관리자 금지어 관리", description = "금지어를 실시간으로 추가하거나 삭제하는 관리자 전용 API")
public interface AdminBannedWordApi {

	@Operation(summary = "금지어 추가", description = "새로운 금지어를 DB와 메모리에 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "✅ 금지어 추가 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 잘못된 요청 (빈 문자열 등)"),
		@ApiResponse(responseCode = "409", description = "⚠️ 이미 존재하는 금지어")
	})
	ResponseEntity<String> add(
		@Parameter(description = "추가할 단어", required = true, example = "바보")
		@RequestParam("word") String word
	);

	@Operation(summary = "금지어 삭제", description = "기존 금지어를 DB와 메모리에서 제거합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "✅ 금지어 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "❌ 존재하지 않는 금지어라 삭제 실패")
	})
	ResponseEntity<String> delete(
		@Parameter(description = "삭제할 단어", required = true, example = "바보")
		@RequestParam("word") String word
	);
}
