package com.example.backend.domain.auth.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.auth.dto.internal.SigninResultDto;
import com.example.backend.domain.auth.dto.request.SigninRequest;
import com.example.backend.domain.auth.dto.response.SigninResponse;
import com.example.backend.domain.auth.enums.EmailVerificationType;
import com.example.backend.domain.auth.service.AuthService;
import com.example.backend.domain.member.dto.request.SignupRequest;
import com.example.backend.global.dto.Response;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

	private final AuthService authService;

	@Override
	@PostMapping("/v1/auth/signup")
	public Response<Void> signup(@Valid @RequestBody SignupRequest request) {
		authService.signup(request);
		return Response.success();
	}

	@Override
	@PostMapping("/v1/auth/signin")
	public Response<SigninResponse> signin(
		@Valid @RequestBody SigninRequest signinRequest,
		HttpServletResponse response
	) {
		SigninResultDto result = authService.signin(signinRequest);

		ResponseCookie cookie = createRefreshTokenCookie(result.tokens().refreshToken(), 12L * 60 * 60);
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return Response.success(SigninResponse.from(result));
	}

	@Override
	@PostMapping("/v1/auth/signout")
	public Response<Void> signout(
		@CookieValue(name = "refreshToken", required = false) String refreshToken,
		HttpServletResponse response
	) {
		authService.signout(refreshToken);
		ResponseCookie cookie = createRefreshTokenCookie("", 0);
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return Response.success();
	}

	@Override
	@GetMapping("/v1/auth/email/verify")
	public void verifyEmail(
		@RequestParam("code") String verificationCode,
		@RequestParam("type") EmailVerificationType type,
		HttpServletResponse response
	) throws IOException {

		response.sendRedirect(authService.verifyEmail(verificationCode, type));
	}

	private ResponseCookie createRefreshTokenCookie(String refreshToken, long maxAge) {
		return ResponseCookie.from("refreshToken", refreshToken)
			.maxAge(maxAge)
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();
	}
}
