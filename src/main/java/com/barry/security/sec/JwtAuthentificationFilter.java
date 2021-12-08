/**
 * 
 */
package com.barry.security.sec;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author algas
 *
 */
public class JwtAuthentificationFilter extends UsernamePasswordAuthenticationFilter {

	Logger logger = LoggerFactory.getLogger(JwtAuthentificationFilter.class);
	private AuthenticationManager authenticationManager;
	
	/**
	 * @param authenticationManager
	 */
	public JwtAuthentificationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("mySecret1234");
		String jwtAccessToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+15*60*1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getAuthorities().stream().map(granted->granted.getAuthority()).collect(Collectors.toList()))
				.sign(algorithm);
		response.setHeader("Authorization", jwtAccessToken);
		
		String jwtRefreshToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+15*60*1000))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);
		Map<String, String> idToken = new HashMap<>();
		idToken.put("acces-token", jwtAccessToken);
		idToken.put("refresh-token", jwtRefreshToken);
		response.setContentType("/application/json");
		 new ObjectMapper().writeValue(response.getOutputStream(), idToken);
	}
	

	
}
