package com.example.backend.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.backend.domain.member.dto.request.CreateQuotationRequest;
import com.example.backend.domain.member.dto.request.PartnerRequest;
import com.example.backend.domain.member.dto.response.CreateQuotationResponse;
import com.example.backend.domain.member.dto.response.PartnerOrderResponse;
import com.example.backend.domain.member.dto.response.PartnerResponse;
import com.example.backend.domain.member.enums.Role;
import com.example.backend.domain.order.enums.OrderStatus;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.annotation.permission.Permission;
import com.example.backend.global.annotation.permission.RequiresPartner;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthErrorCode;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.OrderErrorCode;
import com.example.backend.global.errorcode.PartnerErrorCode;
import com.example.backend.global.errorcode.QuotationErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Partner", description = "파트너 전용 비즈니스 API")
public interface PartnerApi {

	@Operation(summary = "파트너 등록", description = "일반 회원이 파트너 정보를 등록하여 승인 대기 상태가 됨")
	@ApiSuccessResponse(data = PartnerResponse.class)
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class,
		errorCode = {"DUPLICATE_PARTNER_REGISTER", "INVALID_PARTNER_NAME", "INVALID_PARTNER_CONTACT",
			"INVALID_PARTNER_LOCATION", "INVALID_PARTNER_INTRODUCE"})
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = {"UNAUTHENTICATED_ACCESS", "ACCESS_DENIED"})
	@Permission(roles = Role.MEMBER)
	Response<PartnerResponse> registerPartner(
		@Parameter(hidden = true) @Auth AuthMember authMember,
		@Valid @RequestBody PartnerRequest.Register request
	);

	@Operation(summary = "내 파트너 정보 조회", description = "로그인한 회원의 파트너 상세 정보를 조회")
	@ApiSuccessResponse(data = PartnerResponse.class)
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = "UNAUTHENTICATED_ACCESS")
	@Permission(roles = {Role.MEMBER, Role.PARTNER, Role.ADMIN, Role.PARTNER_ADMIN})
	Response<PartnerResponse> getMyPartner(
		@Parameter(hidden = true) @Auth AuthMember authMember
	);

	@Operation(summary = "내 파트너 정보 수정", description = "로그인한 회원의 파트너 정보(이름, 소개, 연락처, 지역)를 수정")
	@ApiSuccessResponse(data = PartnerResponse.class)
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class,
		errorCode = {"PARTNER_NOT_FOUND", "INVALID_PARTNER_NAME", "INVALID_PARTNER_CONTACT", "INVALID_PARTNER_LOCATION",
			"INVALID_PARTNER_INTRODUCE"})
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = "UNAUTHENTICATED_ACCESS")
	@Permission(roles = {Role.MEMBER, Role.PARTNER, Role.ADMIN, Role.PARTNER_ADMIN})
	Response<PartnerResponse> updateMyPartner(
		@Parameter(hidden = true) @Auth AuthMember authMember,
		@Valid @RequestBody PartnerRequest.Update request
	);

	@Deprecated(forRemoval = true)
	@Operation(summary = "파트너 탈퇴(삭제)", description = "파트너 등록을 취소하고 정보를 삭제함")
	@ApiSuccessResponse
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class, errorCode = "UNAUTHENTICATED_ACCESS")
	@Permission(roles = {Role.MEMBER, Role.PARTNER, Role.ADMIN, Role.PARTNER_ADMIN})
	Response<Void> deleteMyPartner(
		@Parameter(hidden = true) @Auth AuthMember authMember
	);

	@Operation(summary = "전체 주문 조회", description = "공개된 의뢰 목록 전체를 조회")
	@RequiresPartner
	@ApiSuccessResponse(data = PartnerOrderResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Page<PartnerOrderResponse>> getOrders(
		@RequestParam(required = false) OrderStatus status,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "특정 주문 상세 조회", description = "특정 의뢰의 상세 요구사항 및 정보를 조회")
	@RequiresPartner
	@ApiSuccessResponse(data = PartnerOrderResponse.class)
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = "ORDER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = "UNAUTHORIZED_ORDER_ACCESS")
	Response<PartnerOrderResponse> getOrder(
		@Parameter(hidden = true) @Auth AuthMember authMember,
		@PathVariable Long orderId
	);

	@Operation(summary = "내 작업 목록 조회", description = "파트너가 견적을 제출하여 채택된 모든 주문 목록을 조회")
	@RequiresPartner
	@ApiSuccessResponse(data = PartnerOrderResponse.class)
	Response<Page<PartnerOrderResponse>> getMySelectedOrders(
		@Parameter(hidden = true) @Auth AuthMember authMember,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "견적서 작성", description = "의뢰에 대한 견적서를 작성하여 제출")
	@RequiresPartner
	@ApiSuccessResponse(data = CreateQuotationResponse.class)
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class,
		errorCode = {"ORDER_NOT_FOUND", "ALREADY_PROCESSED_ORDER", "ORDER_MODIFIED_BEFORE_QUOTE"})
	Response<CreateQuotationResponse> createQuotation(
		@RequestBody @Valid CreateQuotationRequest request,
		@Parameter(hidden = true) @Auth AuthMember authMember
	);

	@Operation(summary = "견적서 취소", description = "파트너가 본인이 제출한 결제 전 견적서를 소프트 삭제합니다.")
	@RequiresPartner
	@ApiSuccessResponse
	@ApiFailResponse(errorCodeClass = QuotationErrorCode.class,
		errorCode = {"QUOTATION_NOT_FOUND", "UNAUTHORIZED_QUOTATION_ACCESS", "CANNOT_CANCEL_QUOTATION"})
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	Response<Void> cancelQuotation(
		@Parameter(hidden = true) @Auth AuthMember authMember,
		@PathVariable Long quotationId
	);

	@Operation(summary = "주문 수락", description = "파트너가 대기 중인 의뢰를 수락하여 결제 대기(견적 완료) 상태로 변경합니다.")
	@RequiresPartner
	@ApiSuccessResponse
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = {"ORDER_NOT_FOUND", "ALREADY_PROCESSED_ORDER"})
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	Response<Void> acceptOrder(
		@Parameter(hidden = true) @Auth AuthMember authMember,
		@PathVariable Long orderId
	);

	@Operation(summary = "주문 거절", description = "파트너가 대기 중인 의뢰를 거절하여 취소 상태로 변경합니다.")
	@RequiresPartner
	@ApiSuccessResponse
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = {"ORDER_NOT_FOUND", "ALREADY_PROCESSED_ORDER"})
	@ApiFailResponse(errorCodeClass = PartnerErrorCode.class, errorCode = "PARTNER_NOT_FOUND")
	Response<Void> rejectOrder(
		@Parameter(hidden = true) @Auth AuthMember authMember,
		@PathVariable Long orderId
	);
}
