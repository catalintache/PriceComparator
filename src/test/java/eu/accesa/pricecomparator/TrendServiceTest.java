package eu.accesa.pricecomparator;

import eu.accesa.pricecomparator.analytics.service.TrendService;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class TrendServiceTest {

    @Autowired TrendService   trend;
    @Autowired PriceRepository priceRepo;

    @Test
    void getDailyMin_collapsesToOneEntryPerDay() {
        priceRepo.save(new PriceRecord("P1", 1.0, LocalDate.parse("2025-05-01"), 1, "kg", "S"));
        priceRepo.save(new PriceRecord("P1", 0.8, LocalDate.parse("2025-05-01"), 1, "kg", "S"));

        var result = trend.getDailyMin("P1",
                LocalDate.parse("2025-05-01"),
                LocalDate.parse("2025-05-01"));

        assertThat(result).singleElement()
                .extracting(p -> p.getUnitPrice())
                .isEqualTo(0.8);
    }
}