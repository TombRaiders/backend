package com.example.backend.domain.bulletinboard.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardReportRequest;
import com.example.backend.domain.bulletinboard.service.BulletinBoardReportService;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class BulletinBoardReportController implements BulletinBoardReportApi {

	private final BulletinBoardReportService bulletinBoardReportService;

	@Override
	public Response<Void> createReport(AuthMember member, BulletinBoardReportRequest request) {
		bulletinBoardReportService.createReport(member.id(), request);
		return Response.success();
	}
}
