package com.example.security_2fa.config.security;

import com.example.security_2fa.service.UserService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String LOGIN_URL = "/login";

	private static final String[] PATHS = {"/", "/signup", LOGIN_URL, "/logout", "/h2-console/**"};

	private static String LOGIN_URL_WITH_PARAM(String param) {
		return LOGIN_URL + "?" + param;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(authRequest -> authRequest.antMatchers(PATHS).permitAll()
						.anyRequest().fullyAuthenticated())
				.csrf().disable()
				.formLogin()
				.authenticationDetailsSource(new OTPWebAuthenticationDetailsSource())
				.loginPage(LOGIN_URL).defaultSuccessUrl("/")
				.failureUrl(LOGIN_URL_WITH_PARAM("error")).failureHandler(new CustomAuthenticationFailureHandler()).permitAll()
				.and()
				.logout()
				.logoutSuccessUrl(LOGIN_URL_WITH_PARAM("logout"))
				.deleteCookies("JSESSIONID").permitAll()
				.and()
				.headers().frameOptions().disable()
		;

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().antMatchers("/h2-console/**");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}

			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController(LOGIN_URL).setViewName("login");
			}
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public IGoogleAuthenticator googleAuthenticator() { return new GoogleAuthenticator(); }

	@Bean
	public InMemoryUserDetailsManager usersDetailsService() {
		UserDetails userDetails = User.builder()
				.username("user")
				.password("user")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(userDetails);
	}

	@Bean
	AuthenticationProvider authenticationProvider(UserService userService) {
		return new CustomAbstractUserDetailsAuthenticationProvider(passwordEncoder(), userService, googleAuthenticator());
	}
}
