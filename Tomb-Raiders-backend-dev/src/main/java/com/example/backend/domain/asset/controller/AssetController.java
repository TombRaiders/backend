package com.example.backend.domain.asset.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.asset.dto.request.CreateAssetRequest;
import com.example.backend.domain.asset.dto.request.UploadAssetRequest;
import com.example.backend.domain.asset.dto.response.AssetResponse;
import com.example.backend.domain.asset.service.AssetService;
import com.example.backend.global.annotation.Auth;
import com.example.backend.global.dto.AuthMember;
import com.example.backend.global.dto.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssetController implements AssetApi {

	private final AssetService assetService;

	@Override
	@GetMapping("/v1/asset")
	public Response<Page<AssetResponse>> getAssets(
		@Auth AuthMember member,
		@RequestParam Boolean commission,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return Response.success(assetService.getAssets(member.id(), commission, pageable));
	}

	@Override
	@GetMapping("/v1/asset/{assetId}")
	public Response<AssetResponse> getAsset(
		@Auth AuthMember member, @PathVariable Long assetId) {
		return Response.success(assetService.getAsset(member.id(), assetId));
	}

	@Override
	@PostMapping(value = "/v1/asset/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<AssetResponse> uploadAsset(
		@Auth AuthMember member,
		@RequestPart("file") MultipartFile file) throws IOException {
		return Response.success(assetService.uploadAsset(member.id(), new UploadAssetRequest(file)));
	}

	@Override
	@PostMapping(value = "/v1/asset/create")
	public Response<AssetResponse> createAsset(
		@Auth AuthMember member,
		@Valid @RequestBody CreateAssetRequest request) {
		return Response.success(assetService.createAsset(member.id(), request));
	}

	@Override
	@PostMapping(value = "/v1/asset/{assetId}")
	public Response<AssetResponse> recreateAsset(
		@Auth AuthMember member,
		@PathVariable Long assetId,
		@RequestParam(value = "templateId") Long templateId) {
		return Response.success(assetService.recreateAsset(member.id(), assetId, templateId));
	}

	@Override
	@DeleteMapping("/v1/asset/{assetId}")
	public Response<Void> deleteAsset(
		@Auth AuthMember member, @PathVariable("assetId") Long assetId) {
		assetService.deleteAsset(member.id(), assetId);
		return Response.success();
	}
}
