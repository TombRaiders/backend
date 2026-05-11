package com.example.backend.domain.commission.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.commission.dto.response.CommissionResponse;
import com.example.backend.domain.commission.service.CommissionService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommissionController implements CommissionApi {

	private final CommissionService commissionService;

	// 생성일 기준 역정렬
	@Override
	@GetMapping("/v1/commissions")
	public Response<Page<CommissionResponse>> getCommission(
		@Auth AuthMember member,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<CommissionResponse> responses = commissionService.checkList(member.id(), pageable);
		return Response.success(responses);
	}

	@Override
	@GetMapping("/v1/commissions/{commissionId}")
	public Response<CommissionResponse> getCommission(
		@Auth AuthMember member,
		@Positive @PathVariable("commissionId") Long commissionId
	) {
		CommissionResponse response = commissionService.check(member.id(), commissionId);
		return Response.success(response);
	}

	@Override
	@PostMapping("/v1/commissions")
	public Response<CommissionResponse> createCommission(
		@Auth AuthMember member,
		@RequestPart("imageFile") MultipartFile imageFile,
		@RequestParam(value = "templateId") Long templateId
	) throws IOException {
		CommissionResponse response = commissionService.createAiImage(member.id(), imageFile, templateId);

		return Response.success(response);
	}

	@Override
	@PostMapping("/v1/commissions/{commissionId}")
	public Response<CommissionResponse> reCreateCommission(
		@Auth AuthMember member,
		@Positive @PathVariable Long commissionId,
		@RequestParam(value = "templateId") Long templateId
	) throws IOException {
		CommissionResponse response = commissionService.reCreateAiImage(member.id(), commissionId, templateId);

		return Response.success(response);
	}

	@Override
	@PatchMapping("/v1/commissions/{commissionId}/good")
	public Response<CommissionResponse> updateCommissionGoodStatus(
		@Auth AuthMember member,
		@Positive @PathVariable Long commissionId
	) {
		CommissionResponse response = commissionService.updateGoodStatus(member.id(), commissionId);

		return Response.success(response);
	}

	@Override
	@DeleteMapping("/v1/commissions/{commissionId}")
	public Response<Void> deleteCommission(
		@Auth AuthMember member,
		@Positive @PathVariable("commissionId") Long commissionId
	) {
		commissionService.deleteCommission(member.id(), commissionId);
		return Response.success();
	}
}
