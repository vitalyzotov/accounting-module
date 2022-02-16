package ru.vzotov.accounting.test;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.vzotov.accounting.infrastructure.security.User;

import java.util.Arrays;

@Component
public class UserDetailsServiceTestImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final SimpleGrantedAuthority mainAuthority = new SimpleGrantedAuthority("PERSON_" + username.substring(username.indexOf('_') + 1));
        return new User(
                username.substring(0, username.indexOf('_')), "password",
                mainAuthority,
                Arrays.asList(
                        mainAuthority,
                        new SimpleGrantedAuthority("ROLE_USER")
                ));
    }
}
