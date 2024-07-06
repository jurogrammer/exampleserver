package juro.exampleserver.repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import juro.exampleserver.dto.user.UserCreateRequestDto;
import juro.exampleserver.dto.user.UserDto;

@Repository
public class UserMapperHardCording implements UserMapper {
	private static final ConcurrentHashMap<Long, UserDto> REPOSITORY = new ConcurrentHashMap<>();
	private static final AtomicLong ATOMIC_LONG = new AtomicLong();

	@Override
	public Optional<UserDto> getUserById(long id) {
		return Optional.ofNullable(REPOSITORY.get(id));
	}

	@Override
	public UserDto createUser(UserCreateRequestDto dto) {
		long id = ATOMIC_LONG.incrementAndGet();
		UserDto userDto = UserDto.builder()
			.id(id)
			.nickname(dto.getNickname())
			.password(dto.getPassword())
			.email(dto.getEmail())
			.build();

		REPOSITORY.put(id, userDto);
		return userDto;
	}
}
