package ca.sheridancollege.komarovs.assignment3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ca.sheridancollege.komarovs.assignment3.services.UserDetailsServiceImpl;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
		
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		// TEMPORARY!!!! - remove this when done!!
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		http
			.authorizeRequests()
				.antMatchers("/").hasAnyRole("USER", "ADMIN")
				.antMatchers("/login", "/js/**", "/css/**", "/images/**")
					.permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.formLogin().loginPage("/login")
				.defaultSuccessUrl("/", true).permitAll()
			.and()
				.logout()
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/login?logout").permitAll();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}
	
}
