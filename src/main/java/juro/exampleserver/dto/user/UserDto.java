package juro.exampleserver.dto.user;

import java.util.List;

import juro.exampleserver.repository.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
	private String username;
	private String password;
	private String email;
	private UserRole role;

	public static UserDto of(User user) {
		return UserDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.password(user.getPassword())
			.email(user.getEmail())
			.role(user.getRole())
			.build();
	}
}
