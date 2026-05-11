package com.example.backend.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.member.dto.response.PartnerResponse;
import com.example.backend.domain.member.service.partner.PartnerService;
import com.example.backend.global.dto.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminPartnerController implements AdminPartnerApi {

	private final PartnerService partnerService;

	@Override
	@GetMapping("/admin/v1/partners/pending")
	public Response<Page<PartnerResponse>> getPendingPartners(Pageable pageable) {
		Page<PartnerResponse> responses = partnerService.getPendingPartners(pageable);
		return Response.success(responses);
	}

	@Override
	@PatchMapping("/admin/v1/partners/{partnerId}/approve")
	public Response<Void> approvePartner(@PathVariable("partnerId") Long partnerId) {
		partnerService.approvePartner(partnerId);
		return Response.success();
	}

	@Override
	@DeleteMapping("/admin/v1/partners/{partnerId}/reject")
	public Response<Void> rejectPartner(@PathVariable("partnerId") Long partnerId) {
		partnerService.rejectPartner(partnerId);
		return Response.success();
	}

	@Override
	@DeleteMapping("/admin/v1/partners/{partnerId}")
	public Response<Void> deletePartner(@PathVariable("partnerId") Long partnerId) {
		partnerService.deletePartner(partnerId);
		return Response.success();
	}

	@Override
	@GetMapping("/admin/v1/partners")
	public Response<Page<PartnerResponse>> getAllPartners(Pageable pageable) {
		Page<PartnerResponse> responses = partnerService.getAllPartners(pageable);
		return Response.success(responses);
	}

	@Override
	@GetMapping("/admin/v1/partners/{partnerId}")
	public Response<PartnerResponse> getPartner(@PathVariable("partnerId") Long partnerId) {
		PartnerResponse response = partnerService.getPartner(partnerId);
		return Response.success(response);
	}
}
