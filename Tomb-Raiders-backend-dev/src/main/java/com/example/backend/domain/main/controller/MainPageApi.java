package com.example.backend.domain.main.controller;

import java.util.List;

import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardResponse;
import com.example.backend.domain.main.dto.response.MainPageImageResponse;
import com.example.backend.global.dto.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "메인 페이지", description = "메인 화면에 표시되는 레일 이미지와 게시글 조회 API")
public interface MainPageApi {

	@Operation(summary = "메인 레일 이미지 목록 조회", description = "메인 화면 상단 레일에 노출할 이미지 목록을 노출 순서대로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메인 레일 이미지 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<List<MainPageImageResponse>> getRailImages();

	@Operation(summary = "메인 게시글 목록 조회", description = "메인 화면에 노출할 게시글 미리보기 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메인 게시글 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<List<BulletinBoardResponse.MainPreview>> getMainBoards();
}
