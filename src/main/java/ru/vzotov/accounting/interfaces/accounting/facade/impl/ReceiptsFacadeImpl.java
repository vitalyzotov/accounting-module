package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptRegistrationService;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.TimePeriodDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.TimelineDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.ReceiptDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.PurchaseCategoryAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.QRCodeDTOAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.Assembler;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptsFacadeImpl implements ReceiptsFacade {

    private static final Logger log = LoggerFactory.getLogger(ReceiptsFacadeImpl.class);

    private final ReceiptRepository receiptRepository;

    private final PurchaseCategoryRepository categoryRepository;

    private final QRCodeRepository codeRepository;

    private final ReceiptRegistrationService receiptRegistrationService;

    public ReceiptsFacadeImpl(ReceiptRepository receiptRepository,
                              PurchaseCategoryRepository categoryRepository,
                              QRCodeRepository codeRepository,
                              ReceiptRegistrationService receiptRegistrationService) {
        this.receiptRepository = receiptRepository;
        this.categoryRepository = categoryRepository;
        this.codeRepository = codeRepository;
        this.receiptRegistrationService = receiptRegistrationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<ReceiptDTO> listAllReceipts(LocalDate fromDate, LocalDate toDate) {
        List<Receipt> receipts = receiptRepository.findByDate(fromDate, toDate);
        return receipts.stream().map(receipt -> new ReceiptDTOAssembler().toDTO(receipt)).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<QRCodeDTO> listAllCodes(LocalDate fromDate, LocalDate toDate) {
        List<QRCode> codes = codeRepository.findByDate(fromDate, toDate);
        return codes.stream().map(code -> new QRCodeDTOAssembler().toDTO(code)).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public QRCodeDTO getCode(String receiptId) {
        QRCode code = codeRepository.find(new ReceiptId(receiptId));
        return new QRCodeDTOAssembler().toDTO(code);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public TimelineDTO getTimeline() {
        final TimelineDTO result = new TimelineDTO();
        final ZoneId zoneId = ZoneId.systemDefault().normalized();
        final LocalDate today = LocalDate.now();
        final Receipt oldest = receiptRepository.findOldest();

        if (oldest != null) {
            LocalDate startOfMonth = oldest.dateTime().atZone(zoneId).toLocalDate().withDayOfMonth(1);
            LocalDate endOfMonth;
            do {
                endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

                log.debug("Count receipts from {} to {}", startOfMonth, endOfMonth);

                final long count = receiptRepository.countByDate(startOfMonth, endOfMonth);
                final TimePeriodDTO period = new TimePeriodDTO(
                        startOfMonth,
                        endOfMonth,
                        count
                );
                result.getPeriods().add(period);

                startOfMonth = startOfMonth.plusMonths(1);
            } while (endOfMonth.isBefore(today));
        }

        return result;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Deprecated
    public ReceiptDTO loadReceipt(String qrCodeData) {
        Receipt receipt = receiptRepository.findByQRCodeData(new QRCodeData(qrCodeData));
        return receipt == null ? null : new ReceiptDTOAssembler().toDTO(receipt);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public ReceiptDTO getReceipt(String qrCodeData) {
        Receipt receipt = receiptRepository.findByQRCodeData(new QRCodeData(qrCodeData));
        return receipt == null ? null : new ReceiptDTOAssembler().toDTO(receipt);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public ReceiptDTO loadReceiptDetails(QRCodeData qrCodeData) {
        Validate.notNull(qrCodeData);
        try {
            ReceiptId receiptId = receiptRegistrationService.loadDetails(qrCodeData);
            return new ReceiptDTOAssembler().toDTO(receiptRepository.find(receiptId));
        } catch (ReceiptNotFoundException | IOException e) {
            log.error("Unable to load receipt details", e);
        }
        return null;
    }

    @Override
    @Transactional(value = "accounting-tx")
    public void assignCategoryToItem(ReceiptId receiptId, Integer itemIndex, String newCategory)
            throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        Receipt receipt = receiptRepository.find(receiptId);
        if (receipt == null) {
            throw new ReceiptNotFoundException();
        }
        PurchaseCategory newCat = categoryRepository.findByName(newCategory);

        if (receipt.products().items().stream().filter(item -> item.index().equals(itemIndex)).count() != 1) {
            throw new ReceiptItemNotFoundException();
        }

        receipt.assignCategoryToItem(itemIndex, newCat);
        receiptRepository.store(receipt);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<PurchaseCategoryDTO> getAllCategories() {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        return assembler.toDTOList(categoryRepository.findAll());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public PurchaseCategoryDTO getCategory(PurchaseCategoryId id) {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        return assembler.toDTO(categoryRepository.findById(id));
    }

    @Override
    @Transactional(value = "accounting-tx")
    public PurchaseCategoryDTO createNewCategory(String name) {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        PurchaseCategory category = new PurchaseCategory(PurchaseCategoryId.nextId(), name);
        categoryRepository.store(category);
        return assembler.toDTO(category);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public PurchaseCategoryDTO renameCategory(PurchaseCategoryId id, String newName) {
        Assembler<PurchaseCategoryDTO, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        PurchaseCategory category = categoryRepository.findById(id);
        category.rename(newName);
        categoryRepository.store(category);
        return assembler.toDTO(category);
    }

}
