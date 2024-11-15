package com.example.jeweryapp.demos.web.Service;

import com.example.jeweryapp.demos.web.Entity.User;
import com.example.jeweryapp.demos.web.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));

        User user = optionalUser.orElseThrow(() -> {
            logger.warn("User not found: {}", username);
            return new UsernameNotFoundException("User not found with username: " + username);
        });

        Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        switch (user.getLevel()) {
            case 1:
                logger.info("{} is assigned USER role", user.getUsername());
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
            case 2:
                logger.info("{} is assigned ADMIN role", user.getUsername());
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            default:
                logger.warn("Unknown user level for: {}", user.getUsername());
        }

        return grantedAuthorities;
    }
}