package eu.accesa.pricecomparator;

import eu.accesa.pricecomparator.catalog.service.PriceService;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PriceServiceTest {

    @Autowired PriceService   service;
    @Autowired PriceRepository repo;

    @Test
    void importCsv_savesAllRows() throws Exception {
        LocalDate date = LocalDate.of(2025, 5, 1);
        service.importPrices(date);                    // citeşte CSV-ul de test
        assertThat(repo.count()).isEqualTo(3);        // CSV-ul are 3 rânduri
    }
}