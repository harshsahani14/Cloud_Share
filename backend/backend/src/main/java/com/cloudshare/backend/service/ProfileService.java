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
		
		if(profileRepositry.existsByClerkId(profileDTO.getClerkId())) {
			return updateProfile(profileDTO);
		}
		
		ProfileDocument profile =  ProfileDocument.builder()
				                                   .clerkId(profileDTO.getClerkId())
				                                   .email(profileDTO.getEmail())
				                                   .firstName(profileDTO.getFirstName())
				                                   .lastName(profileDTO.getLastName())
				                                   .photoUrl(profileDTO.getPhotoUrl())
				                                   .credits(5)
				                                   .createdAt(Instant.now())
				                                   .build();
		
		ProfileDocument savedProfile = profileRepositry.save(profile);
		
		return ProfileDTO.builder()
				         .id(savedProfile.getId())
				         .clerkId(savedProfile.getClerkId())
				         .email(savedProfile.getEmail())
				         .firstName(savedProfile.getFirstName())
				         .lastName(savedProfile.getLastName())
				         .photoUrl(savedProfile.getPhotoUrl())
				         .credits(savedProfile.getCredits())
				         .createdAt(savedProfile.getCreatedAt())
				         .build();
	
	}
	
	public ProfileDTO updateProfile(ProfileDTO profileDTO) {
		
		ProfileDocument existingProfile = profileRepositry.findByClerkId(profileDTO.getClerkId());
		
		if(existingProfile != null) {
			
			if(profileDTO.getEmail() != null && !profileDTO.getEmail().isEmpty()) {
				existingProfile.setEmail(profileDTO.getEmail());
			}
			
			if(profileDTO.getFirstName() != null && !profileDTO.getFirstName().isEmpty()) {
				existingProfile.setFirstName(profileDTO.getFirstName());
			}
			
			if(profileDTO.getLastName() != null && !profileDTO.getLastName().isEmpty()) {
				existingProfile.setLastName(profileDTO.getLastName());
			}
			
			if(profileDTO.getPhotoUrl() != null && !profileDTO.getPhotoUrl().isEmpty()) {
				existingProfile.setPhotoUrl(profileDTO.getPhotoUrl());
			}
			
			profileRepositry.save(existingProfile);
			
			return ProfileDTO.builder()
						.id(existingProfile.getId())
						.email(existingProfile.getEmail())
						.clerkId(existingProfile.getClerkId())
						.firstName(existingProfile.getFirstName())
						.lastName(existingProfile.getLastName())
						.credits(existingProfile.getCredits())
						.createdAt(existingProfile.getCreatedAt())
						.photoUrl(existingProfile.getPhotoUrl())
						.build();
		}
		
		return null;
		
	}
	
	public boolean existsByClerkId(String clerkId) {
		return profileRepositry.existsByClerkId(clerkId);
	}
	
	public void deleteProfile(String clerkId) {
		
		ProfileDocument existingProfile = profileRepositry.findByClerkId(clerkId);
		
		if(existingProfile != null) {
			profileRepositry.delete(existingProfile);
		}
	}
	
	
		
}