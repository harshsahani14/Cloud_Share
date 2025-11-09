package com.cloudshare.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudshare.backend.dto.ProfileDTO;
import com.cloudshare.backend.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerProfile(@RequestBody ProfileDTO profileDTO){
		
		ProfileDTO savedProfile =  profileService.createProfile(profileDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
	}
	
}
