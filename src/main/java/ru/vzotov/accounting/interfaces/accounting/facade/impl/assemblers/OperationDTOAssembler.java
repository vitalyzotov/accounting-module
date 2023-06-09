package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;

public class OperationDTOAssembler {

    public static AccountingApi.OperationRef toRef(Operation model) {
        return model == null ? null : new AccountingApi.OperationId(model.operationId().idString());
    }

    public static AccountingApi.OperationRef toRef(OperationId model) {
        return model == null ? null : new AccountingApi.OperationId(model.idString());
    }

    public static AccountingApi.AccountOperation toDTO(Operation model) {
        return model == null ? null : new AccountingApi.AccountOperation(
                model.account().number(),
                model.date(),
                model.authorizationDate(),
                model.transactionReference() == null ? null : model.transactionReference().reference(),
                model.operationId().idString(),
                String.valueOf(model.type().symbol()),
                model.amount().amount().doubleValue(),
                model.amount().currency().getCurrencyCode(),
                model.description(),
                model.comment(),
                model.category() == null ? null : model.category().id()
        );
    }

    public static AccountingApi.CardOperation toDTO(CardOperation model) {
        return model == null ? null : new AccountingApi.CardOperation(
                model.operationId().idString(),
                model.cardNumber().value(),
                PosTerminalDTOAssembler.toDTO(model.terminal()), //nullable
                model.authDate(),
                model.purchaseDate(),
                MoneyDTOAssembler.toDTO(model.amount()),
                model.extraInfo(),
                model.mcc().value()
        );
    }

    public static AccountingApi.HoldOperation toDTO(HoldOperation hold) {
        return hold == null ? null : new AccountingApi.HoldOperation(
                hold.holdId().value(),
                hold.account().number(),
                hold.date(),
                String.valueOf(hold.type().symbol()),
                hold.amount().amount().doubleValue(),
                hold.amount().currency().getCurrencyCode(),
                hold.description(),
                hold.comment());
    }
}
