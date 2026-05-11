package com.example.backend.domain.main.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.bulletinboard.dto.response.BulletinBoardResponse;
import com.example.backend.domain.main.dto.response.MainPageImageResponse;
import com.example.backend.domain.main.service.MainBulletinBoardService;
import com.example.backend.domain.main.service.MainPageService;
import com.example.backend.global.dto.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainPageController implements MainPageApi {

	private final MainPageService mainPageService;
	private final MainBulletinBoardService mainBulletinBoardService;

	@Override
	@GetMapping("/v1/main/rail-images")
	public Response<List<MainPageImageResponse>> getRailImages() {
		return Response.success(mainPageService.getRailImages());
	}

	@Override
	@GetMapping("/v1/main/bulletin-boards")
	public Response<List<BulletinBoardResponse.MainPreview>> getMainBoards() {
		return Response.success(mainBulletinBoardService.getMainBoards());
	}
}
