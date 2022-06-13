package com.riannegreiros.springecommerce.security.userDetails;

import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal() {
    }

    public UserPrincipal(Customer customer) {
        this.username = customer.getEmail();
        this.password = customer.getPassword();

        authorities = customer.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().toUpperCase()))).collect(Collectors.toList());
    }

    public UserPrincipal(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();

        authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().toUpperCase()))).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(String roleName) {
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(roleName);
        }
        return hasRole;
    }
}
