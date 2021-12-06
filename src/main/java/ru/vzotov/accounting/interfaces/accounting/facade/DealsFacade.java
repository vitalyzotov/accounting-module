package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DealsFacade {
    DealDTO createDeal(LocalDate date, long amount, String currency, String description, String comment,
                       Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases) throws CategoryNotFoundException;

    DealDTO modifyDeal(String dealId, LocalDate date, long amount, String currency, String description, String comment,
                       Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases) throws DealNotFoundException, CategoryNotFoundException;

    DealDTO deleteDeal(String dealId) throws DealNotFoundException;

    List<DealDTO> listDeals(LocalDate from, LocalDate to);

    DealDTO getDeal(String dealId) throws DealNotFoundException;

    LocalDate getMinDealDate();
    LocalDate getMaxDealDate();
}
