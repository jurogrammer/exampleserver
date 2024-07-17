package juro.exampleserver.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import juro.exampleserver.config.auth.JwtUtil;

class JwtUtilTest {
	private final JwtUtil sut = new JwtUtil(
		"V1RFTEpCSlRVUkRQVVBHUFBQSE5NTk1BVlNNU0pGV0JCWUpSU0dHVVVKTFRHVk5NUUdKWUQ",
		1000L
	);

	@Test
	@DisplayName("Token 검증 - 256bit secret key 입력시 검증 성공")
	void test() {
		// given
		String username = "username";
		String token = sut.generateToken(username);

		// when
		boolean b = sut.validateToken(token);

		// then
		Assertions.assertTrue(b);

	}

}
