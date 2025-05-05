## Spring security

https://docs.spring.io/spring-security/reference/servlet/getting-started.html#hello-expectations

If you define a @Configuration with a SecurityFilterChain bean in your application, it switches off the default webapp security settings in Spring Boot.

```Java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().authenticated()
			)
			.httpBasic(Customizer.withDefaults())
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
			.username("user")
			.password("password")
			.roles("USER")
			.build();

		return new InMemoryUserDetailsManager(userDetails);
	}

}
```

## Multiple SecurityFilterChain

https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-multi-securityfilterchain-figure

https://www.danvega.dev/blog/multiple-spring-security-configs


## Logging

`logging.level.org.springframework.security=TRACE`
