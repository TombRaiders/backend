package com.example.backend.domain.bulletinboard.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardRequest;
import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardReportResponse;
import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardResponse;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Tag(name = "Admin BulletinBoard", description = "게시판 관리자 전용 API")
@RequestMapping("/api")
public interface AdminBulletinBoardApi {

	@Operation(summary = "게시글 강제 삭제", description = "관리자 권한으로 특정 게시글을 강제로 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@DeleteMapping("/admin/v1/bulletin-boards/{boardId}")
	Response<Void> deleteBoardByAdmin(
		@Parameter(description = "삭제할 게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId
	);

	@Operation(summary = "댓글 강제 삭제", description = "관리자 권한으로 특정 댓글을 강제로 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@DeleteMapping("/admin/v1/bulletin-boards/{boardId}/comments/{commentId}")
	Response<Void> deleteCommentByAdmin(
		@Parameter(description = "게시글 ID (참조용)", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId,
		@Parameter(description = "삭제할 댓글 ID", required = true, example = "100")
		@Positive @PathVariable("commentId") Long commentId
	);

	@Operation(summary = "공지사항 작성", description = "관리자 권한으로 공지사항(게시글)을 작성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "공지사항 작성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 입력값"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@PostMapping(value = "/admin/v1/bulletin-boards", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	Response<Long> createAnnouncement(
		@Parameter(hidden = true) @Auth AuthMember admin,
		@Valid @RequestPart("request") BulletinBoardRequest.Write request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	);

	@Operation(summary = "신고 내역 목록 조회", description = "접수된 신고 내역을 최신순으로 페이징하여 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/admin/v1/bulletin-boards/reports")
	Response<Page<BulletinBoardReportResponse>> getReports(
		@Parameter(description = "페이지 정보")
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "신고 내역 상세 조회", description = "특정 신고 내역의 상세 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 신고 내역"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/admin/v1/bulletin-boards/reports/{reportId}")
	Response<BulletinBoardReportResponse> getReport(
		@Parameter(description = "신고 ID", required = true, example = "5")
		@Positive @PathVariable("reportId") Long reportId
	);

	@Operation(summary = "특정 유저가 작성한 게시글 목록 조회", description = "관리자 권한으로 특정 유저의 게시글 목록을 페이징하여 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/admin/v1/bulletin-boards/boards-info/{memberId}")
	Response<Page<BulletinBoardResponse.Detail>> getBoardsByTargetMember(
		@Parameter(hidden = true) @Auth AuthMember admin,
		@Parameter(description = "조회할 대상 유저 ID", required = true, example = "2")
		@Positive @PathVariable("memberId") Long targetMemberId,
		@Parameter(description = "페이지 정보")
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "특정 유저가 작성한 댓글 목록 조회", description = "관리자 권한으로 특정 유저의 댓글 목록을 페이징하여 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/admin/v1/bulletin-boards/comments/comments-info/{memberId}")
	Response<Page<BulletinBoardResponse.CommentDetail>> getCommentsByTargetMember(
		@Parameter(description = "조회할 대상 유저 ID", required = true, example = "2")
		@Positive @PathVariable("memberId") Long targetMemberId,
		@Parameter(description = "페이지 정보")
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);
}
