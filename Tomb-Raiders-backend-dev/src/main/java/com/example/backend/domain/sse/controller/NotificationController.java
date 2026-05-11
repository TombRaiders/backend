package com.example.backend.domain.sse.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.sse.service.NotificationService;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

	private final NotificationService notificationService;

	@Override
	public Response<Void> readNotification(
		AuthMember authMember,
		Long notificationId
	) {
		notificationService.readNotification(notificationId, authMember.id());
		return Response.success();
	}

	@Override
	public Response<Void> readAllNotifications(AuthMember authMember) {
		notificationService.readAllNotifications(authMember.id());
		return Response.success();
	}
}
