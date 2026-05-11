package com.example.backend.domain.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.member.dto.request.AddressCreateRequest;
import com.example.backend.domain.member.dto.request.AddressUpdateRequest;
import com.example.backend.domain.member.dto.response.AddressResponse;
import com.example.backend.domain.member.service.address.AddressService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController implements AddressApi {

	private final AddressService addressService;

	@Override
	@PostMapping("/v1/member/address")
	public Response<Void> createAddress(
		@Auth AuthMember authMember,
		@Valid @RequestBody AddressCreateRequest request
	) {
		addressService.createAddress(authMember.id(), request);
		return Response.success();
	}

	@Override
	@PatchMapping("/v1/member/address/{addressId}")
	public Response<Void> updateAddress(
		@Auth AuthMember authMember,
		@PathVariable("addressId") Long addressId,
		@Valid @RequestBody AddressUpdateRequest request
	) {
		addressService.updateAddress(authMember.id(), addressId, request);
		return Response.success();
	}

	@Override
	@GetMapping("/v1/member/address/{addressId}")
	public Response<AddressResponse> getAddress(
		@Auth AuthMember authMember,
		@PathVariable("addressId") Long addressId
	) {
		AddressResponse response = addressService.getAddress(authMember.id(), addressId);
		return Response.success(response);
	}

	@Override
	@GetMapping("/v1/member/address")
	public Response<List<AddressResponse>> getAddressList(
		@Auth AuthMember authMember
	) {
		List<AddressResponse> responses = addressService.getAddressList(authMember.id());
		return Response.success(responses);
	}

	@Override
	@DeleteMapping("/v1/member/address/{addressId}")
	public Response<Void> deleteAddress(
		@Auth AuthMember authMember,
		@PathVariable("addressId") Long addressId
	) {
		addressService.deleteAddress(authMember.id(), addressId);
		return Response.success();
	}
}
