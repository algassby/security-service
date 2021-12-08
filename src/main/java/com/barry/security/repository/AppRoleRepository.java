/**
 * 
 */
package com.barry.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barry.security.entities.AppRole;

/**
 * @author algas
 *
 */
public interface AppRoleRepository extends JpaRepository<AppRole, Integer>{

	public AppRole findByRoleName(String roleName);
}
