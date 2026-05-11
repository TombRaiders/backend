package com.example.backend.domain.commission.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.commission.dto.response.CommissionResponse;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.CommissionErrorCode;
import com.example.backend.global.errorcode.ImageErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;

@Tag(name = "Commission", description = "AI 이미지 의뢰 관리 API")
public interface CommissionApi {

	@Operation(summary = "의뢰 목록 조회", description = "로그인한 사용자가 요청한 AI 이미지 의뢰 목록을 조회")
	@ApiSuccessResponse(data = CommissionResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Page<CommissionResponse>> getCommission(@Auth AuthMember member, Pageable pageable);

	@Operation(summary = "의뢰 상세 조회", description = "특정 의뢰의 상세 정보를 조회 (본인 요청만 가능)")
	@ApiSuccessResponse(data = CommissionResponse.class)
	@ApiFailResponse(errorCodeClass = ImageErrorCode.class, errorCode = "ORIGINAL_REQUEST_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<CommissionResponse> getCommission(
		@Auth AuthMember member,
		@Positive @PathVariable("commissionId") Long commissionId
	);

	@Operation(summary = "AI 이미지 생성 의뢰", description = "이미지를 업로드하여 AI 변환 작업을 의뢰")
	@ApiSuccessResponse(data = CommissionResponse.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<CommissionResponse> createCommission(
		@Auth AuthMember member,
		@RequestPart("imageFile") MultipartFile imageFile,
		@RequestParam(value = "templateId") Long templateId
	) throws IOException;

	@Operation(summary = "AI 이미지 재생성 의뢰", description = "업로드된 이미지로 다시 AI 변환 작업을 의뢰")
	@ApiSuccessResponse(data = CommissionResponse.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = CommissionErrorCode.class, errorCode = "CANNOT_RECREATE_COMMISSION")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<CommissionResponse> reCreateCommission(
		@Auth AuthMember member,
		@Positive @PathVariable Long commissionId,
		@RequestParam(value = "templateId") Long templateId
	) throws IOException;

	@Operation(summary = "의뢰 상태 변경 (좋아요/싫어요)",
		description = "생성된 AI 이미지에 대한 좋아요(true), 싫어요(false), 관심없음(null) 상태를 변경합니다.")
	@ApiSuccessResponse(data = CommissionResponse.class)
	@ApiFailResponse(errorCodeClass = ImageErrorCode.class, errorCode = "ORIGINAL_REQUEST_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<CommissionResponse> updateCommissionGoodStatus(
		@Auth AuthMember member,
		@Positive @PathVariable Long commissionId
	);

	@Operation(summary = "의뢰 취소", description = "진행 중이거나 완료된 의뢰를 취소(소프트 딜리트)합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = ImageErrorCode.class, errorCode = "ORIGINAL_REQUEST_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Void> deleteCommission(
		@Auth AuthMember member,
		@Positive @PathVariable("commissionId") Long commissionId
	);
}
