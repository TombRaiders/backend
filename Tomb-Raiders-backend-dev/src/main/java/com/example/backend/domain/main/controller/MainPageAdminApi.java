package com.example.backend.domain.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardRequest;
import com.example.backend.domain.main.dto.request.CreateMainPageImageRequest;
import com.example.backend.domain.main.dto.request.UpdateMainPageImageRequest;
import com.example.backend.domain.main.dto.response.MainPageImageResponse;
import com.example.backend.global.dto.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Tag(name = "관리자 메인 페이지", description = "메인 화면 레일 이미지와 핀 고정 게시글을 관리하는 관리자 API")
public interface MainPageAdminApi {

	@Operation(summary = "관리자 메인 레일 이미지 목록 조회", description = "관리자 화면에서 메인 레일 이미지 목록을 노출 순서대로 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메인 레일 이미지 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<List<MainPageImageResponse>> getRailImages();

	@Operation(summary = "메인 레일 이미지 등록", description = "메인 화면에 노출할 레일 이미지를 multipart/form-data 형식으로 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메인 레일 이미지 등록 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 입력값 또는 등록 가능 개수 초과"),
		@ApiResponse(responseCode = "500", description = "이미지 업로드 실패 또는 서버 내부 오류")
	})
	Response<MainPageImageResponse> createRailImage(
		@Valid @ModelAttribute CreateMainPageImageRequest request
	) throws IOException;

	@Operation(summary = "메인 레일 이미지 수정", description = "메인 레일 이미지의 이미지 파일, 대체 텍스트, 노출 순서를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메인 레일 이미지 수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 입력값"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 메인 레일 이미지"),
		@ApiResponse(responseCode = "500", description = "이미지 업로드 실패 또는 서버 내부 오류")
	})
	Response<MainPageImageResponse> updateRailImage(
		@Parameter(description = "수정할 메인 레일 이미지 ID", required = true, example = "1")
		@Positive @PathVariable Long imageId,
		@Valid @ModelAttribute UpdateMainPageImageRequest request
	) throws IOException;

	@Operation(summary = "메인 레일 이미지 삭제", description = "지정한 메인 레일 이미지를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메인 레일 이미지 삭제 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 메인 레일 이미지"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<Void> deleteRailImage(
		@Parameter(description = "삭제할 메인 레일 이미지 ID", required = true, example = "1")
		@Positive @PathVariable Long imageId
	);

	@Operation(summary = "메인 핀 고정 게시글 수정", description = "메인 화면에 핀 고정으로 노출할 게시글 목록과 노출 순서를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메인 핀 고정 게시글 수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 입력값 또는 핀 고정 가능 개수 초과"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<Void> updatePinpoints(
		@Valid @RequestBody BulletinBoardRequest.UpdatePinpoints request
	);
}
