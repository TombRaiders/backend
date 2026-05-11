package com.example.backend.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.backend.domain.member.dto.response.MemberResponse;
import com.example.backend.domain.member.enums.Role;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.annotation.permission.Permission;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Member", description = "관리자 전용 회원 관리 API (총 관리자/파트너 관리자 등)")
public interface AdminMemberApi {

	@Operation(summary = "관리자 목록 조회 (총 관리자 전용)", description = "시스템 내의 모든 관리자 계정을 조회합니다.")
	@ApiSuccessResponse(data = Page.class) // 제네릭 타입 명시를 위해 Page.class 지정
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN})
	Response<Page<MemberResponse>> getAdmins(Pageable pageable);

	@Operation(summary = "회원 권한 변경", description = "대상의 권한을 승격하거나 강등합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN, Role.BULLETINBOARD_ADMIN})
	Response<Void> changeMemberRole(
		@Auth AuthMember operator,
		@PathVariable("targetMemberId") Long targetMemberId,
		@RequestParam("newRole") Role newRole
	);

	@Operation(summary = "회원 단건 조회 (PK)", description = "member_id(PK)를 통해 특정 회원을 조회합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN}) // 필요에 따라 조회 권한 조정
	Response<MemberResponse> getMemberById(@PathVariable Long memberId);

	@Operation(summary = "회원 단건 조회 (로그인 ID)", description = "로그인 ID를 통해 특정 회원을 조회합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN}) // 필요에 따라 조회 권한 조정
	Response<MemberResponse> getMemberByLoginId(@RequestParam String loginId);

	@Operation(summary = "전체 회원 목록 조회", description = "시스템 내의 모든 회원 계정을 페이징하여 조회합니다.")
	@ApiSuccessResponse(data = Page.class)
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = {Role.ADMIN, Role.PARTNER_ADMIN, Role.BULLETINBOARD_ADMIN})
	Response<Page<MemberResponse>> getAllMembers(Pageable pageable);
}
