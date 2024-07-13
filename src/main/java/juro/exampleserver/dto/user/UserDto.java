package juro.exampleserver.dto.user;

import java.util.List;

import juro.exampleserver.repository.user.model.User;
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
	private List<UserRole> roles;

	public static UserDto of(User user) {
		return UserDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.password(user.getPassword())
			.email(user.getEmail())
			.roles(user.getRoles())
			.build();
	}
}
