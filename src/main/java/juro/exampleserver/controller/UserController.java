package juro.exampleserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import juro.exampleserver.controller.model.UserLoginRequest;
import juro.exampleserver.controller.model.UserRegisterRequest;
import juro.exampleserver.controller.model.UserResponse;
import juro.exampleserver.dto.common.ApiResponse;
import juro.exampleserver.dto.user.UserDto;
import juro.exampleserver.service.UserService;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/v1/auth/login")
	public ApiResponse<String> login(@Valid @RequestBody UserLoginRequest request) {
		String token = userService.login(request.toDto());
		return ApiResponse.success(token);
	}

	@PostMapping("/v1/auth/register")
	public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
		UserDto user = userService.register(request.toDto());
		UserResponse userResponse = UserResponse.of(user);
		return ApiResponse.success(userResponse);
	}

	// TODO: method 적용
	// @PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/v1/users/{id}")
	public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
		UserDto user = userService.getUser(id);

		UserResponse userResponse = UserResponse.of(user);
		return ApiResponse.success(userResponse);
	}

}
