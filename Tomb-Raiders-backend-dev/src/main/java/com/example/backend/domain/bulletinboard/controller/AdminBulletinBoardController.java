package com.example.backend.domain.bulletinboard.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardRequest;
import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardReportResponse;
import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardResponse;
import com.example.backend.domain.bulletinboard.enums.BulletinBoardType;
import com.example.backend.domain.bulletinboard.service.BulletinBoardReportService;
import com.example.backend.domain.bulletinboard.service.BulletinBoardService;
import com.example.backend.domain.member.enums.Role;
import com.example.backend.global.annotation.permission.Permission;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@Permission(roles = {Role.ADMIN, Role.BULLETINBOARD_ADMIN})
public class AdminBulletinBoardController implements AdminBulletinBoardApi {

	private final BulletinBoardService bulletinBoardService;
	private final BulletinBoardReportService bulletinBoardReportService;

	@Override
	public Response<Void> deleteBoardByAdmin(Long boardId) {
		bulletinBoardService.deleteBoardByAdmin(boardId);
		return Response.success();
	}

	@Override
	public Response<Void> deleteCommentByAdmin(Long boardId, Long commentId) {
		bulletinBoardService.removeCommentByAdmin(commentId);
		return Response.success();
	}

	@Override
	public Response<Long> createAnnouncement(AuthMember admin, BulletinBoardRequest.Write request,
		List<MultipartFile> images) {
		BulletinBoardRequest.Write adminBoardRequest = new BulletinBoardRequest.Write(
			request.title(),
			request.content(),
			request.assetId(),
			BulletinBoardType.ADMIN_BOARD,
			request.commissionId(),
			request.retainedImageIds()
		);

		return Response.success(bulletinBoardService.createBoard(adminBoardRequest, images, admin));
	}

	@Override
	public Response<Page<BulletinBoardReportResponse>> getReports(Pageable pageable) {
		return Response.success(bulletinBoardReportService.getAllReports(pageable));
	}

	@Override
	public Response<BulletinBoardReportResponse> getReport(Long reportId) {
		return Response.success(bulletinBoardReportService.getReportDetail(reportId));
	}

	@Override
	public Response<Page<BulletinBoardResponse.Detail>> getBoardsByTargetMember(
		AuthMember admin,
		Long targetMemberId,
		Pageable pageable
	) {
		return Response.success(bulletinBoardService.getBoardsByMember(targetMemberId, admin.id(), pageable));
	}

	@Override
	public Response<Page<BulletinBoardResponse.CommentDetail>> getCommentsByTargetMember(
		Long targetMemberId,
		Pageable pageable
	) {
		return Response.success(bulletinBoardService.getCommentsByMember(targetMemberId, pageable));
	}
}
