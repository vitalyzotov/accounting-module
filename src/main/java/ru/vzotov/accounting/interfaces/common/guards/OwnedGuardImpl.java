package ru.vzotov.accounting.interfaces.common.guards;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.vzotov.person.domain.model.Owned;

import java.util.List;

@Service
public class OwnedGuardImpl implements OwnedGuard {
    @Override
    @PreAuthorize("#entity==null or hasAuthority(#entity.owner().authority())")
    public <E extends Owned> E accessing(E entity) {
        return entity;
    }

    @Override
    @PostFilter("hasAuthority(filterObject.owner().authority())")
    public <E extends Owned> List<E> filter(List<E> list) {
        return list;
    }
}
