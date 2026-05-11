package com.example.backend.domain.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.domain.member.dto.request.MemberBanRequest;
import com.example.backend.domain.member.dto.request.MemberUnbanRequest;
import com.example.backend.domain.member.dto.response.MemberResponse;
import com.example.backend.domain.member.enums.Role;
import com.example.backend.global.annotation.permission.Permission;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Member Ban (Admin)", description = "관리자 회원 제재 관리 API")
public interface MemberBanApi {

	@Operation(summary = "회원 차단 (정지)", description = "관리자가 특정 회원을 지정된 기간(일) 동안 정지시킵니다. (기간 미입력 시 영구 정지)")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = {"MEMBER_NOT_FOUND", "ALREADY_BANNED"})
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN, Role.BULLETINBOARD_ADMIN})
	Response<Void> banMember(@Valid @RequestBody MemberBanRequest request);

	@Operation(summary = "회원 차단 해제", description = "정지된 회원을 즉시 활동 가능 상태(ACTIVE)로 변경합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN, Role.BULLETINBOARD_ADMIN})
	Response<Void> unbanMember(@Valid @RequestBody MemberUnbanRequest request);

	@Operation(summary = "차단된 회원 목록 조회", description = "현재 정지 상태(BANNED)인 모든 회원을 조회합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN, Role.BULLETINBOARD_ADMIN})
	Response<List<MemberResponse>> getBannedMembers();
}
