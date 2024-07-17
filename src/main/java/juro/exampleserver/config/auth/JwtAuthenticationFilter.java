package juro.exampleserver.config.auth;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import juro.exampleserver.controller.common.ApiResponse;
import juro.exampleserver.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailServiceImpl userDetailService;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws IOException, ServletException {
		try {
			String header = request.getHeader("Authorization");
			if (header == null || !header.startsWith("Bearer ")) {
				ServiceUser serviceUser = ServiceUser.guest();

				UserAuthenticationToken authenticationToken = UserAuthenticationToken.builder()
					.serviceUser(ServiceUser.guest())
					.credentials(null)
					.authorities(serviceUser.getAuthorities())
					.details(extractHeaders(request))
					.build();

				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(authenticationToken);
				SecurityContextHolder.setContext(context);

				filterChain.doFilter(request, response);
				return;
			}
			String token = header.substring(7);
			if (!jwtUtil.validateToken(token)) {
				throw new BadCredentialsException("Token is is invalid.");
			}

			String username = jwtUtil.getUsernameFromToken(token);
			ServiceUser serviceUser = userDetailService.loadUserByUsername(username);
			UserAuthenticationToken authenticationToken = UserAuthenticationToken.builder()
				.serviceUser(serviceUser)
				.credentials(token)
				.authorities(serviceUser.getAuthorities())
				.details(extractHeaders(request))
				.build();

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authenticationToken);
			SecurityContextHolder.setContext(context);

			filterChain.doFilter(request, response);
		} catch (AuthenticationException authenticationException) {
			logger.error("authentication failed", authenticationException);
			writeErrorResponse(response, ErrorCode.FORBIDDEN);
		} catch (Exception e) {
			logger.error("authentication failed because of unexpected error.", e);
			writeErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);

		}

	}

	private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		String responseBody = objectMapper.writeValueAsString(ApiResponse.fail(errorCode));
		response.getWriter().write(responseBody);
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
