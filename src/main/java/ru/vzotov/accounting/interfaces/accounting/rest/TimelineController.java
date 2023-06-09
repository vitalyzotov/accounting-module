package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Timeline;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;

@RestController
@RequestMapping("/timeline")
@CrossOrigin
public class TimelineController {

    private final ReceiptsFacade receiptsFacade;

    public TimelineController(ReceiptsFacade receiptsFacade) {
        this.receiptsFacade = receiptsFacade;
    }

    @GetMapping
    public Timeline getTimeline() {
        return receiptsFacade.getTimeline();
    }

}
