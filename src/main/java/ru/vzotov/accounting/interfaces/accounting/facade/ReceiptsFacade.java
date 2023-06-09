package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ReceiptsFacade {

    /**
     * Get list of all cash receipts within date range
     *
     * @param fromDate start date (inclusive)
     * @param toDate   end date (inclusive)
     * @return list of cash receipts
     */
    List<AccountingApi.Receipt> listAllReceipts(LocalDate fromDate, LocalDate toDate);

    /**
     * Get list of all QR codes within date range
     *
     * @param fromDate start date (inclusive)
     * @param toDate   end date (inclusive)
     * @return list of all QR codes
     */
    List<AccountingApi.QRCode> listAllCodes(LocalDate fromDate, LocalDate toDate);

    /**
     * Get QR code by cash receipt ID
     * @param receiptId ID of cash receipt
     * @return QR code
     */
    AccountingApi.QRCode getCode(String receiptId);

    /**
     * Calculate timeline.
     * Application counts number of cash receipts for each month, returns all months having cash receipts.
     *
     * @return timeline data
     */
    AccountingApi.Timeline getTimeline();

    /**
     * Get receipt from persistent storage
     *
     * @param qrCodeData
     * @return
     * @deprecated use getReceipt(String qrCodeData)
     */
    @Deprecated
    AccountingApi.Receipt loadReceipt(String qrCodeData);

    /**
     * Get receipt from persistent storage
     *
     * @param qrCodeData QR code
     * @return cash receipt
     */
    AccountingApi.Receipt getReceipt(String qrCodeData);

    /**
     * Try loading cash receipt from external systems
     *
     * @param qrCodeData QR data to identify the receipt
     * @return cash receipt if loaded successfully, <code>null</code> otherwise
     */
    AccountingApi.Receipt loadReceiptDetails(QRCodeData qrCodeData);

    /**
     * Assign category to receipt item
     *
     * @param receiptId   ID of receipt
     * @param itemIndex   index of item
     * @param newCategory new category
     * @throws ReceiptNotFoundException     if receipt was not found
     * @throws ReceiptItemNotFoundException if receipt item was not found
     */
    void assignCategoryToItem(ReceiptId receiptId,
                              Integer itemIndex,
                              String newCategory) throws ReceiptNotFoundException, ReceiptItemNotFoundException;

    /**
     * Get list of purchase categories
     *
     * @return list of purchase categories
     */
    List<AccountingApi.PurchaseCategory> getAllCategories();

    /**
     * Get purchase category by ID
     *
     * @param id category ID
     * @return category DTO
     */
    AccountingApi.PurchaseCategory getCategory(PurchaseCategoryId id);

    /**
     * Create new purchase category
     *
     * @param name category name
     * @return category DTO
     */
    AccountingApi.PurchaseCategory createNewCategory(String name);

    /**
     * Rename purchase category
     *
     * @param id category ID
     * @param newName new category name
     * @return category DTO
     */
    AccountingApi.PurchaseCategory renameCategory(PurchaseCategoryId id, String newName);
}
