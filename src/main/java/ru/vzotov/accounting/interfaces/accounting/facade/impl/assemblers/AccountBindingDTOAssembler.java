package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.AccountBinding;

public class AccountBindingDTOAssembler {

    public static AccountingApi.AccountBinding toDTO(AccountBinding binding) {
        return binding == null ? null : new AccountingApi.AccountBinding(
                binding.accountNumber().number(),
                binding.from(),
                binding.to()
        );
    }
}
