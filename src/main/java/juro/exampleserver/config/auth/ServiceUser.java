package juro.exampleserver.config.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import juro.exampleserver.repository.user.model.UserRole;
import juro.exampleserver.repository.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUser implements UserDetails {
	private Long id;
	private String username;
	private String password;
	private String email;
	private List<GrantedAuthority> authorities;

	public static ServiceUser user(User user) {
		return ServiceUser.builder()
			.id(user.getId())
			.username(user.getUsername())
			.password(user.getPassword())
			.email(user.getEmail())
			.authorities(AuthorityUtils.createAuthorityList(UserRole.USER.name()))
			.build();

	}

	public static ServiceUser guest() {
		return ServiceUser.builder()
			.id(null)
			.username(null)
			.password(null)
			.email(null)
			.authorities(AuthorityUtils.createAuthorityList(UserRole.GUEST.name()))
			.build();

	}
}
