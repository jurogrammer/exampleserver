package juro.exampleserver.repository;

import java.util.Optional;

import juro.exampleserver.dto.user.UserCreateRequestDto;
import juro.exampleserver.dto.user.UserDto;

public interface UserMapper {
	Optional<UserDto> getUserById(long id);

	UserDto createUser(UserCreateRequestDto dto);
}
