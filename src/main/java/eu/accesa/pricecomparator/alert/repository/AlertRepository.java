package eu.accesa.pricecomparator.alert.repository;

import eu.accesa.pricecomparator.alert.model.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByActiveTrue();
}