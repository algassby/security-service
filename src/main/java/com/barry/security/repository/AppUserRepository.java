/**
 * 
 */
package com.barry.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barry.security.entities.AppUser;

/**
 * @author algas
 *
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	public AppUser findByUserName(String username);
}
