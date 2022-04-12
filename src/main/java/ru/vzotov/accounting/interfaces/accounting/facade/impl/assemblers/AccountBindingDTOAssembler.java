package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountBindingDTO;
import ru.vzotov.banking.domain.model.AccountBinding;

public class AccountBindingDTOAssembler {

    public static AccountBindingDTO toDTO(AccountBinding binding) {
        return binding == null ? null : new AccountBindingDTO(
                binding.accountNumber().number(),
                binding.from(),
                binding.to()
        );
    }
}
