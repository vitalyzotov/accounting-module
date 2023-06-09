package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.OperationNotFoundException;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.banking.domain.model.OperationType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/accounting/operations")
@CrossOrigin
public class OperationsController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<AccountingApi.AccountOperation> listOperations(@RequestParam(required = false) String type,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return accountingFacade.listOperations(
                type == null ? null : OperationType.of(type.charAt(0)),
                from, to);
    }

    @GetMapping("{operationId}")
    public AccountingApi.AccountOperation getOperation(@PathVariable String operationId) throws OperationNotFoundException, AccountNotFoundException {
        return accountingFacade.getOperation(operationId);
    }

    @DeleteMapping("{operationId}")
    public AccountingApi.AccountOperation deleteOperation(@PathVariable String operationId) throws OperationNotFoundException {
        return accountingFacade.deleteOperation(operationId);
    }

    @PatchMapping("{operationId}")
    public AccountingApi.AccountOperation patchOperation(@PathVariable String operationId,
                                                         @RequestBody HashMap<String, Object> patch)
            throws OperationNotFoundException, CategoryNotFoundException {
        Validate.notEmpty(operationId);

        AccountingApi.AccountOperation result = null;
        if (patch.containsKey("categoryId")) {
            Long categoryId = (Long) patch.get("categoryId");
            if (categoryId == null) throw new IllegalArgumentException("categoryId is null");
            result = accountingFacade.assignCategoryToOperation(operationId, categoryId);
        }

        if (patch.containsKey("comment")) {
            result = accountingFacade.modifyComment(operationId, Objects.toString(patch.get("comment"), null));
        }
        return result;
    }

}
