package sinnake.auth.spring.security.oauth2Exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = SinnakeOauth2ExceptionSerializer.class)
public class SinnakeOauth2Exception extends OAuth2Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SinnakeOauth2Exception(String msg) {
		super(msg);
	}
}
