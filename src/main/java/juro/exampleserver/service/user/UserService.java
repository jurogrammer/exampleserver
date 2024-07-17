package juro.exampleserver.service.user;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import juro.exampleserver.config.auth.JwtUtil;
import juro.exampleserver.service.user.model.LoginRequestDto;
import juro.exampleserver.service.user.model.UserDto;
import juro.exampleserver.service.user.model.UserRegisterRequestDto;
import juro.exampleserver.repository.user.model.UserRole;
import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.user.UserRepository;
import juro.exampleserver.repository.user.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final Pattern PATTERN = Pattern.compile(
		"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$");

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Transactional(readOnly = true)
	public String login(LoginRequestDto dto) {
		User user = userRepository.findByUsername(dto.getUsername())
			.orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST,
				"credential is not correct. request=%s".formatted(dto)));

		if (!dto.getPassword().equals(user.getPassword())) {
			throw new ClientException(ErrorCode.BAD_REQUEST,
				"credential is not correct. request=%s".formatted(dto));
		}

		return jwtUtil.generateToken(dto.getUsername());
	}

	@Transactional(readOnly = true)
	public UserDto getUser(Long id) {
		User user = userRepository.findUserById(id)
			.orElseThrow(
				() -> new ClientException(ErrorCode.BAD_REQUEST, "cannot found user. userId = %s".formatted(id)));

		return UserDto.of(user);
	}

	@Transactional
	public UserDto register(UserRegisterRequestDto dto) {
		validatePassword(dto.getPassword());
		Optional<User> findUser = userRepository.findByUsername(dto.getUsername());
		if (findUser.isPresent()) {
			throw new ClientException(ErrorCode.BAD_REQUEST, "username already exists");
		}

		User user = User.builder()
			.username(dto.getUsername())
			.password(dto.getPassword())
			.email(dto.getEmail())
			.roles(List.of(UserRole.USER))
			.build();

		User savedUser = userRepository.save(user);
		return UserDto.of(savedUser);
	}

	// 비밀번호 규칙 정의 (최소 8자, 대문자, 소문자, 숫자, 특수 문자 포함)
	private void validatePassword(String password) {
		if (password == null || !PATTERN.matcher(password).matches()) {
			throw new ClientException(ErrorCode.INVALID_PASSWORD,
				"invalid password. password = %s".formatted(password));
		}
	}
}
