package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.TimelineDTO;

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
    List<ReceiptDTO> listAllReceipts(LocalDate fromDate, LocalDate toDate);

    /**
     * Get list of all QR codes within date range
     *
     * @param fromDate start date (inclusive)
     * @param toDate   end date (inclusive)
     * @return list of all QR codes
     */
    List<QRCodeDTO> listAllCodes(LocalDate fromDate, LocalDate toDate);

    /**
     * Get QR code by cash receipt ID
     * @param receiptId ID of cash receipt
     * @return QR code
     */
    QRCodeDTO getCode(String receiptId);

    /**
     * Формирование актуального таймлайна.
     * Система подсчитывает количество чеков за каждый месяц, возвращает все месяцы где есть чеки и количество чеков.
     *
     * @return таймлайн
     */
    TimelineDTO getTimeline();

    /**
     * Get receipt from persistent storage
     *
     * @param qrCodeData
     * @return
     * @deprecated use getReceipt(String qrCodeData)
     */
    @Deprecated
    ReceiptDTO loadReceipt(String qrCodeData);

    /**
     * Get receipt from persistent storage
     *
     * @param qrCodeData QR code
     * @return cash receipt
     */
    ReceiptDTO getReceipt(String qrCodeData);

    /**
     * Try loading cash receipt from external systems
     *
     * @param qrCodeData QR data to identify the receipt
     * @return cash receipt if loaded successfully, <code>null</code> otherwise
     */
    ReceiptDTO loadReceiptDetails(QRCodeData qrCodeData);

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
    List<PurchaseCategoryDTO> getAllCategories();

    /**
     * Get purchase category by ID
     *
     * @param id category ID
     * @return category DTO
     */
    PurchaseCategoryDTO getCategory(PurchaseCategoryId id);

    /**
     * Create new purchase category
     *
     * @param name category name
     * @return category DTO
     */
    PurchaseCategoryDTO createNewCategory(String name);

    /**
     * Rename purchase category
     *
     * @param id category ID
     * @param newName new category name
     * @return category DTO
     */
    PurchaseCategoryDTO renameCategory(PurchaseCategoryId id, String newName);
}
