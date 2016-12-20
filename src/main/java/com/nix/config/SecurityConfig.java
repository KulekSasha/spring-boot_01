package com.nix.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private static final GrantedAuthority USER = new SimpleGrantedAuthority("ROLE_USER");
    private static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    private static final String REALM = "MY_TEST_REALM";

    private UserDetailsService userDetailsService;

    @Autowired
    private Environment env;

    @Resource(name = "userDetailsService")
    public void setUserDetailsService(@Qualifier("userDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

//    @Configuration
//    @Order(1)
//    public class ApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            // @formatter:off
//             http
//                    .antMatcher("/api/**")
//                    .authorizeRequests()
//                    .regexMatchers("/api/soap/users\\?wsdl").permitAll()
//                    .antMatchers("/api/**").hasRole("ADMIN")
//                    .and()
//                 .httpBasic()
//                    .realmName(REALM)
////                    .authenticationEntryPoint(getBasicAuthEntryPoint())
//                    .and()
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                 .exceptionHandling()
//                     .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            log.debug("in access handler");
//                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            PrintWriter writer = response.getWriter();
//                            writer.println("HTTP Status 403 : " + accessDeniedException.getMessage());
//                            })
//                     .and()
//                 .csrf().disable();
//            // @formatter:on
//        }
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

//    @Bean
//    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
//        return new CustomBasicAuthenticationEntryPoint();
//    }

    @Configuration
    @Order(2)
    public class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


        @Override
        protected void configure(HttpSecurity http) throws Exception {
//      @formatter:off
            http
                    .authorizeRequests()
                    .antMatchers("/resources/**", "/", "/login", "/logout",
                            "/registration/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").access("hasRole('ADMIN') or hasRole('USER')")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("login")
                    .passwordParameter("pwd")
                    .successHandler((req, resp, auth) -> {
                        String location;
                        if (auth.getAuthorities().contains(ADMIN)) {
                            location = req.getContextPath().concat("/admin/users");
                        } else if (auth.getAuthorities().contains(USER)) {
                            location = req.getContextPath().concat("/user/user");
                        } else {
                            location = req.getContextPath().concat("/login");
                        }
                        resp.sendRedirect(location);
                    })
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/access_denied")
                .and()
                    .csrf().disable();
            // @formatter:on
        }
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AuthenticationSuccessHandlerImpl();
//    }

//    @Bean
//    public ReCaptchaImpl reCaptcha() {
//        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
//        reCaptcha.setPrivateKey(env.getProperty("recaptcha.secret-key"));
//        reCaptcha.setPublicKey(env.getProperty("recaptcha.site-key"));
//        return reCaptcha;
//    }

}
