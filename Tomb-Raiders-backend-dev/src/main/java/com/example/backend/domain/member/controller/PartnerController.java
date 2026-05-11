package com.example.backend.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.member.dto.request.CreateQuotationRequest;
import com.example.backend.domain.member.dto.request.PartnerRequest;
import com.example.backend.domain.member.dto.response.CreateQuotationResponse;
import com.example.backend.domain.member.dto.response.PartnerOrderResponse;
import com.example.backend.domain.member.dto.response.PartnerResponse;
import com.example.backend.domain.member.service.partner.PartnerService;
import com.example.backend.domain.order.enums.OrderStatus;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PartnerController implements PartnerApi {

	private final PartnerService partnerService;

	@Override
	@PostMapping("/v1/partners")
	public Response<PartnerResponse> registerPartner(
		@Auth AuthMember authMember,
		@Valid @RequestBody PartnerRequest.Register request
	) {
		PartnerResponse response = partnerService.registerPartner(authMember.id(), request);
		return Response.success(response);
	}

	@Override
	@GetMapping("/v1/partners/info")
	public Response<PartnerResponse> getMyPartner(
		@Auth AuthMember authMember
	) {
		PartnerResponse response = partnerService.getMyPartner(authMember.id());
		return Response.success(response);
	}

	@Override
	@PutMapping("/v1/partners/info")
	public Response<PartnerResponse> updateMyPartner(
		@Auth AuthMember authMember,
		@Valid @RequestBody PartnerRequest.Update request
	) {
		PartnerResponse response = partnerService.updateMyPartner(authMember.id(), request);
		return Response.success(response);
	}

	@Override
	@DeleteMapping("/v1/partners/info")
	public Response<Void> deleteMyPartner(
		@Auth AuthMember authMember
	) {
		partnerService.deleteMyPartner(authMember.id());
		return Response.success();
	}

	@Override
	@GetMapping("/v1/partners")
	public Response<Page<PartnerOrderResponse>> getOrders(
		@RequestParam(required = false) OrderStatus status,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return Response.success(partnerService.getOrders(status, pageable));
	}

	@Override
	@GetMapping("/v1/partners/{orderId}")
	public Response<PartnerOrderResponse> getOrder(
		@Auth AuthMember authMember,
		@PathVariable Long orderId
	) {
		return Response.success(partnerService.getOrder(authMember.id(), orderId));
	}

	@Override
	@GetMapping("/v1/partners/selected")
	public Response<Page<PartnerOrderResponse>> getMySelectedOrders(
		@Auth AuthMember authMember,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return Response.success(partnerService.getMySelectedOrders(authMember.id(), pageable));
	}

	@Override
	@PostMapping("/v1/partners/quotations")
	public Response<CreateQuotationResponse> createQuotation(
		@Valid @RequestBody CreateQuotationRequest request,
		@Auth AuthMember authMember
	) {
		// 서비스에서 반환된 DTO를 프론트엔드로 전달
		CreateQuotationResponse response = partnerService.createQuotation(authMember.id(), request);
		return Response.success(response);
	}

	@Override
	@DeleteMapping("/v1/partners/quotations/{quotationId}")
	public Response<Void> cancelQuotation(
		@Auth AuthMember authMember,
		@PathVariable Long quotationId
	) {
		partnerService.cancelQuotation(authMember.id(), quotationId);
		return Response.success();
	}

	@Override
	@PostMapping("/v1/partners/orders/{orderId}/accept")
	public Response<Void> acceptOrder(
		@Auth AuthMember authMember,
		@PathVariable Long orderId
	) {
		partnerService.acceptOrder(authMember.id(), orderId);
		return Response.success();
	}

	@Override
	@PostMapping("/v1/partners/orders/{orderId}/reject")
	public Response<Void> rejectOrder(
		@Auth AuthMember authMember,
		@PathVariable Long orderId
	) {
		partnerService.rejectOrder(authMember.id(), orderId);
		return Response.success();
	}
}
