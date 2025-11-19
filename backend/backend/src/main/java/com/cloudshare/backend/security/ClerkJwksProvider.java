package com.cloudshare.backend.security;

import java.math.BigInteger;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ClerkJwksProvider {

	@Value("${clerk.jwks-url}")
	private String jwksUrl;
	
	private final Map<String, PublicKey> keyCache = new HashMap<>();
	private long lastFetchTime = 0;
	private static final long CACHE_TTL = 3600000;
	
	public PublicKey getPublicKey(String keyId) throws Exception {
		
		if(keyCache.containsKey(keyId) && System.currentTimeMillis() - lastFetchTime < CACHE_TTL) {
			return keyCache.get(keyId);
		}
		
		refreshKeys();
		
		return keyCache.get(keyId);
	}

	private void refreshKeys() throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
	
		URI uri = new URI(jwksUrl);	
		JsonNode jwks = objectMapper.readTree(uri.toURL());
		
		JsonNode keys = jwks.get("keys");
		
		for(JsonNode key:keys) {
			
			String keyType = key.get("kty").asText();
			String keyId = key.get("kid").asText();
			String alg = key.get("alg").asText();
			
			if("RSA".equals(keyType) && "RSA256".equals(alg)) {
				
				String n = key.get("n").asText();
				String e = key.get("e").asText();
				
				PublicKey publicKey = createPublicKey(n,e);
				
				keyCache.put(keyId, publicKey);		
			}
		}
		
		lastFetchTime = System.currentTimeMillis();
			
	}

	private PublicKey createPublicKey(String modulus, String exponent) throws Exception {
		
		byte[] modulusBytes = Base64.getDecoder().decode(modulus);
		byte[] exponentBytes = Base64.getDecoder().decode(exponent);
		
		BigInteger modulusBigInt = new BigInteger(1,modulusBytes);
		BigInteger exponentBigInt = new BigInteger(1, exponentBytes);
		
		RSAPublicKeySpec spec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		return keyFactory.generatePublic(spec);
	}
}
