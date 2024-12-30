package io.firecontrol360.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

	@Autowired
	private DataSource dataSource;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Delegating the responsibility of general configurations
		// of http security to the super class. It's configuring
		// the followings: Vaadin's CSRF protection by ignoring
		// framework's internal requests, default request cache,
		// ignoring public views annotated with @AnonymousAllowed,
		// restricting access to other views/endpoints, and enabling
		// NavigationAccessControl authorization.
		// You can add any possible extra configurations of your own
		// here (the following is just an example):

		// http.rememberMe().alwaysRemember(false);

		// Configure your static resources with public access before calling
		// super.configure(HttpSecurity) as it adds final anyRequest matcher
		http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll());

		super.configure(http);

		// This is important to register your login view to the
		// navigation access control mechanism:
		setLoginView(http, LoginView.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Customize your WebSecurity configuration.
		super.configure(web);
	}

	/**
	 * Demo UserDetailsManager which only provides two hardcoded in memory users and their roles. NOTE: This shouldn't be used in real world applications. You
	 * have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.
	 */
//	@Bean
//	public UserDetailsManager userDetailsService() {
//		UserDetails user = User.withUsername("user").password("{noop}user").roles("USER").build();
//		UserDetails admin = User.withUsername("admin").password("{noop}admin").roles("ADMIN").build();
//		return new InMemoryUserDetailsManager(user, admin);
//	}

	
	@Bean
	public JdbcUserDetailsManager userDetailsService(DataSource datasource)
			 {
		JdbcUserDetailsManager jdbc = new JdbcUserDetailsManager(datasource);
		//user=$2a$12$xIbrnxPkmToF5OZBOBEkvOoiIrQhJdIlU8MIz8VKT8Q7pPIQ/r59u
		//admin=$2a$12$tu2ovzvW.Dm2KMWCN7gZ2OSah5Z69QsG14WNzhsk8fN0OeDOY.IWG
		return jdbc;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}