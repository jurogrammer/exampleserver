package juro.exampleserver.service;

import org.springframework.stereotype.Component;

import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {
	private final UserRepository userRepository;

	public void validateUserExists(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=%s".formatted(userId));
		}
	}
}
