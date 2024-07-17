package juro.exampleserver.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
//https://github.com/spring-projects/spring-framework/issues/23744#issuecomment-537358899
@EnableMethodSecurity(proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/h2-console/**", "/h2-console", "/error")
				.permitAll()
				.anyRequest()
				.authenticated()
			)
			.csrf(AbstractHttpConfigurer::disable)
			// For H2 console
			.headers(headers -> headers.frameOptions(options -> options.disable()))
			.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
			.rememberMe(AbstractHttpConfigurer::disable)
			.anonymous(AbstractHttpConfigurer::disable);

		return http.build();
	}
}
