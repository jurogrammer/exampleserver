package juro.exampleserver.config.auth;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import juro.exampleserver.repository.user.model.UserRole;
import juro.exampleserver.repository.user.UserRepository;
import juro.exampleserver.repository.user.model.User;
import lombok.RequiredArgsConstructor;

@Profile({"local", "dev"})
@RequiredArgsConstructor
@Component
public class DebuggingAuth {
	private final String USERNAME = "test";
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@PostConstruct
	public void init() {
		userRepository.findByUsername(USERNAME)
			.orElseGet(() -> {
				User user = User.builder()
					.username(USERNAME)
					.email("test@naver.com")
					.password("test")
					.roles(List.of(UserRole.USER))
					.build();

				return userRepository.save(user);
			});
	}

	public String getBearer() {
		return "Bearer " + jwtUtil.generateToken(USERNAME);
	}
}
