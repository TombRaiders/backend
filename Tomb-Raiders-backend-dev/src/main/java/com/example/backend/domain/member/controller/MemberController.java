package com.example.backend.domain.member.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.member.dto.request.MemberRequest;
import com.example.backend.domain.member.dto.request.PasswordChangeRequest;
import com.example.backend.domain.member.dto.request.PasswordResetConfirmRequest;
import com.example.backend.domain.member.dto.request.PasswordResetRequest;
import com.example.backend.domain.member.dto.request.WithdrawRequest;
import com.example.backend.domain.member.dto.response.MemberResponse;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController implements MemberApi {

	private final MemberService memberService;

	@Override
	@PostMapping("/v1/member/password-reset/request")
	public Response<Void> passwordReset(@Valid @RequestBody PasswordResetRequest request) {
		memberService.requestPasswordReset(request);
		return Response.success();
	}

	@Override
	@PostMapping("/v1/member/password-reset/confirm")
	public Response<Void> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
		memberService.confirmPasswordReset(request);
		return Response.success();
	}

	@Override
	@PostMapping("/v1/member/password/change")
	public Response<Void> changePassword(
		@Auth AuthMember member,
		@Valid @RequestBody PasswordChangeRequest request
	) {
		memberService.changePassword(member, request);
		return Response.success();
	}

	@Override
	@PostMapping("/v1/member/withdraw")
	public Response<Void> withdraw(
		@Auth AuthMember member,
		@Valid @RequestBody WithdrawRequest request
	) {
		memberService.withdraw(member, request);
		return Response.success();
	}

	@Override
	@PutMapping("/v1/member/introduce")
	public Response<Void> updateIntroduce(
		@Auth AuthMember member,
		@Valid @RequestBody MemberRequest.UpdateIntroduce request
	) {
		memberService.updateIntroduce(member, request);
		return Response.success();
	}

	@Override
	@PutMapping("/v1/member/nickname")
	public Response<Void> updateNickname(
		@Auth AuthMember member,
		@Valid @RequestBody MemberRequest.UpdateNickname request
	) {
		memberService.updateNickname(member, request);
		return Response.success();
	}

	@Override
	@PutMapping(value = "/v1/member/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<Void> updateProfileImage(
		@Auth AuthMember member,
		@RequestPart(value = "image", required = false) MultipartFile image
	) {
		memberService.updateProfileImage(member, image);
		return Response.success();
	}

	@Override
	@GetMapping("/v1/member/me")
	public Response<MemberResponse> getMyInfo(@Auth AuthMember member) {
		return Response.success(memberService.getMyInfo(member));
	}
}
