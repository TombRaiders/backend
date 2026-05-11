package com.example.backend.domain.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.domain.member.dto.request.AddressCreateRequest;
import com.example.backend.domain.member.dto.request.AddressUpdateRequest;
import com.example.backend.domain.member.dto.response.AddressResponse;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AddressErrorCode;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Address", description = "회원 배송지 관리 API")
public interface AddressApi {

	@Operation(summary = "배송지 생성", description = "회원의 새로운 배송지를 등록")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	Response<Void> createAddress(
		@Auth AuthMember authMember,
		@Valid @RequestBody AddressCreateRequest request
	);

	@Operation(summary = "배송지 수정", description = "기존 배송지 정보를 수정")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AddressErrorCode.class, errorCode = {"ADDRESS_NOT_FOUND",
		"ADDRESS_ACCESS_DENIED", "EMPTY_UPDATE_REQUEST"})
	Response<Void> updateAddress(
		@Auth AuthMember authMember,
		@PathVariable("addressId") Long addressId,
		@Valid @RequestBody AddressUpdateRequest request
	);

	@Operation(summary = "배송지 단건 조회", description = "특정 배송지의 상세 정보를 조회")
	@ApiSuccessResponse(data = AddressResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AddressErrorCode.class, errorCode = {"ADDRESS_NOT_FOUND",
		"ADDRESS_ACCESS_DENIED"})
	Response<AddressResponse> getAddress(
		@Auth AuthMember authMember,
		@PathVariable("addressId") Long addressId
	);

	@Operation(summary = "배송지 목록 조회", description = "회원이 등록한 모든 배송지 목록을 조회")
	@ApiSuccessResponse(data = AddressResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<List<AddressResponse>> getAddressList(
		@Auth AuthMember authMember
	);

	@Operation(summary = "배송지 삭제", description = "특정 배송지를 삭제")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AddressErrorCode.class, errorCode = {"ADDRESS_NOT_FOUND",
		"ADDRESS_ACCESS_DENIED"})
	Response<Void> deleteAddress(
		@Auth AuthMember authMember,
		@PathVariable("addressId") Long addressId
	);
}
