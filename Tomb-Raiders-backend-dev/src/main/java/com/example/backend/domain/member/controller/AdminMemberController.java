package com.example.backend.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.member.dto.response.MemberResponse;
import com.example.backend.domain.member.enums.Role;
import com.example.backend.domain.member.service.AdminMemberService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminMemberController implements AdminMemberApi {
	private final AdminMemberService adminMemberService;

	@Override
	@GetMapping("/admin/v1/admins")
	public Response<Page<MemberResponse>> getAdmins(Pageable pageable) {
		Page<MemberResponse> responsePage = adminMemberService.memberFindByRoleIn(pageable);
		return Response.success(responsePage);
	}

	@Override
	@PatchMapping("/admin/v1/members/{targetMemberId}/role")
	public Response<Void> changeMemberRole(
		@Auth AuthMember operator,
		@PathVariable("targetMemberId") Long targetMemberId,
		@RequestParam("newRole") Role newRole
	) {
		adminMemberService.changeMemberRole(operator, targetMemberId, newRole);
		return Response.success();
	}

	/* =========================================================================
	 * 3. 특정 회원 조회 (권한 부여 목적 등)
	 * ========================================================================= */
	@Override
	@GetMapping("/admin/v1/members/{memberId}")
	public Response<MemberResponse> getMemberById(@PathVariable Long memberId) {

		MemberResponse response = adminMemberService.getMemberById(memberId);
		return Response.success(response);
	}

	@Override
	@GetMapping("/admin/v1/members/search")
	public Response<MemberResponse> getMemberByLoginId(@RequestParam String loginId) {

		// /admin/v1/members/search?loginId=아이디 형식으로 호출
		MemberResponse response = adminMemberService.getMemberByLoginId(loginId);
		return Response.success(response);
	}

	@Override
	@GetMapping("/admin/v1/members")
	public Response<Page<MemberResponse>> getAllMembers(Pageable pageable) {

		Page<MemberResponse> members = adminMemberService.getAllMembers(pageable);
		return Response.success(members);
	}
}
