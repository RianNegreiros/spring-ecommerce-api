package com.riannegreiros.springecommerce.security.config;

import com.riannegreiros.springecommerce.security.filters.CustomAuthenticationFilter;
import com.riannegreiros.springecommerce.security.userDetails.CustomCustomerDetailsService;
import com.riannegreiros.springecommerce.security.userDetails.CustomUserDetailsService;
import com.riannegreiros.springecommerce.security.filters.CustomAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.riannegreiros.springecommerce.utils.JWTConstants.JWTSECRET;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService userDetailsService;
    private final CustomCustomerDetailsService customCustomerDetailsService;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService, CustomCustomerDetailsService customCustomerDetailsService) {
        this.userDetailsService = userDetailsService;
        this.customCustomerDetailsService = customCustomerDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(customCustomerDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.cors().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/customers/**").permitAll();
        http.authorizeRequests().antMatchers("/api/token/**").permitAll();
        http.authorizeRequests().antMatchers("/api/users/**", "/api/settings/**").hasAuthority("Admin");
        http.authorizeRequests().antMatchers("/api/categories/**", "/api/brands/**").hasAnyAuthority("Admin", "Editor");
        http.authorizeRequests().antMatchers(HttpMethod.POST , "/api/products/**").hasAnyAuthority("Admin", "Editor");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE , "/api/products/**").hasAnyAuthority("Admin", "Editor");
        http.authorizeRequests().antMatchers(HttpMethod.PUT , "/api/products/**").hasAnyAuthority("Admin", "Editor", "Salesperson");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/products/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.logout().permitAll();
        http.rememberMe().key(JWTSECRET).tokenValiditySeconds(7 * 24 * 60 * 60);
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
