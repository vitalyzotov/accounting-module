package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.infrastructure.security.SecurityUtils;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.PurchaseCategoryAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.QRCodeDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.ReceiptDTOAssembler;
import ru.vzotov.accounting.interfaces.common.guards.OwnedGuard;
import ru.vzotov.accounting.interfaces.common.assembler.Assembler;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptRegistrationService;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptsFacadeImpl implements ReceiptsFacade {

    private static final Logger log = LoggerFactory.getLogger(ReceiptsFacadeImpl.class);

    private final ReceiptRepository receiptRepository;

    private final PurchaseCategoryRepository categoryRepository;

    private final QRCodeRepository codeRepository;

    private final ReceiptRegistrationService receiptRegistrationService;

    private final OwnedGuard ownedGuard;

    public ReceiptsFacadeImpl(ReceiptRepository receiptRepository,
                              PurchaseCategoryRepository categoryRepository,
                              QRCodeRepository codeRepository,
                              ReceiptRegistrationService receiptRegistrationService,
                              OwnedGuard ownedGuard) {
        this.receiptRepository = receiptRepository;
        this.categoryRepository = categoryRepository;
        this.codeRepository = codeRepository;
        this.receiptRegistrationService = receiptRegistrationService;
        this.ownedGuard = ownedGuard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Receipt> listAllReceipts(LocalDate fromDate, LocalDate toDate) {
        List<Receipt> receipts = receiptRepository.findByDate(SecurityUtils.getAuthorizedPersons(), fromDate, toDate);
        return receipts.stream().map(receipt -> new ReceiptDTOAssembler().toDTO(receipt)).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.QRCode> listAllCodes(LocalDate fromDate, LocalDate toDate) {
        List<QRCode> codes = codeRepository.findByDate(SecurityUtils.getAuthorizedPersons(), fromDate, toDate);
        return codes.stream().map(code -> new QRCodeDTOAssembler().toDTO(code)).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.QRCode getCode(String receiptId) {
        QRCode code = ownedGuard.accessing(codeRepository.find(new ReceiptId(receiptId)));
        return new QRCodeDTOAssembler().toDTO(code);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.Timeline getTimeline() {
        final AccountingApi.Timeline result = new AccountingApi.Timeline();
        final ZoneId zoneId = ZoneId.systemDefault().normalized();
        final LocalDate today = LocalDate.now();
        final Collection<PersonId> authorizedPersons = SecurityUtils.getAuthorizedPersons();
        final Receipt oldest = receiptRepository.findOldest(authorizedPersons);

        if (oldest != null) {
            LocalDate startOfMonth = oldest.dateTime().atZone(zoneId).toLocalDate().withDayOfMonth(1);
            LocalDate endOfMonth;
            do {
                endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

                log.debug("Count receipts from {} to {}", startOfMonth, endOfMonth);

                final long count = receiptRepository.countByDate(authorizedPersons, startOfMonth, endOfMonth);
                final AccountingApi.TimePeriod period = new AccountingApi.TimePeriod(
                        startOfMonth,
                        endOfMonth,
                        count
                );
                result.periods().add(period);

                startOfMonth = startOfMonth.plusMonths(1);
            } while (endOfMonth.isBefore(today));
        }

        return result;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Deprecated
    public AccountingApi.Receipt loadReceipt(String qrCodeData) {
        Receipt receipt = receiptRepository.findByQRCodeData(new QRCodeData(qrCodeData));
        return receipt == null ? null : new ReceiptDTOAssembler().toDTO(receipt);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.Receipt getReceipt(String qrCodeData) {
        Receipt receipt = ownedGuard.accessing(receiptRepository.findByQRCodeData(new QRCodeData(qrCodeData)));
        return new ReceiptDTOAssembler().toDTO(receipt);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.Receipt loadReceiptDetails(QRCodeData qrCodeData) {
        Validate.notNull(qrCodeData);
        try {
            ownedGuard.accessing(codeRepository.findByQRCodeData(qrCodeData));
            ReceiptId receiptId = receiptRegistrationService.loadDetails(qrCodeData);
            return new ReceiptDTOAssembler().toDTO(ownedGuard.accessing(receiptRepository.find(receiptId)));
        } catch (ReceiptNotFoundException | IOException e) {
            log.error("Unable to load receipt details", e);
        }
        return null;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public void assignCategoryToItem(ReceiptId receiptId, Integer itemIndex, String newCategory)
            throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        Receipt receipt = receiptRepository.find(receiptId);
        if (receipt == null) {
            throw new ReceiptNotFoundException();
        }
        //fixme: sync with owner of receipt
        PurchaseCategory newCat = ownedGuard.accessing(categoryRepository
                .findByName(SecurityUtils.getCurrentPerson(), newCategory));

        if (receipt.products().items().stream().filter(item -> item.index().equals(itemIndex)).count() != 1) {
            throw new ReceiptItemNotFoundException();
        }

        receipt.assignCategoryToItem(itemIndex, newCat);
        receiptRepository.store(receipt);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.PurchaseCategory> getAllCategories() {
        Assembler<AccountingApi.PurchaseCategory, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        return assembler.toDTOList(categoryRepository.findAll(SecurityUtils.getCurrentPerson()));
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.PurchaseCategory getCategory(PurchaseCategoryId id) {
        Assembler<AccountingApi.PurchaseCategory, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        return assembler.toDTO(ownedGuard.accessing(categoryRepository.findById(id)));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.PurchaseCategory createNewCategory(String name) {
        Assembler<AccountingApi.PurchaseCategory, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        PurchaseCategory category = new PurchaseCategory(PurchaseCategoryId.nextId(), SecurityUtils.getCurrentPerson(), name);
        categoryRepository.store(category);
        return assembler.toDTO(category);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.PurchaseCategory renameCategory(PurchaseCategoryId id, String newName) {
        final Assembler<AccountingApi.PurchaseCategory, PurchaseCategory> assembler = new PurchaseCategoryAssembler();
        final PurchaseCategory category = ownedGuard.accessing(categoryRepository.findById(id));
        category.rename(newName);
        categoryRepository.store(category);
        return assembler.toDTO(category);
    }

}
