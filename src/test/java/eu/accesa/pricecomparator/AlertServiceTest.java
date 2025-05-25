package eu.accesa.pricecomparator;

import eu.accesa.pricecomparator.alert.dto.AlertRequestDto;
import eu.accesa.pricecomparator.alert.service.AlertService;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AlertServiceTest {

    @Autowired AlertService service;
    @Autowired PriceRepository priceRepo;

    @MockBean jakarta.mail.javamail.JavaMailSender mailSender;

    @Test
    void checkAlerts_sendsEmailAndDeactivates() {
        // 1) creare alertă
        AlertRequestDto dto = new AlertRequestDto();
        dto.setProductId("P1");
        dto.setUserId("test@x.com");
        dto.setTargetPrice(0.9);
        var alert = service.create(dto);

        // 2) preţ sub ţintă
        priceRepo.save(new PriceRecord("P1", 0.8, LocalDate.now(), 1, "kg", "S"));

        // 3) execuţie
        service.checkAlerts();

        // 4) verificări
        Mockito.verify(mailSender).send(any(MimeMessage.class));
        assert !alert.isActive();
    }
}