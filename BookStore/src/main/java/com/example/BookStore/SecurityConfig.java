package com.example.BookStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JWTFilter jwtFilter;
	
	@Autowired //Tạo bean cho tham số vì autowire trên hàm.
	public void config (AuthenticationManagerBuilder auth) throws Exception {
				
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder()); // so sánh với mật khẩu người dùng trong csdl	
		
		System.out.println("1");
	}
	
	@Bean // để check mật khẩu tài khoản bên LogController , tạo bean cho kiểu trả về khác với @Autowired
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		System.out.println("2");
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	//Phân quyền theo đường dẫn và phương thức xác thực
	public SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.authorizeRequests()
			.requestMatchers("/admin/**")
			.hasAnyAuthority("ADMIN", "SUBADMIN") // với đường dẫn admin thì cần có quyền là role_admin/subadmin
			.requestMatchers("/member/**") 
			.authenticated() // với đường dẫn member chỉ cần đăng nhập
			.anyRequest().permitAll() // tất cả các request khác không cần quyền
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
			.and().httpBasic()
			.and().csrf().disable();
		
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		System.out.println("3");
			
			
		return httpSecurity.build();			
	}	
}
