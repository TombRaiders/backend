package com.example.backend.domain.bulletinboard.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardRequest;
import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardResponse;
import com.example.backend.domain.bulletinboard.enums.BulletinBoardType;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Tag(name = "Bulletin Board", description = "게시판 통합 API (게시글, 댓글, 추천)")
public interface BulletinBoardApi {

	@Operation(summary = "게시글 전체 조회", description = "게시판의 모든 게시글을 최신순으로 페이징하여 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<Page<BulletinBoardResponse.Detail>> getAllBoards(
		@Parameter(hidden = true) @Auth(required = false) AuthMember member,
		@Parameter(description = "페이지 정보 (기본값: size=50, sort=createdAt,desc)")
		@RequestParam(defaultValue = "FREE_BOARD") BulletinBoardType type,
		@PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "게시글 상세 조회", description = "게시글 ID를 통해 상세 내용, 에셋 정보, 댓글 수, 추천 수 등을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<BulletinBoardResponse.Detail> getBoard(
		@Parameter(hidden = true) @Auth(required = false) AuthMember member,
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId
	);

	@Operation(summary = "인기글 조회", description = "추천수가 높은 전체 게시글 Top 10을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<List<BulletinBoardResponse.Popular>> getPopularBoards();


	@Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "작성 성공 (생성된 게시글 ID 반환)"),
		@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패 (제목/내용 누락 등)"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<Long> createBoard(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Valid @RequestPart("request") BulletinBoardRequest.Write request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	);

	@Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다. (작성자만 가능)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "수정 성공 (게시글 ID 반환)"),
		@ApiResponse(responseCode = "403", description = "수정 권한 없음 (작성자가 아님)"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글")
	})
	Response<Long> updateBoard(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "수정할 게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId,
		@Valid @RequestPart("request") BulletinBoardRequest.Write request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	);

	@Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. (Soft Delete 적용, 작성자만 가능)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "삭제 성공"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음 (작성자가 아님)"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글")
	})
	Response<Void> deleteBoard(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "삭제할 게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId
	);

	@Operation(summary = "댓글 목록 조회", description = "특정 게시글에 달린 댓글 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글")
	})
	Response<Page<BulletinBoardResponse.CommentDetail>> getComments(
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId,
		@Parameter(description = "페이지 정보 (기본값: size=50)")
		@PageableDefault(size = 50) Pageable pageable
	);

	@Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다. (parentId 입력 시 대댓글)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "작성 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글 또는 부모 댓글")
	})
	Response<Void> addComment(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId,
		@Valid @RequestBody BulletinBoardRequest.AddComment request
	);

	@Operation(summary = "댓글 수정", description = "작성자가 본인의 댓글 내용을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "수정 성공"),
		@ApiResponse(responseCode = "403", description = "수정 권한 없음"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글")
	})
	Response<Void> updateComment(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId,
		@Parameter(description = "수정할 댓글 ID", required = true, example = "100")
		@Positive @PathVariable("commentId") Long commentId,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "수정할 댓글 내용 (Text Plain)", required = true,
			content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "수정된 댓글 내용입니다."))
		)
		@RequestBody String content
	);

	@Operation(summary = "댓글 삭제", description = "작성자가 본인의 댓글을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "삭제 성공"),
		@ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 댓글")
	})
	Response<Void> removeComment(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId,
		@Parameter(description = "삭제할 댓글 ID", required = true, example = "100")
		@Positive @PathVariable("commentId") Long commentId
	);

	@Operation(summary = "게시글 추천(좋아요) 토글", description = "게시글을 추천하거나, 이미 추천했다면 취소합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "토글 성공 (추천됨/취소됨)"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 게시글")
	})
	Response<Void> toggleRecommend(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "게시글 ID", required = true, example = "1")
		@Positive @PathVariable("boardId") Long boardId
	);

	@Operation(summary = "내가 작성한 게시글 목록 조회", description = "로그인한 사용자가 자신이 작성한 게시글 목록을 최신순으로 페이징하여 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<Page<BulletinBoardResponse.Detail>> getMyBoards(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "페이지 정보 (기본값: size=20, sort=createdAt,desc)")
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "내가 작성한 댓글 목록 조회", description = "로그인한 사용자가 자신이 작성한 댓글 목록을 최신순으로 페이징하여 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	Response<Page<BulletinBoardResponse.CommentDetail>> getMyComments(
		@Parameter(hidden = true) @Auth AuthMember member,
		@Parameter(description = "페이지 정보 (기본값: size=20, sort=createdAt,desc)")
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);
}
