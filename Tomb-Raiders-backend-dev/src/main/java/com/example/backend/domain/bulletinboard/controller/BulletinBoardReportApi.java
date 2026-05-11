package com.example.backend.domain.bulletinboard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardReportRequest;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "BulletinBoard Report", description = "게시판 신고 기능 API")
@RequestMapping("/api") // 모든 API의 공통 진입점
public interface BulletinBoardReportApi {

	@Operation(summary = "게시글/댓글 신고", description = "불건전한 게시글이나 댓글을 신고합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "신고 접수 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 입력값"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@PostMapping("/v1/bulletin-board/reports")
	Response<Void> createReport(
		@Auth AuthMember member,
		@Valid @RequestBody BulletinBoardReportRequest request
	);
}
