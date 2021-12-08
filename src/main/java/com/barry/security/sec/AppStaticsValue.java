/**
 * 
 */
package com.barry.security.sec;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author algas
 *
 */
@Data
@Getter
@Setter

public  class AppStaticsValue {

	@Value("${app.accesstokensecretkey}")
	private static  String ACCESSTOKENSECRETKEY;
	@Value("{app.refreshtokensecretkey}")
	private static  String REFERSHTOKENSECRETKEY;
	@Value("${app.refreshTokenExpiration}")
	private   long REFRESHTOKENEXPIRATION;
	@Value("${app.accesstokenExpiration}")
	private final static long  accessTokenExpiration = 2*60*1000;
}
