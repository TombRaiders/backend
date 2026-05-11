package com.example.backend.domain.sse.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;
import com.example.backend.global.errorcode.AuthMemberErrorCode;
import com.example.backend.global.errorcode.NotificationErrorCode;
import com.example.backend.global.swagger.ApiFailResponse;
import com.example.backend.global.swagger.ApiSuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notification", description = "알림 관리 API")
@RequestMapping("/api")
public interface NotificationApi {

	@Operation(summary = "알림 단건 읽음 처리", description = "특정 알림 하나를 읽음 상태로 변경")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = NotificationErrorCode.class,
		errorCode = {"NOTIFICATION_NOT_FOUND", "NOTIFICATION_ACCESS_DENIED"})
	@PatchMapping("/v1/notification/{notificationId}/read")
	Response<Void> readNotification(
		@Auth AuthMember authMember,
		@PathVariable("notificationId") Long notificationId
	);

	@Operation(summary = "알림 모두 읽음 처리", description = "사용자의 읽지 않은 모든 알림을 읽음 상태로 일괄 변경")
	@ApiSuccessResponse()
	@ApiFailResponse(errorCodeClass = AuthMemberErrorCode.class)
	@ApiFailResponse(errorCodeClass = NotificationErrorCode.class,
		errorCode = {"NOTIFICATION_NOT_FOUND", "NOTIFICATION_ACCESS_DENIED"})
	@PatchMapping("/v1/notification/read")
	Response<Void> readAllNotifications(@Auth AuthMember authMember);
}
