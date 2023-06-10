package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.MccGroup;
import ru.vzotov.accounting.interfaces.accounting.facade.MccFacade;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

@RestController
@RequestMapping("/accounting/mcc/groups")
@CrossOrigin
public class MccGroupsController {
    private final MccFacade mccFacade;

    public MccGroupsController(MccFacade mccFacade) {
        this.mccFacade = mccFacade;
    }

    @GetMapping
    public List<MccGroup> listGroups() {
        return mccFacade.listGroups();
    }

    @GetMapping("{groupId}")
    public MccGroup getGroup(@PathVariable String groupId) {
        return mccFacade.getGroup(new MccGroupId(groupId));
    }

}
