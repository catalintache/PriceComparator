package eu.accesa.pricecomparator;

import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerTest {

    @Autowired MockMvc mvc;
    @Autowired PriceRepository repo;

    @Test
    void listByDate_returnsPrices() throws Exception {
        // seed DB
        repo.save(TestData.price("P001", 0.99, LocalDate.parse("2025-05-01")));

        mvc.perform(get("/api/prices")
                        .param("date", "2025-05-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value("P001"))
                .andExpect(jsonPath("$[0].unitPrice").value(0.99));
    }
}
