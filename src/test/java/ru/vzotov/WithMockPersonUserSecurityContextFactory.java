package ru.vzotov;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import ru.vzotov.accounting.infrastructure.security.User;
import ru.vzotov.person.domain.model.PersonId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithMockPersonUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockPersonUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockPersonUser personUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User principal = new User(
                personUser.value(),
                personUser.password(),
                new SimpleGrantedAuthority(new PersonId(personUser.person()).authority()),
                getAuthorities(personUser.roles(), personUser.person()));

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }

    private static List<GrantedAuthority> getAuthorities(String[] roles, String person) {
        return Stream.concat(Stream.of(new PersonId(person).authority()), Arrays.stream(roles).map(a -> "ROLE_" + a))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
