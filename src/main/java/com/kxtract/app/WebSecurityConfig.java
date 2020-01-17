package com.kxtract.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/**
		 * The following is a list of the current Default Security Headers provided by Spring Security:
		 *  - Cache Control
		 *  - Content Type Options
		 *  - HTTP Strict Transport Security
		 *  - X-Frame-Options
		 *  - X-XSS-Protection
		 *  
		 * https://docs.spring.io/spring-security/site/docs/3.2.0.CI-SNAPSHOT/reference/html/headers.html
		 */
		http.headers().contentSecurityPolicy("script-src 'self'");
		//http.csrf().disable();
		//http.headers().frameOptions().sameOrigin();

	}
}