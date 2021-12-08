/**
 * 
 */
package com.barry.security.service;

import java.util.List;

import com.barry.security.entities.AppRole;
import com.barry.security.entities.AppUser;



/**
 * @author algas
 *
 */
public interface AccountService {

	AppUser addNewUser(AppUser appUser);
	AppRole addNewRole(AppRole appRole);
	void addRoleToUser(String username, String roleName);
	AppUser loadUserByUsername(String username);
	List<AppUser> getAllUsers();
}
