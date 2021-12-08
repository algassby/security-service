/**
 * 
 */
package com.barry.security.sec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.barry.security.entities.AppUser;
import com.barry.security.service.AccountService;

/**
 * @author algas
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private AccountService accountService;
	@Override
	public void configure(AuthenticationManagerBuilder  auth) throws Exception {
		auth.userDetailsService(new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				AppUser appUser =  accountService.loadUserByUsername(username);
				Collection<GrantedAuthority> authorities = new ArrayList<>();
				appUser.getAppRoles().forEach(role->{
					authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
				});
				return new User(appUser.getUserName(), appUser.getPassword(),
						appUser.getAppRoles().stream().map(role->new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList()));
			}
			
		});
	}
	@Override
	public void configure(HttpSecurity  http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/h2-console/**","/account/refreshToken/**","/login/**").permitAll();
//		http.authorizeRequests().antMatchers(HttpMethod.POST,"/account/users").hasAuthority("ADMIN");
//		http.authorizeRequests().antMatchers(HttpMethod.POST,"/account/save").hasAuthority("ADMIN");
		//http.formLogin();
		http.authorizeRequests()
		.anyRequest()
		.authenticated();
		http.addFilter(new JwtAuthentificationFilter(authenticationManagerBean()));
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	
}
