package ru.vzotov.accounting.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class User extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final GrantedAuthority mainAuthority;

    public User(String username, String password, GrantedAuthority mainAuthority, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mainAuthority = mainAuthority;
    }

    public GrantedAuthority getMainAuthority() {
        return mainAuthority;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" +
                "Username=" + this.getUsername() + ", " +
                "Password=[PROTECTED], " +
                "Enabled=" + this.isEnabled() + ", " +
                "AccountNonExpired=" + this.isAccountNonExpired() + ", " +
                "credentialsNonExpired=" + this.isCredentialsNonExpired() + ", " +
                "AccountNonLocked=" + this.isAccountNonLocked() + ", " +
                "Main Authority=" + this.getMainAuthority() + ", " +
                "Granted Authorities=" + this.getAuthorities() + "]";
    }

}
