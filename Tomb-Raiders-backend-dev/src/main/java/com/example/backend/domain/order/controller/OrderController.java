package com.example.backend.domain.order.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.order.dto.request.CreateOrderRequest;
import com.example.backend.domain.order.dto.request.UpdateOrderRequest;
import com.example.backend.domain.order.dto.response.CreateOrderResponse;
import com.example.backend.domain.order.dto.response.OrderResponse;
import com.example.backend.domain.order.enums.OrderStatus;
import com.example.backend.domain.order.service.OrderService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController implements OrderApi {

	private final OrderService orderService;

	@Override
	@PostMapping("/v1/order")
	public Response<CreateOrderResponse> createOrder(
		@Auth AuthMember member,
		@Valid @RequestBody CreateOrderRequest request
	) {
		return Response.success(orderService.createOrder(request, member.id(), member.role()));
	}

	@Override
	@DeleteMapping("v1/order/{orderId}")
	public Response<Void> cancelOrder(
		@Auth AuthMember member,
		@PathVariable("orderId") Long orderId
	) {
		orderService.cancelOrder(orderId, member.id(), member.role());
		return Response.success();
	}

	@Override
	@GetMapping({"/v1/order"})
	public Response<Page<OrderResponse>> getOrders(
		@Auth AuthMember member,
		@RequestParam(required = false) OrderStatus orderStatus,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return Response.success(orderService.getOrdersByMember(member.id(), member.role(), orderStatus, pageable));
	}

	@Override
	@GetMapping("/v1/order/{orderId}")
	public Response<OrderResponse> getOrder(
		@Auth AuthMember member,
		@PathVariable("orderId") Long orderId
	) {
		return Response.success(orderService.getOrderByMember(orderId, member.id(), member.role()));
	}

	@Override
	@GetMapping("/v1/order/asset/{assetId}")
	public Response<OrderResponse> getOrderByAsset(
		@Auth AuthMember member,
		@PathVariable("assetId") Long assetId
	) {
		return Response.success(orderService.getOrderByAsset(assetId, member.id(), member.role()));
	}

	@Override
	@PatchMapping("/v1/order/{orderId}")
	public Response<Void> updateOrderStatus(
		@PathVariable("orderId") Long orderId,
		@RequestParam(name = "status") @Min(1) @Max(8) int statusCode
	) {
		orderService.updateOrderStatus(orderId, statusCode);
		return Response.success();
	}

	@Override
	@PutMapping("/v1/order/{orderId}")
	public Response<Void> updateOrder(
		@Auth AuthMember member,
		@PathVariable("orderId") Long orderId,
		@Valid @RequestBody UpdateOrderRequest request
	) {
		orderService.updateOrder(orderId, request, member.id(), member.role());
		return Response.success();
	}
}
