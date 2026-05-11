package com.example.backend.domain.bannedword.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.utils.bannedword.BannedWordUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/banned-words")
@RequiredArgsConstructor
public class AdminBannedWordController implements AdminBannedWordApi {

	private final BannedWordUtil bannedWordUtil;

	@Override
	@PostMapping
	public ResponseEntity<String> add(@RequestParam("word") String word) {
		if (word == null || word.isBlank()) {
			return ResponseEntity.badRequest().body("❌ 단어를 입력해주세요.");
		}

		try {
			bannedWordUtil.addWord(word);
			return ResponseEntity.ok("✅ 금지어 추가 완료: " + word);
		} catch (IllegalArgumentException e) {
			// 💡 중복 에러 발생 시 409 Conflict 반환
			return ResponseEntity.status(HttpStatus.CONFLICT).body("⚠️ " + e.getMessage());
		}
	}

	@Override
	@DeleteMapping
	public ResponseEntity<String> delete(@RequestParam("word") String word) {
		if (word == null || word.isBlank()) {
			return ResponseEntity.badRequest().body("❌ 단어를 입력해주세요.");
		}
		try {
			bannedWordUtil.deleteWord(word);
			return ResponseEntity.ok("🗑️ 금지어 삭제 완료: " + word);
		} catch (NoSuchElementException e) {
			// 💡 없는 단어 삭제 시 404 Not Found 반환
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
		}
	}
}
