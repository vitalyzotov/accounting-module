package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.ReceiptItemCategoryPatch;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounting/receipt-items")
@CrossOrigin
public class ReceiptItemsController {
    private final ReceiptsFacade receiptsFacade;

    @Autowired
    public ReceiptItemsController(ReceiptsFacade receiptsFacade) {
        this.receiptsFacade = receiptsFacade;
    }

    @PatchMapping("/{itemIndex}")
    public void patchItemCategory(@PathVariable Integer itemIndex,
                                  @RequestParam("receipt") String receiptId,
                                  @RequestBody ReceiptItemCategoryPatch patch) throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        Validate.notNull(itemIndex);
        Validate.isTrue(itemIndex >= 0);
        receiptsFacade.assignCategoryToItem(new ReceiptId(receiptId), itemIndex, patch.getCategory());
    }
}
