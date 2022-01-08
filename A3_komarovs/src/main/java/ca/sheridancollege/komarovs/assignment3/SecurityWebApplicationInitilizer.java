package ca.sheridancollege.komarovs.assignment3;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import ca.sheridancollege.komarovs.assignment3.security.SecurityConfig;

public class SecurityWebApplicationInitilizer extends AbstractSecurityWebApplicationInitializer {
	
	public SecurityWebApplicationInitilizer() {
		super(SecurityConfig.class);
	}

}
