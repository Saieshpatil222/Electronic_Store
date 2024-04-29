package com.electronic.store.config;

import com.electronic.store.security.JwtAuthenticationEntryPoint;
import com.electronic.store.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//public class SecurityConfig  extends WebMvcConfigurerAdapter--> we use this in past

@Configuration
@EnableMethodSecurity(prePostEnabled = true) //Change
//--> by using this we provide Roles directly on controllers handler methods
public class SecurityConfig {

    @Autowired
    //-> yala autowired kel mhnje yachi implementation class customeUserdetailsService brobr yeun jail(Obj bnvel tya class ch)
    public UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    private final String[] PUBLIC_URLS = {
            //these are swagger provided Urls
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            // "/v2/api-docs",
            "/test"
    };


    //DaoAuthenticationProvider this will not be private Or final
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());


        return daoAuthenticationProvider;
    }

    @Bean // --> we make bean because we can outside wherever we want and autowired // anywhere
    public PasswordEncoder passwordEncoder() {


        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(e -> e.disable()).authorizeHttpRequests(request -> request.requestMatchers("/auth/login")//we want this is public so we made this permitAll
                        .permitAll()
                        .requestMatchers("/auth/google")//we want this is public so we made this permitAll
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/users")
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                        .requestMatchers(PUBLIC_URLS)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                ).exceptionHandling(config -> config.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

   /* @Bean
    public FilterRegistrationBean corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
        filterRegistrationBean.setOrder(-110);
        return filterRegistrationBean;
    }*/

    //in Spring boot if we want to declare any type of bean then we create that in any config class whether that class must contain @configuration annotation
//**********************************	
//we have different ways to configure cors(Cross origin resource sharing)
// 1>cross origin annotation with cors()in Spring security
// 2>Global config
    //GLOBAL CORS CONFIG(Global Config):-
   /* @Bean
    public FilterRegistrationBean corsFilter() {

        // Creates a source for CORS configurations based on URL patterns.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // initializes a new CORS configuration to specify rules for handling CORS
        // requests
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");// it will allow any domain eg
        // "http://localhost:4200","http://localhost:4242"
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);

        // Registers the CORS configuration to apply to all endpoints (/**).
        source.registerCorsConfiguration("/**", configuration);

        // Creates an instance of FilterRegistrationBean for registering a filter.
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(source));

        // Sets the filter to be used, which in this case is CorsFilter
        // instantiated with the previously configured source.
        // here we only register corsfilter

        // Sets the order of the filter.
        // Filters with lower values of order get executed first.
        // Negative values typically indicate filters added by the framework.
        filterRegistrationBean.setOrder(-110);

        return filterRegistrationBean;
    }*/


//**********************************		
    // ABOVE OR we can declare UrlBasedCorsConfigurationSource named as "corsFilter"
    //if we want to use .core()
//		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//		{  
//			http
//				.csrf()
//				.disable()//
//				.cors()
    //}
    //**IN BELOW CODE WE PROVIDE USER MANUALLYY WE DONT TAKE IT FROM DB
    // now only we declare here beans
//	@Bean
//	public UserDetailsService userDetailsService() {
//
//		UserDetails normalUser = User.builder().username("bhushan")
//											.password(passwordEncoder().encode("bhushan"))
//											.roles("Normal")
//
//											.build();
//
//		UserDetails Admin = User.builder().username("shubham").password(passwordEncoder().encode("shubham"))
//				.roles("Admin").build();
//
//		// users create
//
//		// InMemoryUserDetailsManager
//
//		return new InMemoryUserDetailsManager(normalUser, Admin);
//	}
    /*
     * public InMemoryUserDetailsManager(UserDetails... users) { for (UserDetails
     * user : users) { createUser(user); } }
     */
    //** BELOW ARE THE FORM BASED AUTHENTICATION
    //if we have Jsp,Html pages then spring security provides byDefault form based login page
    //then we can easily configure

    //	@Bean
    //	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception
    //	{
    //
    ////		http
    ////		.authorizeRequests().anyRequest().authenticated()//sglya request astil tya authorized and authenticate chi permission dili apan ithe
    ////		.and()
    ////		.formLogin()
    ////		.loginPage("login.html")
    ////		.loginProcessingUrl("/process-url")
    ////		.defaultSuccessUrl("/dashboard")
    ////		.failureUrl("/error")
    ////		.and()
    ////		.logout()
    ////		.logoutUrl("/logout");
    //		//here also we can provide role wise authentication
    //
    //
    //		return http.build();
    //	}
//*************************************
//BELOW IS BASIC AUTHENTICATION : -
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//	{
//		http
//			.csrf().disable()//
//			.cors().disable()
//			.authorizeRequests().anyRequest().authenticated()
//			.and()
//			.httpBasic();//--> main method of HttpSecurity class that method is responsible for basic authentication
//
//		return http.build();
//	}
//
//*************************************
    //BELOW IS JWT AUTHENTICATION : -
    /*
     * SecurityFilterChain :-
     * 1>it is an interface
     * 2>the SecurityFilterChain is a core component used to define
     * security configurations for different parts of your application.
     */
}