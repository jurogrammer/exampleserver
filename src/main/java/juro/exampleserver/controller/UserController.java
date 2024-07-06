package juro.exampleserver.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import juro.exampleserver.controller.model.UserCreateRequest;
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

	@GetMapping("/users/{id}")
	public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
		UserDto user = userService.getUser(id);

		UserResponse userResponse = UserResponse.of(user);
		return ApiResponse.success(userResponse);
	}

	@PostMapping("/users")
	public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
		UserDto user = userService.create(request.toDto());
		UserResponse userResponse = UserResponse.of(user);
		return ApiResponse.success(userResponse);
	}
}
