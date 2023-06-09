package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.MccDetails;
import ru.vzotov.accounting.interfaces.accounting.facade.MccFacade;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

@RestController
@RequestMapping("/accounting/mcc/details")
@CrossOrigin
public class MccDetailsController {
    private final MccFacade mccFacade;

    public MccDetailsController(MccFacade mccFacade) {
        this.mccFacade = mccFacade;
    }

    @GetMapping(params = {"!group"})
    public List<MccDetails> listAllDetails() {
        return mccFacade.listDetails();
    }

    @GetMapping(params = {"group"})
    public List<MccDetails> listGroupDetails(@RequestParam("group") String group) {
        return mccFacade.listGroupDetails(new MccGroupId(group));
    }

    @GetMapping("{code}")
    public MccDetails getDetails(@PathVariable String code) {
        return mccFacade.getDetails(new MccCode(code));
    }

}
