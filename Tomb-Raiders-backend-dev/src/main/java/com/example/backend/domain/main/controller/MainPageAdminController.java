package com.example.backend.domain.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.bulletinboard.dto.request.BulletinBoardRequest;
import com.example.backend.domain.main.dto.request.CreateMainPageImageRequest;
import com.example.backend.domain.main.dto.request.UpdateMainPageImageRequest;
import com.example.backend.domain.main.dto.response.MainPageImageResponse;
import com.example.backend.domain.main.service.MainBulletinBoardService;
import com.example.backend.domain.main.service.MainPageService;
import com.example.backend.domain.member.enums.Role;
import com.example.backend.global.annotation.permission.Permission;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Permission(roles = Role.ADMIN)
public class MainPageAdminController implements MainPageAdminApi {

	private final MainPageService mainPageService;
	private final MainBulletinBoardService mainBulletinBoardService;

	@Override
	@GetMapping("/admin/v1/main/rail-images")
	public Response<List<MainPageImageResponse>> getRailImages() {
		return Response.success(mainPageService.getRailImages());
	}

	@Override
	@PostMapping(value = "/admin/v1/main/rail-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<MainPageImageResponse> createRailImage(
		@Valid @ModelAttribute CreateMainPageImageRequest request
	) throws IOException {
		return Response.success(mainPageService.createRailImage(request));
	}

	@Override
	@PatchMapping(value = "/admin/v1/main/rail-images/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<MainPageImageResponse> updateRailImage(
		@Positive @PathVariable Long imageId,
		@Valid @ModelAttribute UpdateMainPageImageRequest request
	) throws IOException {
		return Response.success(mainPageService.updateRailImage(imageId, request));
	}

	@Override
	@DeleteMapping("/admin/v1/main/rail-images/{imageId}")
	public Response<Void> deleteRailImage(
		@Positive @PathVariable Long imageId
	) {
		mainPageService.deleteRailImage(imageId);
		return Response.success();
	}

	@Override
	@Permission(roles = {Role.ADMIN, Role.BULLETINBOARD_ADMIN})
	@PutMapping("/admin/v1/main/bulletin-boards/pinpoints")
	public Response<Void> updatePinpoints(
		@Valid @RequestBody BulletinBoardRequest.UpdatePinpoints request
	) {
		mainBulletinBoardService.updatePinpoints(request);
		return Response.success();
	}
}
