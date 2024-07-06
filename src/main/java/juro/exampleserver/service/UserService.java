package juro.exampleserver.service;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import juro.exampleserver.dto.user.UserCreateRequestDto;
import juro.exampleserver.dto.user.UserDto;
import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

	private final UserMapper userMapper;

	public UserDto getUser(Long id) {

		return userMapper.getUserById(id)
			.orElseThrow(
				() -> new ClientException(ErrorCode.BAD_REQUEST, "cannot found user. userId = %s".formatted(id)));
	}

	public UserDto create(UserCreateRequestDto dto) {
		// validatePassword(dto.getPassword());

		return userMapper.createUser(dto);
	}

	// 비밀번호 규칙 정의 (최소 8자, 대문자, 소문자, 숫자, 특수 문자 포함)

	private void validatePassword(String password) {
		if (password == null || !pattern.matcher(password).matches()) {
			throw new ClientException(ErrorCode.BAD_REQUEST, "invalid password. password = %s".formatted(password));
		}
	}
}
