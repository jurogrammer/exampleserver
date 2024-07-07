package juro.exampleserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// .authorizeHttpRequests((authorize) -> authorize
			// 	.requestMatchers("/v1/auth/login", "/v1/auth/register", "/v1/users/{id}", "/h2-console/**",
			// 		"/h2-console").permitAll()
			// 	// .anyRequest().authenticated()
			// )
			.csrf(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions(options -> options.disable()))
			.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
		; // For H2 console

		return http.build();
	}

	// @Bean
	// public PasswordEncoder passwordEncoder() {
	// 	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	// }
}
