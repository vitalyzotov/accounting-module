package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.MccFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MccGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

@RestController
@RequestMapping("/accounting/mcc/groups")
@CrossOrigin
public class MccGroupsController {
    @Autowired
    private MccFacade mccFacade;

    @GetMapping
    public List<MccGroupDTO> listGroups() {
        return mccFacade.listGroups();
    }

    @GetMapping("{groupId}")
    public MccGroupDTO getGroup(@PathVariable String groupId) {
        return mccFacade.getGroup(new MccGroupId(groupId));
    }

}
