package com.example.backend.domain.bulletinboard.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardRequest;
import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardResponse;
import com.example.backend.domain.bulletinboard.enums.BulletinBoardType;
import com.example.backend.domain.bulletinboard.service.BulletinBoardService;
import com.example.backend.domain.bulletinboard.service.PopularBulletinBoardService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BulletinBoardController implements BulletinBoardApi {

	private final BulletinBoardService bulletinBoardService;
	private final PopularBulletinBoardService popularBulletinBoardService;

	@Override
	@GetMapping("/v1/bulletin-boards")
	public Response<Page<BulletinBoardResponse.Detail>> getAllBoards(
		@Auth(required = false) AuthMember member,
		@RequestParam(defaultValue = "FREE_BOARD") BulletinBoardType type,
		@PageableDefault(size = 20, sort = "createdAt") Pageable pageable
	) {
		Long memberId = (member != null) ? member.id() : null;
		return Response.success(bulletinBoardService.getAllBoards(memberId, type, pageable));
	}

	@Override
	@GetMapping("/v1/bulletin-boards/{boardId}")
	public Response<BulletinBoardResponse.Detail> getBoard(
		@Auth(required = false) AuthMember member,
		@Positive @PathVariable("boardId") Long boardId
	) {
		Long memberId = (member != null) ? member.id() : null;
		return Response.success(bulletinBoardService.getBoard(boardId, memberId));
	}

	@Override
	@GetMapping("/v1/bulletin-boards/popular")
	public Response<List<BulletinBoardResponse.Popular>> getPopularBoards() {
		return Response.success(popularBulletinBoardService.getPopularBoards());
	}

	@Override
	@PostMapping(value = "/v1/bulletin-boards", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<Long> createBoard(
		@Auth AuthMember member,
		@Valid @RequestPart("request") BulletinBoardRequest.Write request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		return Response.success(bulletinBoardService.createBoard(request, images, member));
	}

	@Override
	@PutMapping(value = "/v1/bulletin-boards/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<Long> updateBoard(
		@Auth AuthMember member,
		@Positive @PathVariable("boardId") Long boardId,
		@Valid @RequestPart("request") BulletinBoardRequest.Write request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		return Response.success(bulletinBoardService.updateBoard(boardId, request, images, member.id()));
	}

	@Override
	@DeleteMapping("/v1/bulletin-boards/{boardId}")
	public Response<Void> deleteBoard(
		@Auth AuthMember member,
		@Positive @PathVariable("boardId") Long boardId
	) {
		bulletinBoardService.deleteBoard(boardId, member.id());
		return Response.success();
	}

	@Override
	@GetMapping("/v1/bulletin-boards/{boardId}/comments")
	public Response<Page<BulletinBoardResponse.CommentDetail>> getComments(
		@Positive @PathVariable("boardId") Long boardId,
		@PageableDefault(size = 50) Pageable pageable
	) {
		return Response.success(bulletinBoardService.getComments(boardId, pageable));
	}

	@Override
	@PostMapping("/v1/bulletin-boards/{boardId}/comments")
	public Response<Void> addComment(
		@Auth AuthMember member,
		@Positive @PathVariable("boardId") Long boardId,
		@Valid @RequestBody BulletinBoardRequest.AddComment request
	) {
		bulletinBoardService.addComment(boardId, member.id(), request);
		return Response.success();
	}

	@Override
	@PatchMapping("/v1/bulletin-boards/{boardId}/comments/{commentId}")
	public Response<Void> updateComment(
		@Auth AuthMember member,
		@Positive @PathVariable("boardId") Long boardId,
		@Positive @PathVariable("commentId") Long commentId,
		@RequestBody String content
	) {
		bulletinBoardService.updateComment(commentId, content, member.id());
		return Response.success();
	}

	@Override
	@DeleteMapping("/v1/bulletin-boards/{boardId}/comments/{commentId}")
	public Response<Void> removeComment(
		@Auth AuthMember member,
		@Positive @PathVariable("boardId") Long boardId,
		@Positive @PathVariable("commentId") Long commentId
	) {
		bulletinBoardService.removeComment(boardId, commentId, member.id());
		return Response.success();
	}

	@Override
	@PostMapping("/v1/bulletin-boards/{boardId}/recommends")
	public Response<Void> toggleRecommend(
		@Auth AuthMember member,
		@Positive @PathVariable("boardId") Long boardId
	) {
		bulletinBoardService.toggleRecommend(boardId, member.id());
		return Response.success();
	}

	@Override
	@GetMapping("/v1/bulletin-boards/boards-info")
	public Response<Page<BulletinBoardResponse.Detail>> getMyBoards(
		@Auth AuthMember member,
		@PageableDefault(size = 20, sort = "createdAt") Pageable pageable
	) {
		// targetMemberId와 currentMemberId 모두 본인(member.id())
		return Response.success(bulletinBoardService.getBoardsByMember(member.id(), member.id(), pageable));
	}

	@Override
	@GetMapping("/v1/bulletin-boards/comments/comments-info")
	public Response<Page<BulletinBoardResponse.CommentDetail>> getMyComments(
		@Auth AuthMember member,
		@PageableDefault(size = 20, sort = "createdAt") Pageable pageable
	) {
		return Response.success(bulletinBoardService.getCommentsByMember(member.id(), pageable));
	}
}
