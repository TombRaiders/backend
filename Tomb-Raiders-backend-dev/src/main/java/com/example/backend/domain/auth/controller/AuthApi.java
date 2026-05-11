package com.example.backend.domain.auth.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.backend.domain.auth.dto.request.SigninRequest;
import com.example.backend.domain.auth.dto.response.SigninResponse;
import com.example.backend.domain.auth.enums.EmailVerificationType;
import com.example.backend.domain.member.dto.request.SignupRequest;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthErrorCode;
import com.example.backend.global.errorcode.MemberErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * 인증 관련 API 인터페이스
 * <p>
 * 로그인, 로그아웃, 토큰 재발급 등 사용자의 신원 확인과 관련된 엔드포인트를 제공
 * </p>
 */
@Tag(name = "Auth", description = "인증/인가 관련 API")
public interface AuthApi {

	@Operation(summary = "회원가입", description = "신규 회원 정보를 저장하고 상태를 PENDING으로 설정한 후 인증 메일 전송")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "DUPLICATE_EMAIL")
	Response<Void> signup(@Valid @RequestBody SignupRequest request);

	@Operation(summary = "로그인", description = "로그인 아이디와 비밀번호를 사용하여 로그인")
	@ApiSuccessResponse(data = SigninResponse.class)
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class, errorCode = "MEMBER_NOT_FOUND")
	@ApiFailResponse(errorCodeClass = AuthErrorCode.class,
		errorCode = {"ACCOUNT_DISABLED", "ACCOUNT_LOCKED", "INVALID_PASSWORD"})
	Response<SigninResponse> signin(
		@Valid @RequestBody SigninRequest signinRequest,
		HttpServletResponse response
	);

	@Operation(summary = "로그아웃", description = "사용자의 Refresh Token을 Redis에서 삭제하고, 클라이언트 쿠키를 만료시킴")
	@ApiSuccessResponse()
	Response<Void> signout(
		@CookieValue(name = "refreshToken", required = false) String refreshToken,
		HttpServletResponse response
	);

	@Operation(summary = "이메일 인증 확인", description = "메일로 발송된 코드를 검증하고 완료 후 리다이렉트 수행")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = MemberErrorCode.class,
		errorCode = {"INVALID_SIGNUP_VERIFICATION_CODE", "INVALID_PASSWORD_RESET_VERIFICATION_CODE",
			"INVALID_EMAIL_VERIFICATION_TYPE", "MEMBER_NOT_FOUND"})
	void verifyEmail(
		@RequestParam("code") String verificationCode,
		@RequestParam("type") EmailVerificationType type,
		HttpServletResponse response
	) throws IOException;
}
