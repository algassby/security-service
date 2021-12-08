package com.barry.security;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.barry.security.entities.AppRole;
import com.barry.security.entities.AppUser;
import com.barry.security.service.AccountService;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	@Bean
	public CommandLineRunner start(AccountService accountService) {
		return args->{
			accountService.addNewRole(new AppRole(0, "USER"));
			accountService.addNewRole(new AppRole(0, "ADMIN"));
			accountService.addNewRole(new AppRole(0, "CUSTOMER_MANAGER"));
			accountService.addNewRole(new AppRole(0, "PRODUCT_MANAGER"));
			accountService.addNewRole(new AppRole(0, "BILLS_MANAGER"));
			
			accountService.addNewUser(new AppUser(0, "user1", "123456789", new ArrayList<AppRole>()));
			accountService.addNewUser(new AppUser(0, "admin", "123456789", new ArrayList<AppRole>()));
			accountService.addNewUser(new AppUser(0, "user2", "123456789", new ArrayList<AppRole>()));
			accountService.addNewUser(new AppUser(0, "user3", "123456789", new ArrayList<AppRole>()));
			accountService.addNewUser(new AppUser(0, "user4", "123456789", new ArrayList<AppRole>()));
			
			accountService.addRoleToUser("user1", "USER");
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
			accountService.addRoleToUser("user2", "CUSTOMER_MANAGER");
			accountService.addRoleToUser("user3", "USER");
			accountService.addRoleToUser("user3", "PRODUCT_MANAGER");
			accountService.addRoleToUser("user4", "USER");
			accountService.addRoleToUser("user4", "CUSTOMER_MANAGER");
		};
	}

}
