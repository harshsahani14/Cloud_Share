package com.cloudshare.backend.repositry; 

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cloudshare.backend.document.ProfileDocument;

public interface ProfileRepositry extends MongoRepository<ProfileDocument, String> {

	Optional<ProfileDocument> findByEmail(String email);
	
	ProfileDocument findByClerkId(String clerkId);
	
	Boolean existsByClerkId(String clerkId);
	
}
