package ru.vzotov.accounting.interfaces.accounting.facade.impl.assembler;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ItemDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.ReceiptDTOAssembler;
import ru.vzotov.cashreceipt.ReceiptFactory;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.time.Month.JULY;
import static org.assertj.core.api.Assertions.assertThat;


public class ReceiptDTOAssemblerTest {

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Test
    public void toDTO() throws IOException {
        final ReceiptDTOAssembler assembler = new ReceiptDTOAssembler();
        final Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt015.json");

        final ReceiptDTO dto = assembler.toDTO(receipt);

        assertThat(dto).isNotNull();
        assertThat(dto.getDateTime())
                .isEqualTo(LocalDateTime.of(2018, JULY, 17, 17, 8, 0));

        assertThat(dto.getTotalSum().getAmount()).isEqualTo(15000);
        assertThat(dto.getTotalSum().getCurrency()).isEqualTo("RUR");

        assertThat(dto.getFiscalInfo().getKktRegId()).isEqualTo("0000485300049451");
        assertThat(dto.getFiscalInfo().getKktNumber()).isNull();
        assertThat(dto.getFiscalInfo().getFiscalDocumentNumber()).isEqualTo("2056");
        assertThat(dto.getFiscalInfo().getFiscalDriveNumber()).isEqualTo("9288000100080294");
        assertThat(dto.getFiscalInfo().getFiscalSign()).isEqualTo("2024263777");

        assertThat(dto.getItems()).hasSize(1);
        ItemDTO item = dto.getItems().get(0);
        assertThat(item.getName()).isEqualTo("Пепси напиток 0,8л.");
        assertThat(item.getQuantity()).isEqualTo(1d);
        assertThat(item.getPrice().getAmount()).isEqualTo(15000);
        assertThat(item.getSum().getAmount()).isEqualTo(15000);
    }

    @Test
    public void toDTOList() throws IOException {
        final ReceiptDTOAssembler assembler = new ReceiptDTOAssembler();
        final Receipt receipt1 = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt015.json");
        final Receipt receipt2 = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt017.json");

        List<ReceiptDTO> list = assembler.toDTOList(Arrays.asList(receipt1, receipt2));
        Assertions.assertThat(list).hasSize(2);
    }
}
