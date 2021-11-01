package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.HoldOperationDTO;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.Operation;

public class OperationDTOAssembler {

    public static AccountOperationDTO toDTO(Operation model) {
        return model == null ? null : new AccountOperationDTO(
                model.account().accountNumber().number(),
                model.date(),
                model.authorizationDate(),
                model.transactionReference() == null ? null : model.transactionReference().reference(),
                model.operationId().idString(),
                String.valueOf(model.type().symbol()),
                model.amount().amount().doubleValue(),
                model.amount().currency().getCurrencyCode(),
                model.description(),
                model.comment(),
                model.category() == null ? null : model.category().id().id()
        );
    }

    public static HoldOperationDTO toDTO(HoldOperation hold) {
        return hold == null ? null : new HoldOperationDTO(
                hold.holdId().value(),
                hold.account().accountNumber().number(),
                hold.date(),
                String.valueOf(hold.type().symbol()),
                hold.amount().amount().doubleValue(),
                hold.amount().currency().getCurrencyCode(),
                hold.description(),
                hold.comment());
    }
}
