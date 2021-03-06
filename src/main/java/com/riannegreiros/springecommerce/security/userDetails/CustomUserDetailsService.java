package com.riannegreiros.springecommerce.security.userDetails;

import com.riannegreiros.springecommerce.modules.user.entity.User;
import com.riannegreiros.springecommerce.modules.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found by this email: " + email));

        return new UserPrincipal(user);
    }
}
