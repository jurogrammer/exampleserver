package juro.exampleserver.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import juro.exampleserver.config.model.ServiceUser;
import juro.exampleserver.repository.user.UserRepository;
import juro.exampleserver.repository.user.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public ServiceUser loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return ServiceUser.of(user);
	}
}
