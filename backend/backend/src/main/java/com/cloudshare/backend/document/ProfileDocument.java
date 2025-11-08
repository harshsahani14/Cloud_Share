package com.cloudshare.backend.document;

import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "profiles")
public class ProfileDocument {
	
	@Id
	private String id;
	private String clerkId;
	
	@Indexed(unique = true)
	private String email;
	private String firstName;
	private String lastName;
	private String credits;
	private String photoUrl;
	
	@CreatedDate
	private Instant createdAt;

}
