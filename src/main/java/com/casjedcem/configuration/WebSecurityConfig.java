package com.casjedcem.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.casjedcem.service.CustomUserDetailsService;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/admin/users", "/cart",
				"/favorite/remove/{pid}",
				"/favorite/add/{pid}",
				"/favorite").authenticated()
			
			.antMatchers(//"/admin/users",
				//"/admin/categories",
			//	"/admin/categories/add",
				//"/admin/categories/add",
				//"/admin/categories/delete/{id}",
				//"/admin/categories/update/{id}",
				//"/admin/products",
				//"/admin/products/add",
				//"/admin/products/add",
				//"/admin/product/delete/{id}",
				//"/admin/product/update/{id}"
					).hasRole("ADMIN")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			        .loginPage("/login")
				.usernameParameter("email")
				.defaultSuccessUrl("/")
				.permitAll()
			.and()
			.logout().logoutSuccessUrl("/").permitAll()
			.and()
			.logout().deleteCookies("JSESSIONID")
			.and()
			.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400);
	}
	
	
}
