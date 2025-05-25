package eu.accesa.pricecomparator;

import eu.accesa.pricecomparator.discount.service.DiscountService;
import eu.accesa.pricecomparator.discount.repository.DiscountRepository;
import eu.accesa.pricecomparator.discount.model.Discount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(DiscountService.class)
class DiscountServiceTest {

    @Autowired DiscountService service;
    @Autowired DiscountRepository repo;

    @Test
    void getTopPercent_returnsInDescOrder() {
        repo.save(new Discount("P1", "Store1", 10, LocalDate.now(), LocalDate.now().plusDays(7)));
        repo.save(new Discount("P2", "Store2", 25, LocalDate.now(), LocalDate.now().plusDays(7)));

        var list = service.getTopPercent(1);
        assertThat(list).hasSize(1);
        assertThat(list.getFirst().percentage()).isEqualTo(25);
    }
}
