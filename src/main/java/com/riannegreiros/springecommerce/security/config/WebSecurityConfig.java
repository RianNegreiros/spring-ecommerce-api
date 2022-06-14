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

    public static final String[] AUTH_WHITELIST = {
            "/authenticate",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/webjars/**"
    };

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
        http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
        http.authorizeRequests().antMatchers("/api/customers/**").permitAll();
        http.authorizeRequests().antMatchers("/api/token/**").permitAll();
        http.authorizeRequests().antMatchers("/api/user/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/api/categories/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/api/brands/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/api/products/**").hasAnyAuthority("ADMIN", "SALESPERSON", "SHIPPER");
        http.authorizeRequests().antMatchers("/api/settings/**").hasAnyAuthority("ADMIN");
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
