package org.thekiddos.faith.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.thekiddos.faith.services.UserService;

@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Autowired
    public WebConfig( UserService userService ) {
        this.userService = userService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http.authorizeRequests()
                .antMatchers( "/admin", "/admin/**" ).hasAuthority( "ADMIN" )
                .antMatchers( "/stakeholder", "/stakeholder/**" ).hasAuthority( "STAKEHOLDER" )
                .antMatchers( "/register", "/register/**", "/forgot-password", "/reset-password", "/reset-password/**" ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage( "/login" ).usernameParameter("email").successHandler( myAuthenticationSuccessHandler() ).permitAll();
    }

    @Override
    public void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userService ).passwordEncoder( bCryptPasswordEncoder() );
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new LoginRedirection();
    }
}
