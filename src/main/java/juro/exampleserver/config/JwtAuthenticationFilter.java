package juro.exampleserver.config;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import juro.exampleserver.config.model.ServiceUser;
import juro.exampleserver.config.model.UserAuthenticationToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailServiceImpl userDetailService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws IOException, ServletException {

		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = header.substring(7);

		if (!jwtUtil.validateToken(token)) {
			throw new BadCredentialsException("Token is is invalid.");
		}

		String username = jwtUtil.getUsernameFromToken(token);
		ServiceUser serviceUser = userDetailService.loadUserByUsername(username);
		if (serviceUser != null) {
			UserAuthenticationToken authenticationToken = UserAuthenticationToken.builder()
				.serviceUser(serviceUser)
				.credentials(token)
				.authorities(serviceUser.getAuthorities())
				.details(extractHeaders(request))
				.build();

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authenticationToken);
			SecurityContextHolder.setContext(context);
		}

		filterChain.doFilter(request, response);
	}

	private Map<String, String> extractHeaders(HttpServletRequest request) {
		Map<String, String> headers = new HashMap<>();

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			headers.put(headerName, headerValue);
		}

		return headers;
	}
}
