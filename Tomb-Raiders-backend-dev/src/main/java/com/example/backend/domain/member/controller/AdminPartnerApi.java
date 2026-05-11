package com.example.backend.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.backend.domain.member.dto.response.PartnerResponse;
import com.example.backend.domain.member.enums.Role;
import com.example.backend.global.annotation.permission.Permission;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthErrorCode;
import com.example.backend.global.errorcode.PartnerErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Partner", description = "관리자 전용 파트너 관리 API")
public interface AdminPartnerApi {

	@Operation(summary = "신규 파트너 신청 목록 조회", description = "승인 대기(PENDING) 중인 파트너 신청 목록을 페이징하여 조회합니다.")
	@ApiSuccessResponse(data = Page.class)
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN})
	Response<Page<PartnerResponse>> getPendingPartners(Pageable pageable);

	@Operation(summary = "파트너 신청 수락", description = "신청을 수락하여 상태를 ACTIVE로 변경하고 회원 권한을 PARTNER로 승급합니다.")
	@ApiSuccessResponse
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN})
	Response<Void> approvePartner(@PathVariable("partnerId") Long partnerId);

	@Operation(summary = "파트너 신청 거절", description = "신청을 거절하고 해당 파트너 신청 데이터를 삭제하여 재신청 가능하게 합니다.")
	@ApiSuccessResponse
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN})
	Response<Void> rejectPartner(@PathVariable("partnerId") Long partnerId);

	@Operation(
		summary = "파트너 강제 삭제 (권한 강등)",
		description = "관리자 권한으로 기존 파트너를 강제 삭제 및 권한을 일반 유저(MEMBER)로 강등시킵니다."
	)
	@ApiSuccessResponse
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN})
	Response<Void> deletePartner(@PathVariable("partnerId") Long partnerId);

	@Operation(summary = "전체 파트너 목록 조회", description = "시스템에 등록된 모든 파트너(상태 무관) 목록을 페이징하여 조회합니다.")
	@ApiSuccessResponse(data = Page.class)
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN})
	Response<Page<PartnerResponse>> getAllPartners(Pageable pageable);

	@Operation(summary = "파트너 상세 단일 조회", description = "특정 파트너의 상세 정보를 조회합니다.")
	@ApiSuccessResponse(data = PartnerResponse.class)
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN})
	Response<PartnerResponse> getPartner(@PathVariable("partnerId") Long partnerId);
}
