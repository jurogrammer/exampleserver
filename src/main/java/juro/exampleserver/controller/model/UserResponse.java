package juro.exampleserver.controller.model;

import juro.exampleserver.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String username;
	private String password;
	private String email;

	public static UserResponse of(UserDto user) {
		return UserResponse.builder()
			.id(user.getId())
			.username(user.getUsername())
			.password(user.getPassword())
			.email(user.getEmail())
			.build();
	}
}
