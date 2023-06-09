package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeline")
@CrossOrigin
public class TimelineController {

    private final ReceiptsFacade receiptsFacade;

    @Autowired
    public TimelineController(ReceiptsFacade receiptsFacade) {
        this.receiptsFacade = receiptsFacade;
    }

    @GetMapping
    public AccountingApi.Timeline getTimeline() {
        return receiptsFacade.getTimeline();
    }

}
