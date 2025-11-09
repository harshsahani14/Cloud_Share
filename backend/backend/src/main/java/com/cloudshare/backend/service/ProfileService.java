package com.cloudshare.backend.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.cloudshare.backend.document.ProfileDocument;
import com.cloudshare.backend.dto.ProfileDTO;
import com.cloudshare.backend.repositry.ProfileRepositry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepositry profileRepositry;
	
	public ProfileDTO createProfile(ProfileDTO profileDTO) {
		
		ProfileDocument profile =  ProfileDocument.builder().clerkId(profileDTO.getClerkId()).email(profileDTO.getEmail()).firstName(profileDTO.getFirstName()).lastName(profileDTO.getLastName()).photoUrl(profileDTO.getPhotoUrl()).credits(5).createdAt(Instant.now()).build();
		
		ProfileDocument savedProfile = profileRepositry.save(profile);
		
		return ProfileDTO.builder().id(savedProfile.getId()).clerkId(savedProfile.getClerkId()).email(savedProfile.getEmail()).firstName(savedProfile.getFirstName()).lastName(savedProfile.getLastName()).photoUrl(savedProfile.getPhotoUrl()).credits(savedProfile.getCredits()).createdAt(savedProfile.getCreatedAt()).build();
	
	}
}