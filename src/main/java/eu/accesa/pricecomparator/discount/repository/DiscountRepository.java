package eu.accesa.pricecomparator.discount.repository;

import eu.accesa.pricecomparator.discount.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    // all discounts still active today or in future
    List<Discount> findByToDateAfter(LocalDate date);

    // discounts that started after given date
    List<Discount> findByFromDateAfter(LocalDate date);

    List<Discount> findByFromDateLessThanEqualAndToDateGreaterThanEqual(
            LocalDate from, LocalDate to);
}