package com.cloudshare.backend.security;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClerkJwtAuthFilter extends OncePerRequestFilter {
	
	@Value("${clerk.issuer}")
	private String clerkIssuer;

	private final ClerkJwksProvider clerkJwksProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if(request.getRequestURI().contains("webhooks")) {
			filterChain.doFilter(request, response);
		}
		
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN,"Request header is invalid");
			return;
		}
		
		try {
			
			String token = authHeader.substring(7);
			String[] chunks = token.split("\\.");
			
			if(chunks.length < 3) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,"Invalid JWT token");
				return;
			}
					
			String headerJson = new String(Base64.getDecoder().decode(chunks[0]));
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(headerJson);
			
			if(!jsonNode.has("kid")) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,"JWT token has missing key id");
				return;
			}
			
			String keyId = jsonNode.get("kid").asText();
			
			PublicKey publicKey = clerkJwksProvider.getPublicKey(keyId);
			
			//Verify the token			
			
			Claims claims = Jwts.parserBuilder()
			        			.setSigningKey(publicKey)
			        			.setAllowedClockSkewSeconds(60)
			        			.requireIssuer(clerkIssuer)
			        			.build()
			        			.parseClaimsJws(token)
			        			.getBody();
			
			String clerkId = claims.getSubject();
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(clerkId , null , Collections.singleton( new SimpleGrantedAuthority("ROLE_ADMIN")));
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			filterChain.doFilter(request, response);
					
					
		} catch (Exception e) {
			
		}
	}

}
