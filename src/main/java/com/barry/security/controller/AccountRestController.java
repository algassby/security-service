/**
 * 
 */
package com.barry.security.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.barry.security.entities.AppRole;
import com.barry.security.entities.AppUser;
import com.barry.security.entities.RoleUserForm;
import com.barry.security.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * @author algas
 *
 */
@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AccountRestController {

	private Logger logger = LoggerFactory.getLogger(AccountRestController.class);
	private AccountService accountService;

	/**
	 * @param accountService
	 */
	@Autowired
	public AccountRestController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}
	
	@GetMapping("/users")
	@PostAuthorize("hasAnyAuthority('ADMIN','USER')")
	public List<AppUser> getAllUsers() {
		accountService.getAllUsers().forEach(user->logger.info(user.toString()));
		return accountService.getAllUsers();
	}
	@PostAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path="/saveUser")
	public AppUser saveUser(@RequestBody AppUser appUser) {
		return accountService.addNewUser(appUser);
	}
	@PostAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path="/saveRole")
	public AppRole saveRole(@RequestBody AppRole appRole) {
		return accountService.addNewRole(appRole);
	}
	@PostMapping(path="/addRoleToUser")
	public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
		 accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
	}
	@GetMapping("/refreshToken")
	public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String authToken =  request.getHeader("Authorization");
		if(authToken!=null && authToken.startsWith("Bearer ")) {
			try {
				//refreshToken
				String jwtrefreshToken = authToken.substring(7);
				Algorithm algorithm = Algorithm.HMAC256("mySecret1234");
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT =  jwtVerifier.verify(jwtrefreshToken);
				String username = decodedJWT.getSubject();
				AppUser appUser = accountService.loadUserByUsername(username);
				
				//AccesToken
				String jwtAccessToken = JWT.create()
						.withSubject(appUser.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis()+1*60*1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", appUser.getAppRoles().stream().map(role->role.getRoleName()).collect(Collectors.toList()))
						.sign(algorithm);
				
				
				Map<String, String> idToken = new HashMap<>();
				idToken.put("acces-token", jwtAccessToken);
				idToken.put("refresh-token", jwtrefreshToken);
				response.setContentType("/application/json");
				 new ObjectMapper().writeValue(response.getOutputStream(), idToken);
			} catch (Exception e) {
				throw e;
			}
		}
		else {
			throw new RuntimeException("RefreshToken Required");
		}
	}
}
