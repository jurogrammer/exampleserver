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
	private String nickname;
	private String password;
	private String email;

	public static UserResponse of(UserDto user) {
		return UserResponse.builder()
			.id(user.getId())
			.nickname(user.getNickname())
			.password(user.getPassword())
			.email(user.getEmail())
			.build();
	}
}
