package com.example.backend.domain.member.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.member.dto.request.MemberRequest;
import com.example.backend.domain.member.dto.request.PasswordChangeRequest;
import com.example.backend.domain.member.dto.request.PasswordResetConfirmRequest;
import com.example.backend.domain.member.dto.request.PasswordResetRequest;
import com.example.backend.domain.member.dto.request.WithdrawRequest;
import com.example.backend.domain.member.dto.response.MemberResponse;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthErrorCode;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberApi {

	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = "ACCOUNT_DISABLED")
	Response<Void> passwordReset(@Valid @RequestBody PasswordResetRequest request);

	@Operation(summary = "비밀번호 재설정 확정", description = "인증 코드 검증 후 실제 비밀번호를 변경")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class,
		errorCode = {"PASSWORD_MISMATCH", "INVALID_PASSWORD_RESET_VERIFICATION_CODE", "VERIFICATION_EMAIL_MISMATCH",
			"MEMBER_NOT_FOUND"})
	Response<Void> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request);

	@Operation(summary = "비밀번호 변경", description = "로그인한 사용자가 기존 비밀번호를 확인 후 새 비밀번호로 변경")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class,
		errorCode = {"MEMBER_NOT_FOUND", "MEMBER_SUSPENDED", "PASSWORD_MISMATCH"})
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = "INVALID_PASSWORD")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Void> changePassword(
		@Auth AuthMember member,
		@Valid @RequestBody PasswordChangeRequest request
	);

	@Operation(summary = "회원 탈퇴", description = "비밀번호 검증 후 회원 상태를 탈퇴로 변경")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = {"MEMBER_NOT_FOUND", "MEMBER_SUSPENDED"})
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = "INVALID_PASSWORD")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Void> withdraw(
		@Auth AuthMember member,
		@Valid @RequestBody WithdrawRequest request
	);

	@Operation(summary = "자기소개 수정", description = "회원의 자기소개 글을 수정 (기본값인 이메일 덮어쓰기)")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = {"MEMBER_NOT_FOUND", "MEMBER_SUSPENDED"})
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Void> updateIntroduce(
		@Auth AuthMember member,
		@Valid @RequestBody MemberRequest.UpdateIntroduce request
	);

	@Operation(summary = "닉네임 수정", description = "회원의 닉네임을 수정 (특수문자, 공백 불가 / 고유값)")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode =
		{"MEMBER_NOT_FOUND", "MEMBER_SUSPENDED", "DUPLICATE_NICKNAME"})
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Void> updateNickname(
		@Auth AuthMember member,
		@Valid @RequestBody MemberRequest.UpdateNickname request
	);

	@Operation(summary = "프로필 이미지 수정", description = "회원의 프로필 이미지를 폼 데이터(MultipartFile)로 받아 수정 (최대 5MB, 이미지 파일만 허용)")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = {"MEMBER_NOT_FOUND", "MEMBER_SUSPENDED"})
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Void> updateProfileImage(
		@Auth AuthMember member,
		@RequestPart(value = "image", required = false) MultipartFile image
	);

	@Operation(summary = "내 정보 조회", description = "로그인한 회원의 현재 정보를 조회 (정보 수정 폼 초기값 세팅용)")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	Response<MemberResponse> getMyInfo(@Auth AuthMember member);
}
