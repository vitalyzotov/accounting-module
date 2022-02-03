package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;

@Service
public class PurchaseCategoryGuardImpl implements PurchaseCategoryGuard {

    private static final Logger log = LoggerFactory.getLogger(PurchaseCategoryGuard.class);

    @Override
    @PreAuthorize("hasAuthority(#entity.owner().authority())")
    //@PostAuthorize("hasAuthority(returnObject.owner().authority())")
    public PurchaseCategory accessing(PurchaseCategory entity) {
        log.debug("Authentication for {} owned by {}: {}", entity, entity.owner(), SecurityContextHolder.getContext().getAuthentication());
        return entity;
    }
}
