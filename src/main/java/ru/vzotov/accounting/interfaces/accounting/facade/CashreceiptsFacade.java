package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.cashreceipt.domain.model.CheckId;
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

public interface CashreceiptsFacade {

    /**
     * Получение всех чеков в заданном интервале дат
     *
     * @param fromDate начальная дата (включается в интервал)
     * @param toDate   конечная дата (включается в интервал)
     * @return список чеков
     */
    List<ReceiptDTO> listAllChecks(LocalDate fromDate, LocalDate toDate);

    /**
     * Получение всех зарегистрированных кодов чеков в заданном интервале дат
     *
     * @param fromDate начальная дата чека (включительно)
     * @param toDate   конечная дата чека (включительно)
     * @return список кодов чеков
     */
    List<QRCodeDTO> listAllCodes(LocalDate fromDate, LocalDate toDate);

    QRCodeDTO getCode(String receiptId);

    /**
     * Формирование актуального таймлайна.
     * Система подсчитывает количество чеков за каждый месяц, возвращает все месяцы где есть чеки и количество чеков.
     *
     * @return таймлайн
     */
    TimelineDTO getTimeline();

    /**
     * Get check from persistent storage
     *
     * @param qrCodeData
     * @return
     * @deprecated use getCheck(String qrCodeData)
     */
    @Deprecated
    ReceiptDTO loadCheck(String qrCodeData);

    /**
     * Get check from persistent storage
     *
     * @param qrCodeData QR code
     * @return check
     */
    ReceiptDTO getCheck(String qrCodeData);

    /**
     * Попытаться загрузить чек из внешних систем
     * @param qrCodeData
     * @return
     */
    ReceiptDTO loadCheckDetails(QRCodeData qrCodeData);

    /**
     * Назначение категории для позиции чека
     *
     * @param checkId     суррогатный идентификатор чека
     * @param itemIndex   номер позиции
     * @param newCategory категория, которую нужно назначить позиции
     * @throws ReceiptNotFoundException     если чек не найден
     * @throws ReceiptItemNotFoundException если позиция в чеке не найдена
     */
    void assignCategoryToItem(CheckId checkId,
                              Integer itemIndex,
                              String newCategory) throws ReceiptNotFoundException, ReceiptItemNotFoundException;

    /**
     * Получение списка категорий
     *
     * @return список категорий
     */
    List<PurchaseCategoryDTO> getAllCategories();

    /**
     * Получение категории
     *
     * @param id идентификатор категории
     * @return категория
     */
    PurchaseCategoryDTO getCategory(PurchaseCategoryId id);

    /**
     * Создание новой категории
     *
     * @param name название категории
     * @return категория
     */
    PurchaseCategoryDTO createNewCategory(String name);

    /**
     * Переименование категории
     *
     * @param id идентификатор
     * @param newName новое название
     * @return категория
     */
    PurchaseCategoryDTO renameCategory(PurchaseCategoryId id, String newName);
}
