package juro.exampleserver.controller.model.user;

import juro.exampleserver.dto.user.LoginRequestDto;
import lombok.Data;

@Data
public class UserLoginRequest {
	private String username;
	private String password;


	public LoginRequestDto toDto() {
		return LoginRequestDto.builder()
			.username(username)
			.password(password)
			.build();


	}
}
