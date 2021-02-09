package ru.andrewalehin.calculator.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
    auth.inMemoryAuthentication()
        .withUser(userBuilder.username("admin").password("admin").roles("ADMIN"))
        .withUser(userBuilder.username("user").password("user").roles("USER"));
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/rest/calculator/findByUser/**",
            "/rest/calculator/findByOperation/**", "/rest/calculator/filter")
        .hasAnyRole("ADMIN")
        .antMatchers("/rest/**").authenticated()
        .and().httpBasic()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().csrf().disable();
  }
}