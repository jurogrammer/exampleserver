package juro.exampleserver.controller.user;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import juro.exampleserver.config.auth.LoginUser;
import juro.exampleserver.controller.common.ApiResponse;
import juro.exampleserver.controller.user.model.UserLoginRequest;
import juro.exampleserver.controller.user.model.UserRegisterRequest;
import juro.exampleserver.controller.user.model.UserResponse;
import juro.exampleserver.service.user.model.UserDto;
import juro.exampleserver.service.user.UserService;
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

	@LoginUser
	@GetMapping("/v1/users/{id}")
	public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
		UserDto user = userService.getUser(id);

		UserResponse userResponse = UserResponse.of(user);
		return ApiResponse.success(userResponse);
	}

}
