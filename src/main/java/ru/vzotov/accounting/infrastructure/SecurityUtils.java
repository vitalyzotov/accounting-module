package ru.vzotov.accounting.infrastructure;

import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.vzotov.person.domain.model.PersonId;

import java.io.Serializable;
import java.util.Collection;

public class SecurityUtils {

    public static PersonId getCurrentPerson() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(PersonId::isValidAuthority)
                .findFirst()
                .map(PersonId::fromAuthority)
                .orElse(null);
    }

    public static void grantPermission(MutableAclService aclService, Authentication authentication,
                                       Class<?> clazz, Serializable identifier, Permission... permissions) {
        final ObjectIdentity oi = new ObjectIdentityImpl(clazz, identifier);
        final PrincipalSid sid = new PrincipalSid(authentication);
        // Create or update the relevant ACL
        MutableAcl acl;
        try {
            acl = (MutableAcl) aclService.readAclById(oi);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oi);
        }

        acl.setOwner(sid);
        for (Permission permission : permissions) {
            acl.insertAce(acl.getEntries().size(), permission, sid, true);
        }
    }
}
