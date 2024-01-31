package ru.vzotov.purchases.domain.model;

import org.springframework.data.jpa.domain.Specification;
import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Interface representing a repository for managing purchases.
 */
public interface PurchaseRepository {

    List<Purchase> findAll(Specification<Purchase> specification);

    /**
     * Finds a purchase by its ID.
     *
     * @param id the ID of the purchase to find
     * @return the purchase with the specified ID, or null if not found
     */
    Purchase find(PurchaseId id);

    /**
     * Store a purchase in the repository.
     *
     * @param purchase The purchase to be stored.
     */
    void store(Purchase purchase);

    /**
     * Finds purchases within a specified date range for the given owners.
     *
     * @param owners       a collection of PersonId objects representing the owners of the purchases
     * @param fromDateTime the starting date and time for the search
     * @param toDateTime   the ending date and time for the search
     * @return a list of Purchase objects matching the specified criteria
     */
    List<Purchase> findByDate(Collection<PersonId> owners, LocalDateTime fromDateTime, LocalDateTime toDateTime);

    /**
     * Deletes a purchase with the specified ID from the repository.
     *
     * @param id the ID of the purchase to be deleted
     * @return true if the purchase was successfully deleted, false otherwise
     */
    boolean delete(PurchaseId id);
}
