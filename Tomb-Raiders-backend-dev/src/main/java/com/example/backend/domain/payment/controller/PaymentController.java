package com.example.backend.domain.payment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.payment.dto.request.PaymentCancelRequest;
import com.example.backend.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.backend.domain.payment.dto.request.PaymentPrepareRequest;
import com.example.backend.domain.payment.dto.response.PaymentDetailResponse;
import com.example.backend.domain.payment.dto.response.PaymentPrepareResponse;
import com.example.backend.domain.payment.dto.response.PaymentResponse;
import com.example.backend.domain.payment.dto.response.PaymentSimpleResponse;
import com.example.backend.domain.payment.service.PaymentService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.OrderErrorCode;
import com.example.backend.global.errorcode.PaymentErrorCode;
import com.example.backend.global.errorcode.QuotationErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Payments", description = "결제 사전 등록, 승인 및 이력 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

	private final PaymentService paymentService;

	@Operation(summary = "결제 사전 등록", description = "채택할 견적서 ID를 받아 서버에 결제 대기 정보를 생성하고, PG사 메타데이터를 반환")
	@ApiSuccessResponse(data = PaymentPrepareResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = QuotationErrorCode.class, errorCode = "QUOTATION_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = {"UNAUTHORIZED_ORDER_ACCESS",
		"ALREADY_PROCESSED_ORDER"})
	@ApiFailResponse(errorCodeClass = PaymentErrorCode.class, errorCode = "ALREADY_PAID")
	@PostMapping("/v1/payments/prepare")
	public Response<PaymentPrepareResponse> preparePayment(
		@Auth AuthMember authMember,
		@RequestBody @Valid PaymentPrepareRequest request
	) {
		return Response.success(paymentService.preparePayment(authMember.id(), request));
	}

	@Operation(summary = "결제 최종 승인", description = "PG사 1차 인증을 마친 결제 건에 대해 금액 위변조를 검증하고 최종 승인 처리를 수행")
	@ApiSuccessResponse(data = PaymentResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = QuotationErrorCode.class, errorCode = "QUOTATION_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = PaymentErrorCode.class,
		errorCode = {"PAYMENT_NOT_FOUND", "PAYMENT_ACCESS_DENIED", "INVALID_PAYMENT_REQUEST",
			"PAYMENT_AMOUNT_MISMATCH", "PAYMENT_CANCEL_FAILED", "PAYMENT_SYNC_FAILED"})
	@PostMapping("/v1/payments/confirm")
	public Response<PaymentResponse> confirmPayment(
		@Auth AuthMember authMember,
		@RequestBody @Valid PaymentConfirmRequest request
	) {
		return Response.success(paymentService.confirmPayment(authMember.id(), request));
	}

	@Operation(summary = "결제 취소", description = "결제를 취소(환불) 처리")
	@ApiSuccessResponse(data = PaymentResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = PaymentErrorCode.class,
		errorCode = {"PAYMENT_NOT_FOUND", "PAYMENT_ACCESS_DENIED", "INVALID_PAYMENT_REQUEST"})
	@PostMapping("/v1/payments/{paymentUid}/cancel")
	public Response<PaymentResponse> cancelPayment(
		@Auth AuthMember authMember,
		@PathVariable String paymentUid,
		@RequestBody @Valid PaymentCancelRequest request
	) {
		return Response.success(paymentService.cancelPayment(authMember.id(), paymentUid, request));
	}

	@Operation(summary = "결제 이력 단건 조회", description = "결제 ID를 통해 상세 결제 정보를 조회")
	@ApiSuccessResponse(data = PaymentDetailResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = PaymentErrorCode.class, errorCode = {"PAYMENT_NOT_FOUND",
		"PAYMENT_ACCESS_DENIED"})
	@GetMapping("/v1/payments/{paymentId}")
	public Response<PaymentDetailResponse> getPayment(
		@Auth AuthMember authMember,
		@PathVariable Long paymentId
	) {
		return Response.success(paymentService.getPayment(authMember.id(), paymentId));
	}

	@Operation(summary = "결제 내역 다건 조회", description = "요청 회원의 결제 내역 목록을 조회")
	@ApiSuccessResponse(data = PaymentSimpleResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@GetMapping("/v1/payments")
	public Response<Page<PaymentSimpleResponse>> getPayments(
		@Auth AuthMember authMember,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return Response.success(paymentService.getPayments(authMember.id(), pageable));
	}
}
