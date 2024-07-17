package juro.exampleserver.config.auth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;


public class UserAuthenticationToken extends AbstractAuthenticationToken {

	private final ServiceUser serviceUser;
	private final String credentials;

	@Builder
	public UserAuthenticationToken(
		ServiceUser serviceUser,
		String credentials,
		Collection<? extends GrantedAuthority> authorities,
		Map<String, String> details
	) {
		super(authorities);
		this.credentials = credentials;
		this.serviceUser = serviceUser;
		super.setDetails(details);
		super.eraseCredentials();
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public ServiceUser getPrincipal() {
		return this.serviceUser;
	}

	@Override
	public Map<String, String> getDetails() {
		return (Map<String, String>)super.getDetails();
	}
}
