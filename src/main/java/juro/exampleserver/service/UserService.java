package juro.exampleserver.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import juro.exampleserver.dto.user.LoginRequestDto;
import juro.exampleserver.dto.user.UserDto;
import juro.exampleserver.dto.user.UserRegisterRequestDto;
import juro.exampleserver.dto.user.UserRole;
import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.UserRepository;
import juro.exampleserver.repository.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final Pattern PATTERN = Pattern.compile(
		"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$");

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;

	public void login(LoginRequestDto dto) {
		Authentication authenticationRequest =
			UsernamePasswordAuthenticationToken.unauthenticated(dto.getUsername(), dto.getPassword());
		Authentication response = this.authenticationManager.authenticate(authenticationRequest);
		if (!response.isAuthenticated()) {
			throw new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request=%s".formatted(dto));
		}
	}

	public UserDto getUser(Long id) {
		User user = userRepository.findUserById(id)
			.orElseThrow(
				() -> new ClientException(ErrorCode.BAD_REQUEST, "cannot found user. userId = %s".formatted(id)));

		return UserDto.of(user);
	}

	public UserDto register(UserRegisterRequestDto dto) {
		validatePassword(dto.getPassword());
		Optional<User> findUser = userRepository.findByUsername(dto.getUsername());
		if (findUser.isPresent()) {
			throw new ClientException(ErrorCode.BAD_REQUEST, "username already exists");
		}

		User user = User.builder()
			.username(dto.getUsername())
			.password(passwordEncoder.encode(dto.getPassword()))
			.email(dto.getEmail())
			.role(UserRole.ROLE_USER)
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
