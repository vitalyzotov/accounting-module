package ru.vzotov.cashreceipt.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReceiptLoaderSchedule {

    private static final long ONE_MINUTE = 60 * 1000;
    private static final long TEN_MINUTES = 10 * 60 * 1000;
    private static final long THIRTY_MINUTES = 3 * TEN_MINUTES;

    private final ReceiptRegistrationService registrationService;

    public ReceiptLoaderSchedule(ReceiptRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Scheduled(fixedDelay = THIRTY_MINUTES, initialDelay = ONE_MINUTE)
    public void triggerLoadReceipts() {
        registrationService.loadNewReceipts();
    }

}
