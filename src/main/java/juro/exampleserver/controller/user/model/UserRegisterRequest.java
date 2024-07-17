package juro.exampleserver.controller.user.model;

import jakarta.validation.constraints.NotBlank;
import juro.exampleserver.service.user.model.UserRegisterRequestDto;
import lombok.Data;

@Data
public class UserRegisterRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;

	public UserRegisterRequestDto toDto() {
		return UserRegisterRequestDto.builder()
			.username(this.username)
			.password(this.password)
			.email(this.email)
			.build();
	}
}
