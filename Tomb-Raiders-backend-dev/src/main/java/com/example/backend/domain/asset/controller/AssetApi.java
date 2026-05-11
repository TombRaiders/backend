package com.example.backend.domain.asset.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.asset.dto.request.CreateAssetRequest;
import com.example.backend.domain.asset.dto.response.AssetResponse;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AssetErrorCode;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Asset", description = "에셋 관리 API")
public interface AssetApi {

	@Operation(
		summary = "내 에셋 목록 조회",
		description = "로그인한 사용자가 소유한 에셋 목록을 조회. "
			+ "commission 파라미터가 true이면 AI 생성 에셋을 포함한 '전체 에셋'을 반환하고, false이면 '직접 업로드한 에셋'만 반환."
	)
	@ApiSuccessResponse(data = AssetResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Page<AssetResponse>> getAssets(
		@Auth AuthMember member,
		@RequestParam Boolean commission,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "에셋 상세 조회", description = "특정 에셋의 상세 정보를 조회. 본인 소유이거나, 접근 권한이 있는 경우에만 가능.")
	@ApiSuccessResponse(data = AssetResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AssetErrorCode.class, errorCode = {"ASSET_NOT_FOUND", "ASSET_ACCESS_DENIED"})
	Response<AssetResponse> getAsset(
		@Auth AuthMember member,
		@PathVariable Long assetId
	);

	@Operation(summary = "3D 에셋 파일 직접 업로드", description = "사용자가 보유한 3D 파일을 업로드하여 에셋으로 등록")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AssetErrorCode.class,
		errorCode = {"INVALID_EXTENSION", "FILE_SIZE_EXCEEDED", "FILE_UPLOAD_ERROR"})
	Response<AssetResponse> uploadAsset(
		@Auth AuthMember member,
		@RequestPart("file") MultipartFile file
	) throws IOException;

	@Operation(summary = "AI 에셋 생성 요청", description = "AI 서비스를 통해 새로운 에셋을 생성하고 등록")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AssetErrorCode.class, errorCode = {"FILE_UPLOAD_ERROR"})
	Response<AssetResponse> createAsset(
		@Auth AuthMember member,
		@Valid @RequestBody CreateAssetRequest request
	);

	@Operation(summary = "에셋 삭제", description = "본인이 소유한 에셋을 삭제(소프트 딜리트) 처리합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AssetErrorCode.class, errorCode = {"ASSET_NOT_FOUND", "ASSET_ACCESS_DENIED"})
	Response<Void> deleteAsset(
		@Auth AuthMember member,
		@PathVariable("assetId") Long assetId
	);

	@Operation(summary = "AI asset recreate request",
		description = "Requests AI asset regeneration for an existing AI-created asset")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AssetErrorCode.class,
		errorCode = {"ASSET_NOT_FOUND", "ASSET_ACCESS_DENIED", "CANNOT_RECREATE_ASSET", "FILE_UPLOAD_ERROR"})
	Response<AssetResponse> recreateAsset(
		@Auth AuthMember member,
		@PathVariable Long assetId,
		@RequestParam(value = "templateId") Long templateId
	);
}
