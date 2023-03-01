package edu.eci.ieti.lab.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguration(@Autowired JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/health").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/users").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
