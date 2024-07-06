package juro.exampleserver.controller.model;

import jakarta.validation.constraints.NotBlank;
import juro.exampleserver.dto.user.UserCreateRequestDto;
import lombok.Data;

@Data
public class UserCreateRequest {
	@NotBlank
	private String nickname;
	@NotBlank
	private String password;
	@NotBlank
	private String email;

	public UserCreateRequestDto toDto() {
		return UserCreateRequestDto.builder()
			.nickname(this.nickname)
			.password(this.password)
			.email(this.email)
			.build();
	}
}
