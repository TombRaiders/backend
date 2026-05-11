package com.example.backend.domain.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.member.dto.request.MemberBanRequest;
import com.example.backend.domain.member.dto.request.MemberUnbanRequest;
import com.example.backend.domain.member.dto.response.MemberResponse;
import com.example.backend.domain.member.service.MemberBanService;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberBanController implements MemberBanApi {

	private final MemberBanService memberBanService;

	@Override
	@PostMapping("/v1/admin/members/ban")
	public Response<Void> banMember(@Valid @RequestBody MemberBanRequest request) {
		memberBanService.banMember(request);
		return Response.success();
	}

	@Override
	@PostMapping("/v1/admin/members/unban")
	public Response<Void> unbanMember(@Valid @RequestBody MemberUnbanRequest request) {
		memberBanService.unbanMember(request);
		return Response.success();
	}

	@Override
	@GetMapping("/v1/admin/members/banned")
	public Response<List<MemberResponse>> getBannedMembers() {
		List<MemberResponse> bannedMembers = memberBanService.getBannedMembers();
		return Response.success(bannedMembers);
	}
}
