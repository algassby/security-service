/**
 * 
 */
package com.barry.security.service;

import java.util.List;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barry.security.entities.AppRole;
import com.barry.security.entities.AppUser;
import com.barry.security.repository.AppRoleRepository;
import com.barry.security.repository.AppUserRepository;

/**
 * @author algas
 *
 */
@Service
@Transactional

public class AccountServiceImpl implements AccountService {
	
	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private PasswordEncoder passwordEncoder;
	
	/**
	 * @param appUserRepository
	 * @param appRoleRepository
	 */
	public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,PasswordEncoder passwordEncoder) {
		super();
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	

	@Override
	public AppUser addNewUser(AppUser appUser) {
	
		String pw  = appUser.getPassword();
		appUser.setPassword(passwordEncoder.encode(pw));
		return appUserRepository.save(appUser) ;
	}

	@Override
	public AppRole addNewRole(AppRole appRole) {
		
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser appUser = appUserRepository.findByUserName(username);
		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		appUser.getAppRoles().add(appRole);
		
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		return appUserRepository.findByUserName(username);
	
	}


	@Override
	public List<AppUser> getAllUsers() {
		return appUserRepository.findAll();
	}

}
