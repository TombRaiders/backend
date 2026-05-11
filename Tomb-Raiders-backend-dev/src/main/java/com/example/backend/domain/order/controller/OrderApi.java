package com.example.backend.domain.order.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.backend.domain.order.dto.request.CreateOrderRequest;
import com.example.backend.domain.order.dto.request.UpdateOrderRequest;
import com.example.backend.domain.order.dto.response.CreateOrderResponse;
import com.example.backend.domain.order.dto.response.OrderResponse;
import com.example.backend.domain.order.enums.OrderStatus;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.annotation.permission.RequiresPartner;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AddressErrorCode;
import com.example.backend.global.errorcode.AssetErrorCode;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.errorcode.OrderErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Tag(name = "Order", description = "주문/결제 관련 API")
public interface OrderApi {

	@Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
	@ApiSuccessResponse(data = CreateOrderResponse.class, status = "201")
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AssetErrorCode.class, errorCode = {"ASSET_NOT_FOUND", "ASSET_ACCESS_DENIED"})
	@ApiFailResponse(errorCodeClass = AddressErrorCode.class,
		errorCode = {"ADDRESS_NOT_FOUND", "ADDRESS_ACCESS_DENIED"})
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = "ORDER_OWNER_MISMATCH")
	Response<CreateOrderResponse> createOrder(
		@Auth AuthMember member,
		@Valid @RequestBody CreateOrderRequest request
	);

	@Operation(summary = "주문 취소", description = "주문을 취소하고 필요 시 환불")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class,
		errorCode = {"ORDER_NOT_FOUND", "UNAUTHORIZED_ORDER_ACCESS", "CANNOT_CANCEL_ORDER", "ALREADY_CANCELED"})
	Response<Void> cancelOrder(
		@Auth AuthMember member,
		@PathVariable("orderId") Long orderId
	);

	@Operation(summary = "내 주문 내역 조회", description = "로그인한 회원의 모든 주문 내역을 리스트로 조회")
	@ApiSuccessResponse(data = OrderResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	Response<Page<OrderResponse>> getOrders(
		@Auth AuthMember member,
		@RequestParam(required = false) OrderStatus orderStatus,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	);

	@Operation(summary = "주문 상세 조회", description = "주문 ID를 통해 상세 정보를 조회")
	@ApiSuccessResponse(data = OrderResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class,
		errorCode = {"ORDER_NOT_FOUND", "UNAUTHORIZED_ORDER_ACCESS"})
	Response<OrderResponse> getOrder(
		@Auth AuthMember member,
		@PathVariable("orderId") Long orderId
	);

	@Operation(summary = "asset ID로 주문 조회", description = "asset ID를 통해 연결된 주문 상세 정보를 조회합니다.")
	@ApiSuccessResponse(data = OrderResponse.class)
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class,
		errorCode = {"ORDER_NOT_FOUND", "UNAUTHORIZED_ORDER_ACCESS"})
	Response<OrderResponse> getOrderByAsset(
		@Auth AuthMember member,
		@PathVariable("assetId") Long assetId
	);

	@Operation(summary = "주문 상태 수정", description = "상태 코드를 사용하여 주문의 상태를 변경")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = "ORDER_NOT_FOUND")
	@RequiresPartner
	Response<Void> updateOrderStatus(
		@PathVariable("orderId") Long orderId,
		@RequestParam(name = "status") @Min(1) @Max(8) int statusCode
	);

	@Operation(summary = "주문 정보 수정", description = "주문의 배송지와 요청사항을 수정합니다.")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = OrderErrorCode.class, errorCode = {"ORDER_NOT_FOUND",
		"UNAUTHORIZED_ORDER_ACCESS"})
	Response<Void> updateOrder(
		@Auth AuthMember member,
		@PathVariable("orderId") Long orderId,
		@Valid @RequestBody UpdateOrderRequest request
	);
}
