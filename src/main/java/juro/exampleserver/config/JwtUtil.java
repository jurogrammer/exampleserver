package juro.exampleserver.config;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final String secretKey;
	private final long jwtExpirationInMs;

	public JwtUtil(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.expiration}") Long jwtExpirationInMs
	) {
		this.secretKey = secretKey;
		this.jwtExpirationInMs = jwtExpirationInMs;
	}

	public static SecretKey generateKeyFromString(String input) {
		return Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(input));
	}

	public String generateToken(String username) {
		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
			.signWith(generateKeyFromString(secretKey))
			.compact();
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			log.error("token is not valid.", e);
			return false;
		}
	}
}
